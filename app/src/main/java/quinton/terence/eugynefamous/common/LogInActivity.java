package quinton.terence.eugynefamous.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import quinton.terence.eugynefamous.AdminAddNewProductActivity;
import quinton.terence.eugynefamous.AdminCategoryActivity;
import quinton.terence.eugynefamous.HomeActivity;
import quinton.terence.eugynefamous.Model.users;
import quinton.terence.eugynefamous.R;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class LogInActivity extends AppCompatActivity {

    private EditText phone, password;
    private TextView iamAdmin, iamNotAdmin;

    private Button  loginBtn;

    private ProgressDialog loadingbar;

    private String ParentdatabName = "Users";
    private CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        phone = findViewById(R.id.login_phone);
        password = findViewById(R.id.login_password);

        iamAdmin = findViewById(R.id.iamAdmin);
        iamNotAdmin = findViewById(R.id.iamnotAdmin);

        loginBtn = findViewById(R.id.log_in);

        loadingbar = new ProgressDialog(this);

        chkBoxRememberMe = findViewById(R.id.remember_me_check);

        Paper.init(this);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser();

            }
        });

        iamAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginBtn.setText("LogIn Admin");
                iamAdmin.setVisibility(View.INVISIBLE);
                iamNotAdmin.setVisibility(View.VISIBLE);

                //setting the db name for the admin
                ParentdatabName = "Admins";


            }
        });

        iamNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginBtn.setText("LogIn");
                iamAdmin.setVisibility(View.VISIBLE);
                iamNotAdmin.setVisibility(View.INVISIBLE);
                ParentdatabName = "Users";


            }
        });

    }

    private void LoginUser() {

        //here is the paper for storing values to the paper library



        String repassword = password.getText().toString();
        String rephone = phone.getText().toString();

         if (TextUtils.isEmpty(repassword)){

            Toast.makeText(this,"password field empty",Toast.LENGTH_SHORT).show();
        }

       else if (TextUtils.isEmpty(rephone)){

            Toast.makeText(this,"phone field empty",Toast.LENGTH_SHORT).show();
        }

       else{

             loadingbar.setTitle("logging account");
             loadingbar.setMessage("wait we are checking credentials");
             loadingbar.setCanceledOnTouchOutside(false);
             loadingbar.show();

             AllowAccessToAccount(rephone, repassword);

         }

    }

    private void AllowAccessToAccount(final String rephone, final String repassword) {

        if (chkBoxRememberMe.isChecked()){

            //storing the variables in our prevalent class
            Paper.book().write(prevalent.UserPhoneKey, rephone);
            Paper.book().write(prevalent.UserPasswordKey, repassword);

        }

        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(ParentdatabName).child(rephone).exists()){


                    users usersData = snapshot.child(ParentdatabName).child(rephone).getValue(users.class);

                    if (usersData.getPhone().equals(rephone)){

                        if (usersData.getPassword().equals(repassword)){

                           if (ParentdatabName.equals("Admins")){


                               Toast.makeText(LogInActivity.this,"Welcome admin logged in successful...",Toast.LENGTH_SHORT).show();

                               loadingbar.dismiss();

                               Intent intent = new Intent(LogInActivity.this, AdminCategoryActivity.class);


                               startActivity(intent);


                           }
                           else if (ParentdatabName.equals("Users")){

                               Toast.makeText(LogInActivity.this,"logged in successful...",Toast.LENGTH_SHORT).show();

                               loadingbar.dismiss();

                               Intent intent = new Intent(LogInActivity.this, HomeActivity.class);

                               prevalent.currentOnlineUser = usersData;

                               startActivity(intent);

                           }



                        }
                        else {

                            Toast.makeText(LogInActivity.this,"password is incorrect",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();

                        }


                    }



                }

                else {

                    Toast.makeText(LogInActivity.this,"account with this" +rephone + "do not exist",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}