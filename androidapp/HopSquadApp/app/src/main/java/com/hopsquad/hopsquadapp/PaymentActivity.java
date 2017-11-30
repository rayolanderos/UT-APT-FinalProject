package com.hopsquad.hopsquadapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Token;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;


public class PaymentActivity extends AppCompatActivity {

    CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
    private EditText mZipcode = (EditText) findViewById(R.id.zip_input);
    private EditText mName = (EditText) findViewById(R.id.name_input);


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
            // ToDo Input empty
        }
        cardToSave.setName(mName.getText().toString());
        cardToSave.setAddressZip(mZipcode.getText().toString());
        cardToSave.validateNumber();
        cardToSave.validateCVC();
        if (!cardToSave.validateCard()){
            // ToDo if card not valid
        }
        Stripe stripe = new Stripe(this, "pk_test_6pRNASCoBOKtIshFeQd4XMUh"/*temporary key, need to get actual one*/);
        stripe.createToken(
                cardToSave,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // ToDo Send token to your server, need to grab the payment info too to send
                    }
                    public void onError(Exception error) {
                        // ToDo Show localized error message
                    }
                }
        );

    }
}
