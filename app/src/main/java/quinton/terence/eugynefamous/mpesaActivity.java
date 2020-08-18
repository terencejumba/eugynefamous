package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import quinton.terence.eugynefamous.Model.AccessToken;
import quinton.terence.eugynefamous.Model.STKPush;
import quinton.terence.eugynefamous.prevalent.prevalent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static quinton.terence.eugynefamous.AppConstants.BUSINESS_SHORT_CODE;
import static quinton.terence.eugynefamous.AppConstants.CALLBACKURL;
import static quinton.terence.eugynefamous.AppConstants.PARTYB;
import static quinton.terence.eugynefamous.AppConstants.PASSKEY;
import static quinton.terence.eugynefamous.AppConstants.TRANSACTION_TYPE;

public class mpesaActivity extends AppCompatActivity implements View.OnClickListener {

    private ApiClient mApiClient;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.mpesa_amount)
    EditText mAmount;
    @BindView(R.id.mpesa_phone)
    EditText mPhone;
    @BindView(R.id.mpesa_pay)
    Button mPay;
    @BindView(R.id.mpesa_done)
    Button Done;

    //for getting intents
    String phone, address, home, TotalAmount, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();


        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        mPay.setOnClickListener(this);

        getAccessToken(); // Request for an access token from the MPESA API.

        phone  = getIntent().getStringExtra("phone");
       address  = getIntent().getStringExtra("address");
        home  = getIntent().getStringExtra("home");
       TotalAmount  = getIntent().getStringExtra("price");
        name  = getIntent().getStringExtra("name");

        Done.setVisibility(View.GONE);


        Done.setOnClickListener(this);


    }


    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);


                    //  Toast.makeText(mpesaActivity.this, "access is" +response.body().accessToken, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    // Initiate payment after a user has entered their name and the amount they wish to pay.
    // The amount can also be passed in from the price of a product for example, if you were integrating an online shop.
    @Override
    public void onClick(View view) {

        String phone_number, amount;

        if (view == mPay) {
             phone_number = mPhone.getText().toString();
            amount = mAmount.getText().toString();
            performSTKPush(phone_number, amount);


        } else if (view == Done) {

            phone_number = mPhone.getText().toString();
           // amount = mAmount.getText().toString();

            final String saveCurrentDate, saveCurrentTime;

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());


            //creating a database reference

            final DatabaseReference ordersRefer = FirebaseDatabase.getInstance().getReference().child("Orders")
                    .child(prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> ordersMap = new HashMap<>();

            ordersMap.put("name", name);
            ordersMap.put("totalAmount", TotalAmount);
            ordersMap.put("phone", phone);
            ordersMap.put("mpesaPhone", phone_number);
            ordersMap.put("address", address);
            ordersMap.put("home", home);
            ordersMap.put("method", "mpesa");
            ordersMap.put("state", "not shipped");
            ordersMap.put("date", saveCurrentDate);
            ordersMap.put("time", saveCurrentTime);

            ordersRefer.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                                .child(prevalent.currentOnlineUser.getPhone()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            Toast.makeText(mpesaActivity.this, "product placed successfully", Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                });

                    }

                }
            });


            startActivity(new Intent(mpesaActivity.this, HomeActivity.class));

            finish();


        }


    }

    //These are the same details that we were setting when we introduced ourselves to the MPesa Daraja API.
    public void performSTKPush(String phone_number, String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "test bin", //The account reference
                "tutorial"  //The transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());


                        Done.setVisibility(View.VISIBLE);

                        mPay.setVisibility(View.GONE);



                    } else {
                        Timber.e("Response %s", response.errorBody().string());




                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);



            }


        });


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}