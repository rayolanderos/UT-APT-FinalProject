package com.hopsquad.hopsquadapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;


public class PaymentActivity extends AppCompatActivity {

    CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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
            // ToDo
        }
        //cardToSave.setName("Customer Name");
        //cardToSave.setAddressZip("12345");
        cardToSave.validateNumber();
        cardToSave.validateCVC();
        if (!cardToSave.validateCard()){
            // ToDo if card not valid
        }
        Stripe stripe = new Stripe(this, "pk_test_6pRNASCoBOKtIshFeQd4XMUh"/*temporary key*/);
        stripe.createToken(
                cardToSave,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // ToDo Send token to your server
                    }
                    public void onError(Exception error) {
                        // ToDo Show localized error message
                    }
                }
        );

    }
}
