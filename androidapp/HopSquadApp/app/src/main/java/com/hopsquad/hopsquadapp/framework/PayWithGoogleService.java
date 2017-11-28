package com.hopsquad.hopsquadapp.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.identity.intents.model.UserAddress;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardInfo;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by memoak on 11/27/2017.
 */

public class PayWithGoogleService {

    public static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 38192;

    private PaymentsClient mPaymentsClient;
    private boolean isReadyToPay = false;
    private String mResultToken = null;

    public PayWithGoogleService(Context context) {
        initializePaymentsClient(context);
    }

    public void confirmOrder(Activity context, float total) {
        PaymentDataRequest request = createPaymentDataRequest(total);
        if (request != null) {
            AutoResolveHelper.resolveTask(mPaymentsClient.loadPaymentData(request),
                    context,
                    LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    private void initializePaymentsClient(Context context) {
        mPaymentsClient = Wallet.getPaymentsClient(context, new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build());

        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .build();

        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(task1 -> {
            try {
                boolean result = task1.getResult(ApiException.class);
                isReadyToPay = result;

            } catch (ApiException apie) {
                apie.printStackTrace();
            }
        });
    }

    public boolean canHandleRequestCode(int requestCode) {
        return requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE;
    }

    public void handleActivityResult(int resultCode, Intent data) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                PaymentData paymentData = PaymentData.getFromIntent(data);
                // This is the raw JSON string version of your Stripe token.
                String rawToken = paymentData.getPaymentMethodToken().getToken();

                mResultToken = rawToken;

                break;
            case Activity.RESULT_CANCELED:
                break;
            case AutoResolveHelper.RESULT_ERROR:
                Status status = AutoResolveHelper.getStatusFromIntent(data);
                // Log the status for debugging
                // Generally there is no need to show an error to
                // the user as the Google Payment API will do that
                break;
            default:
                // Do nothing.
        }
    }

    public String getResultToken() {
        try {
            return parseTokenInvoiceId(mResultToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String parseTokenInvoiceId(String token) throws JSONException {
        JSONObject jsonObject = new JSONObject(token);
        String token_id = jsonObject.getString("id");
        return token_id;
    }

    private PaymentDataRequest createPaymentDataRequest(float total) {
        String totalPrice = String.format("%.2f", total);
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(
                                TransactionInfo.newBuilder()

                                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice(totalPrice)
                                        .setCurrencyCode("USD")
                                        .build())
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }

    private PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", WebServiceRepository.STRIPE_PUBLISHABLE_KEY)
                .addParameter("stripe:version", "5.1.0")
                .build();
    }
}
