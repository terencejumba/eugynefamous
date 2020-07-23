package quinton.terence.eugynefamous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class FinalPaymentDetails extends AppCompatActivity {

    TextView txtid, txtAmount, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_payment_details);


        txtid = findViewById(R.id.txtid);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);

        //get the intent

        Intent intent = getIntent();

        try {

            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));


            //creating a methood for getting the intents

            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        }
        catch (JSONException e){

            e.printStackTrace();

        }

    }

    private void showDetails(JSONObject response, String paymentAmount) {


        try {


            txtid.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText("Ksh" + paymentAmount);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}