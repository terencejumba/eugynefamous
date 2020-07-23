package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userNameEditText, userPhoneEditText, addressEditText;
    private TextView profileChangeTextBtn, saveTextButton;
    private ImageView backBtn;

    //for images
    private Uri imageUri;
    private String myurl = "";
    private StorageTask uploadtask;

    //storage reference for our user profile images
    private StorageReference storageprofilepicturerefe;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //hooks
        profileImageView = findViewById(R.id.profile_image);
        fullNameEditText = findViewById(R.id.settings_full_name);
        userNameEditText = findViewById(R.id.settings_user_name);
        userPhoneEditText = findViewById(R.id.settings_phone_number);
        addressEditText = findViewById(R.id.settings_address);
        profileChangeTextBtn = findViewById(R.id.profile_image_change);
        saveTextButton = findViewById(R.id.update_account_settings);
        backBtn = findViewById(R.id.close_settings);

        //setting visibility
        profileChangeTextBtn.setVisibility(View.GONE);
        profileImageView.setVisibility(View.GONE);


        //hooks for our storage reference
        storageprofilepicturerefe = FirebaseStorage.getInstance().getReference().child("Profile pictures");



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validating
                if (checker.equals("checked")) {

                    //this method will save the whole info of the user
                    //here the user updated the image
                    userInfoSaved();

                } else {
                    //this only updates the changed info here the user did not update the image
                    updateOnlyUserInfo();
                }


            }
        });


        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //assigning a value to our checker variable
                checker = "clicked";

                //for cropping image
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);


            }
        });


        //creating a method
        userInfoDisplay(profileImageView, fullNameEditText, userNameEditText, userPhoneEditText, addressEditText);

    }

    private void updateOnlyUserInfo() {

        DatabaseReference REF = FirebaseDatabase.getInstance().getReference().child("Users");

        //using the hashmap to store all our user data
        HashMap<String, Object> userinfomap = new HashMap<>();
        userinfomap.put("name", fullNameEditText.getText().toString());
        userinfomap.put("username", userNameEditText.getText().toString());
        userinfomap.put("phoneOrder", userPhoneEditText.getText().toString());
        userinfomap.put("address", addressEditText.getText().toString());


        REF.child(prevalent.currentOnlineUser.getPhone()).updateChildren(userinfomap);



        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));

        Toast.makeText(SettingsActivity.this, "profile info updated successfully", Toast.LENGTH_SHORT).show();

        finish();

    }


    //getting the result after one has chose and cropped an image


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


      if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode == RESULT_OK && data != null ) {

          //getting the crop image activity result

          CropImage.ActivityResult result = CropImage.getActivityResult(data);

          //storing the result in our image uri

          imageUri = result.getUri();

          //displaying the image

          profileImageView.setImageURI(imageUri);



      }

      else {

          Toast.makeText(SettingsActivity.this, "error try again", Toast.LENGTH_SHORT).show();

          startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));

          finish();


      }

    }




    private void userInfoSaved() {


        //validating the fields
        if (TextUtils.isEmpty(fullNameEditText.getText().toString())){

            Toast.makeText(SettingsActivity.this, "name is required", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(userNameEditText.getText().toString())){

            Toast.makeText(SettingsActivity.this, "username is required", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString())){

            Toast.makeText(SettingsActivity.this, "name is required", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString())){

            Toast.makeText(SettingsActivity.this, "address is required", Toast.LENGTH_SHORT).show();

        }
        else if (checker.equals("clicked")){

            //storing the  image info to the database

            uploadImage();


        }


    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("update profile");
        progressDialog.setMessage("please wait , while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null){

            //storing the image
            final StorageReference filerefer = storageprofilepicturerefe
                    .child(prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadtask = filerefer.putFile(imageUri);

            //getting the result of the upload task
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()){

                        throw task.getException();

                    }


                    return filerefer.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()){

                        Uri downloaduRl = task.getResult();

                        myurl = downloaduRl.toString();

                        //storing it in that specific users node in the database

                        DatabaseReference REF = FirebaseDatabase.getInstance().getReference().child("Users");

                        //using the hashmap to store all our user data
                        HashMap<String, Object> userinfomap = new HashMap<>();
                        userinfomap.put("name", fullNameEditText.getText().toString());
                        userinfomap.put("username", userNameEditText.getText().toString());
                        userinfomap.put("phoneOrder", userPhoneEditText.getText().toString());
                        userinfomap.put("address", addressEditText.getText().toString());
                        userinfomap.put("image", myurl);

                        REF.child(prevalent.currentOnlineUser.getPhone()).updateChildren(userinfomap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));

                        Toast.makeText(SettingsActivity.this, "profile info updated successfully", Toast.LENGTH_SHORT).show();

                        finish();

                    }

                    else {

                        progressDialog.dismiss();

                        Toast.makeText(SettingsActivity.this, "error updating info", Toast.LENGTH_SHORT).show();

                    }

                }
            });


        }

        else {

            Toast.makeText(SettingsActivity.this, "image not selected", Toast.LENGTH_SHORT).show();
        }


    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userNameEditText, final EditText userPhoneEditText, final EditText addressEditText) {

        //creating a database reference

        DatabaseReference usersrefe = FirebaseDatabase.getInstance().getReference().child("Users").child(prevalent.currentOnlineUser.getPhone());

        usersrefe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("image").exists()) {

                    //getting the user inputs
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();
                    String phone = snapshot.child("phone").getValue().toString();
                    String address = snapshot.child("address").getValue().toString();

                    //displaying the image
                    Picasso.get().load(image).into(profileImageView);

                    fullNameEditText.setText(name);
                    userNameEditText.setText(username);
                    userPhoneEditText.setText(phone);
                    addressEditText.setText(address);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}