package com.hopsquad.hopsquadapp.activities;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hopsquad.hopsquadapp.R;
import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.StripeInfo;
import com.hopsquad.hopsquadapp.viewmodels.OrderHistoryViewModel;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;


public class PaymentActivity extends AppCompatActivity {

    private CardInputWidget mCardInputWidget;
    private EditText mZipcode;
    private EditText mName;
    private LiveData<StripeInfo> si;
    private WebServiceRepository repository;
    private Float totalPrice;
    private OrderHistoryViewModel viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        totalPrice = intent.getFloatExtra("orderTotal", 0);
        TextView cost = (TextView)findViewById(R.id.Cost);
        cost.setText("Total: " + totalPrice);
        mName = (EditText) findViewById(R.id.name_input);
        mZipcode = (EditText) findViewById(R.id.zip_input);
        Button mEmailSignInButton = (Button) findViewById(R.id.submit_button);
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        repository = new WebServiceRepository();
        viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
        viewModel.setRepository(new WebServiceRepository());
        viewModel.init();

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPayment();
            }
        });
    }

    private void submitPayment() {
        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            Toast.makeText(getApplicationContext(), "Card not added", Toast.LENGTH_SHORT).show();
        }
        else if (totalPrice == 0) {
            Toast.makeText(getApplicationContext(), "Payment total error", Toast.LENGTH_SHORT).show();
        }
        else {
            cardToSave.setName(mName.getText().toString());
            cardToSave.setAddressZip(mZipcode.getText().toString());
            cardToSave.validateNumber();
            cardToSave.validateCVC();
            if (!cardToSave.validateCard()) {
                Toast.makeText(getApplicationContext(), "Card couldn't be validated", Toast.LENGTH_SHORT).show();

            }
            else {
                Stripe stripe = new Stripe(this, "pk_test_6pRNASCoBOKtIshFeQd4XMUh"/*ToDo temporary key, need to get actual one*/);
                stripe.createToken(
                        cardToSave,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                Token paymentToken = token;
                                sendStripe(totalPrice, paymentToken);
                                finish();
                                //launchMainActivity();
                            }

                            public void onError(Exception error) {
                                Toast.makeText(getApplicationContext(), "Error occured processing payment", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        }

    }

    private void launchMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public LiveData<StripeInfo> sendStripe(Float price, Token payment) {

        StripeInfo stripe = new StripeInfo();
        stripe.totalPrice = price;
        stripe.paymentToken = payment;

        si = repository.sendStripe(stripe);
        si.observe(this, order -> {
            if (order == null || order.paymentToken == null) {
                Toast.makeText(getApplicationContext(), "Error processing payment", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.order_succesfully_placed_msg, Toast.LENGTH_LONG).show();
                // ToDo not sure If I did the order correct (creating the viewModel object)
                viewModel.setHistoryOrderSelected(null);
                // Todo Refresh orders list
                si.removeObservers(this);
            }
        });
        return si;
    }

}
