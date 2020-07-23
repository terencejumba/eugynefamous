package quinton.terence.eugynefamous;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import quinton.terence.eugynefamous.paymentIntergrations.Configu;

public class PaymentDetailsActivity extends AppCompatActivity {

    //paypal intergration

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            //using sandbox because we are on test mode
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Configu.PAYPAL_CLIENT_ID);


    private TextView Paypal, mPesa, ChooseCurrency, Ksh, Usd;
    private Button confirmBtn;

    //changes depending on which currency was clicked
    private String currency = "Ksh choosed";

    //getting the intents from the confirm final order activity
    private String TotalAmount = "";
    private String name = "";
    private String phone = "";
    private String address = "";
    private String home = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);


        //getting the intents

        TotalAmount = getIntent().getStringExtra("price");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        home = getIntent().getStringExtra("home");


        Paypal = findViewById(R.id.paypal);
        mPesa = findViewById(R.id.mpesa);
        ChooseCurrency = findViewById(R.id.choose_currency);
        Ksh = findViewById(R.id.ksh);
        Usd = findViewById(R.id.usd);

        confirmBtn = findViewById(R.id.confirm_pay_btn);

        ChooseCurrency.setVisibility(View.GONE);
        Ksh.setVisibility(View.GONE);
        Usd.setVisibility(View.GONE);

        Paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChooseCurrency.setVisibility(View.VISIBLE);
                Ksh.setVisibility(View.VISIBLE);
                Usd.setVisibility(View.VISIBLE);


            }
        });


        Ksh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currency = "Ksh choosed";


                confirmBtn.setText("confirm pay (Ksh)");


            }
        });


        Usd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                currency = "usd choosed";


                confirmBtn.setText("confirm pay (Usd)");


            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currency.equals("usd choosed")) {

                    processPayinUsd();

                }


            }
        });


    }

    private void processPayinUsd() {


        //changing the total price which is in ksh to usd
        int oneTypeProductTPrice = ((Integer.valueOf(TotalAmount))) / 100;

        String convertedKshToDollar = String.valueOf(oneTypeProductTPrice);


        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(convertedKshToDollar)), "USD",
                "products payment", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null) {

                    try {

                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, FinalPaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", TotalAmount)  //sending the total amount which has not yet been converted to usd
                        );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (resultCode == Activity.RESULT_CANCELED) {

                    Toast.makeText(this, "canelled", Toast.LENGTH_SHORT).show();

                }

            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {

                Toast.makeText(this, "invalid", Toast.LENGTH_SHORT).show();

            }

        }

    }


}
