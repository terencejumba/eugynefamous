package quinton.terence.eugynefamous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, PhoneEditText, addressEditText , HomeEditText ;
    private Button confirmOrderBtn;

    //recieving the intent from the cart activity

    //

    private String totalAmount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        nameEditText = findViewById(R.id.shipment_name);
        PhoneEditText = findViewById(R.id.shipment_phone_number);
        addressEditText = findViewById(R.id.shipment_address);
        HomeEditText = findViewById(R.id.shipment_city);
        confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);

        //getting the intent

        totalAmount = getIntent().getStringExtra("Total Price");

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check();

            }
        });




    }

    private void check() {

        if (TextUtils.isEmpty(nameEditText.getText().toString())){

            Toast.makeText(this, "please provide your full name", Toast.LENGTH_SHORT).show();

        }

      else  if (TextUtils.isEmpty(PhoneEditText.getText().toString())){

            Toast.makeText(this, "please provide your phone", Toast.LENGTH_SHORT).show();

        }

       else  if (TextUtils.isEmpty(addressEditText.getText().toString())){

            Toast.makeText(this, "please provide your address name", Toast.LENGTH_SHORT).show();

        }
       else if (TextUtils.isEmpty(HomeEditText.getText().toString())){

            Toast.makeText(this, "please provide your city name", Toast.LENGTH_SHORT).show();

        }

       else
        {
            confirmOrder();

        }




    }

    private void confirmOrder() {

        Intent intent = new Intent(ConfirmFinalOrderActivity.this, PaymentDetailsActivity.class);

        intent.putExtra("name", nameEditText.getText().toString());
        intent.putExtra("phone", PhoneEditText.getText().toString());
        intent.putExtra("address", addressEditText.getText().toString());
        intent.putExtra("home", HomeEditText.getText().toString());
        intent.putExtra("price", totalAmount);

        startActivity(intent);

        finish();



    }
}