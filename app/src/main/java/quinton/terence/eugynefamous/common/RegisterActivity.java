package quinton.terence.eugynefamous.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import quinton.terence.eugynefamous.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText phoneNumber, userName, name, password, email;
    private Button register;

    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //hooks
        phoneNumber = findViewById(R.id.relogin_phone_number);
        userName = findViewById(R.id.relogin_username);
        email = findViewById(R.id.relogin_email);
        name = findViewById(R.id.relogin_name);
        password = findViewById(R.id.reglogin_password);


        register = findViewById(R.id.registerr_btn);

        loadingbar = new ProgressDialog(this);

        //setting an onclick listener
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Register();

            }
        });


    }

    private void Register() {


        //getting inputs
        String rename = name.getText().toString();
        String repassword = password.getText().toString();
        String reemail = email.getText().toString();
        String reusername = userName.getText().toString();
        String rephone = phoneNumber.getText().toString();

        //checking if its empty
        if (TextUtils.isEmpty(rename)){

            Toast.makeText(this,"name field empty",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(reemail)){

            Toast.makeText(this,"email field empty",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(reusername)){

            Toast.makeText(this,"username field empty",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(repassword)){

            Toast.makeText(this,"password field empty",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(rephone)){

            Toast.makeText(this,"phone field empty",Toast.LENGTH_SHORT).show();
        }
        else {

            loadingbar.setTitle("create account");
            loadingbar.setMessage("wait we are checking credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidatePhonenumber(rename, rephone,reusername, reemail, repassword);

        }


    }

    private void ValidatePhonenumber(final String rename, final String rephone, final String reusername, final String reemail, final String repassword) {


        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Users").child(rephone).exists())){


                    HashMap<String, Object> userdataMap = new HashMap<>();

                    userdataMap.put("phone", rephone);
                    userdataMap.put("username", reusername);
                    userdataMap.put("password", repassword);
                    userdataMap.put("email", reemail);
                    userdataMap.put("name", rename);

                    RootRef.child("Users").child(rephone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){


                                        Toast.makeText(RegisterActivity.this, "success", Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();

                                        startActivity(new Intent(RegisterActivity.this, LogInActivity.class));

                                    }

                                    else {
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "network error", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
                else {

                    Toast.makeText(RegisterActivity.this, "phone number exists" + rephone, Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                    startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}