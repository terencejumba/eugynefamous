package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import quinton.terence.eugynefamous.common.LogInActivity;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";

    private Button verifyBtn;
    private TextView pageTitle, questionTitle;
    private EditText phoneNumber, question1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        verifyBtn = findViewById(R.id.buton_reset);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_one);
        pageTitle = findViewById(R.id.reset_pass);
        questionTitle = findViewById(R.id.title_question);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings")) {


            pageTitle.setText("set Questions");

            verifyBtn.setText("set");

            displayPreviousAnswers();

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    setAnswers();


                }
            });

        } else if (check.equals("login")) {


            phoneNumber.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    verifyUser();

                }
            });


        }

    }


    private void setAnswers() {

        String answer1 = question1.getText().toString().toLowerCase();

        if (answer1.equals("")) {

            Toast.makeText(ResetPasswordActivity.this, "please answer the question", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(prevalent.currentOnlineUser.getPhone()).child("SecurityQuestion");

            HashMap<String, Object> map = new HashMap<>();
            map.put("answer1", answer1);

            ref.updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(ResetPasswordActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(ResetPasswordActivity.this, HomeActivity.class));
                                finish();

                            }

                        }
                    });


        }

    }


    private void displayPreviousAnswers() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(prevalent.currentOnlineUser.getPhone()).child("SecurityQuestion");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String answer1 = snapshot.child("answer1").getValue().toString();

                    question1.setText(answer1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void verifyUser() {

        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("")){


            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        String mPhone = snapshot.child("phone").getValue().toString();

                        if (snapshot.hasChild("SecurityQuestion")) {



                            String ans1 = snapshot.child("SecurityQuestion").child("answer1").getValue().toString();

                            if (!ans1.equals(answer1)) {

                                Toast.makeText(ResetPasswordActivity.this, "your answer is incorrect", Toast.LENGTH_SHORT).show();

                            } else {

                                //creating a dialog box
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("enter new password");

                                builder.setView(newPassword);

                                builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (!newPassword.getText().toString().equals("")) {

                                            ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                Toast.makeText(ResetPasswordActivity.this, "password changed successfully", Toast.LENGTH_SHORT).show();

                                                                startActivity(new Intent(ResetPasswordActivity.this, LogInActivity.class));

                                                                finish();

                                                            }

                                                        }
                                                    });

                                        }


                                    }
                                });

                                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();

                                    }
                                });

                                builder.show();

                            }


                        } else {

                            Toast.makeText(ResetPasswordActivity.this, "you have not set the security question ", Toast.LENGTH_SHORT).show();

                        }

                    }
                    else {

                        Toast.makeText(ResetPasswordActivity.this, "phone number incorrect", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else {

            Toast.makeText(this, "complete the form", Toast.LENGTH_SHORT).show();

        }



    }

}