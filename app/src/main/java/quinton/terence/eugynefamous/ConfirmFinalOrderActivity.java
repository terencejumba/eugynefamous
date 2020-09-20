package quinton.terence.eugynefamous;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        nameEditText = findViewById(R.id.shipment_name);
        PhoneEditText = findViewById(R.id.shipment_phone_number);
        addressEditText = findViewById(R.id.shipment_address);
        HomeEditText = findViewById(R.id.shipment_city);
        confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);



        //getting the intent

        totalAmount = getIntent().getStringExtra("Total Price");

        Toast.makeText(this, "Total Price:"+ " " + totalAmount + "Ksh", Toast.LENGTH_LONG).show();


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