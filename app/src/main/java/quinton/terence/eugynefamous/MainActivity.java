package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import quinton.terence.eugynefamous.Model.users;
import quinton.terence.eugynefamous.common.LogInActivity;
import quinton.terence.eugynefamous.common.RegisterActivity;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class MainActivity extends AppCompatActivity {

    private Button login, register;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.main_register);

        loadingbar = new ProgressDialog(this);

        //initializing the paper lib
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, LogInActivity.class));

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, RegisterActivity.class));

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, LogInActivity.class));

            }
        });


        //getting the user keys
        String UserPhoneKey = Paper.book().read(prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(prevalent.UserPasswordKey);


        if (UserPhoneKey != ""  && UserPasswordKey != ""){

            if (!TextUtils.isEmpty(UserPasswordKey) &&  !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey, UserPasswordKey);

                loadingbar.setTitle("Already logged in");
                loadingbar.setMessage("please wait....");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

            }

        }



    }

    private void AllowAccess(final String rephone, final String repassword) {

        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(rephone).exists()){


                    users usersData = snapshot.child("Users").child(rephone).getValue(users.class);

                    if (usersData.getPhone().equals(rephone)){

                        if (usersData.getPassword().equals(repassword)){

                            Toast.makeText(MainActivity.this,"logged in successful...",Toast.LENGTH_SHORT).show();

                            loadingbar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                            //this makes it to know the user
                            prevalent.currentOnlineUser = usersData;

                            startActivity(intent);



                        }
                        else {

                            Toast.makeText(MainActivity.this,"password is incorrect",Toast.LENGTH_SHORT).show();

                        }


                    }



                }

                else {

                    Toast.makeText(MainActivity.this,"account with this" +rephone + "do not exist",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}