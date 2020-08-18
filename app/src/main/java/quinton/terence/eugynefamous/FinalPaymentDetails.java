package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import quinton.terence.eugynefamous.prevalent.prevalent;

public class FinalPaymentDetails extends AppCompatActivity {

    TextView txtid, txtAmount, txtStatus;
    Button done;

    String name = "";
    String phone = "";
    String address = "";
    String home = "";
    String PaymentDetails = "";
    String PaymentAmount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_payment_details);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();


        txtid = findViewById(R.id.txtid);
        txtAmount = findViewById(R.id.txtAmount);
        txtStatus = findViewById(R.id.txtStatus);
        done = findViewById(R.id.done_btn);


        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        address = getIntent().getStringExtra("address");
        home = getIntent().getStringExtra("home");

        //get the intent

        //Intent intent = getIntent();
      PaymentDetails = getIntent().getStringExtra("PaymentDetails");
      PaymentAmount = getIntent().getStringExtra("PaymentAmount");


      done.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent = new Intent(FinalPaymentDetails.this , HomeActivity.class);

              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent. FLAG_ACTIVITY_CLEAR_TASK );
              startActivity(intent);
              finish();

          }
      });


        try {

           // JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));

            JSONObject jsonObject = new JSONObject(PaymentDetails);

            //creating a methood for getting the intents

            showDetails(jsonObject.getJSONObject("response"), PaymentAmount);

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


            final  String saveCurrentDate, saveCurrentTime;

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());


            //creating a database reference

            final  DatabaseReference ordersRefer = FirebaseDatabase.getInstance().getReference().child("Orders")
                    .child(prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> ordersMap = new HashMap<>();

            ordersMap.put("name", name);
            ordersMap.put("totalAmount", PaymentAmount);
            ordersMap.put("phone", phone);
            ordersMap.put("address", address);
            ordersMap.put("home", home);
            ordersMap.put("payState",response.getString("state"));
            ordersMap.put("payid", response.getString("id"));
            ordersMap.put("state", "not shipped");
            ordersMap.put("date", saveCurrentDate);
            ordersMap.put("time", saveCurrentTime);

            ordersRefer.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                                .child(prevalent.currentOnlineUser.getPhone()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            Toast.makeText(FinalPaymentDetails.this, "product placed successfully", Toast.LENGTH_SHORT).show();





                                        }

                                    }
                                });

                    }

                }
            });




        } catch (JSONException e) {
            e.printStackTrace();
        }





    }
}