package com.hopsquad.hopsquadapp;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hopsquad.hopsquadapp.api.WebServiceRepository;
import com.hopsquad.hopsquadapp.models.StripeInfo;
import com.hopsquad.hopsquadapp.viewmodels.TapListViewModel;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;


public class PaymentActivity extends AppCompatActivity {

    CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
    private EditText mZipcode = (EditText) findViewById(R.id.zip_input);
    private EditText mName = (EditText) findViewById(R.id.name_input);
    private LiveData<StripeInfo> si;
    private WebServiceRepository repository = new WebServiceRepository();
    private Float totalPrice;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        totalPrice = intent.getFloatExtra("orderTotal", 0);
        TextView cost = (TextView)findViewById(R.id.Cost);
        cost.setText("Total: " + totalPrice);

        Button mEmailSignInButton = (Button) findViewById(R.id.submit_button);
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
                                // ToDo Send token to your server, the following 2 need to get to process_payment.py on the webapp
                                Token paymentToken = token;
                                sendStripe(totalPrice, paymentToken);
                            }

                            public void onError(Exception error) {
                                Toast.makeText(getApplicationContext(), "Error occured processing payment", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        }

    }

    public LiveData<StripeInfo> sendStripe(Float price, Token payment) {

        StripeInfo stripe = new StripeInfo();
        stripe.totalPrice = price;
        stripe.paymentToken = payment;

        si = repository.sendStripe(stripe);
        if((si.getValue().paymentToken) == null)
        {
            Toast.makeText(getApplicationContext(), "Error processing payment", Toast.LENGTH_LONG);
        }
        return si;
    }

}
