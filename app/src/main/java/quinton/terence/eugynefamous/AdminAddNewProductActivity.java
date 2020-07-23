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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import quinton.terence.eugynefamous.common.LogInActivity;

public class AdminAddNewProductActivity extends AppCompatActivity {

    //for getting intents
    private String  CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button addNewProductBtn;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDesccription, InputProductPrice;

    private ProgressDialog loadingbar;

    //variable for the gallery intent
    private static final int GalleryPick = 1 ;

    //variable for getting image uri
    private Uri ImageUri;

    //variable for random key
    private String productRandomKey, downloadImageUrl;

    //variable for our storage database
    private StorageReference ProductImagesref;

    //variable for our Database
    private DatabaseReference Productsref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        //getting intent
       CategoryName = getIntent().getExtras().get("category").toString();

       //hooks for our storage reference
        ProductImagesref = FirebaseStorage.getInstance().getReference().child("Product Images");

        //hooks for our database reference
        Productsref = FirebaseDatabase.getInstance().getReference().child("Products");

       //hooks
        addNewProductBtn = findViewById(R.id.add_new_product);
        InputProductName = findViewById(R.id.product_name);
        InputProductDesccription = findViewById(R.id.product_description);
        InputProductPrice = findViewById(R.id.product_price);
        InputProductImage = findViewById(R.id.select_product_image);

        loadingbar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenGallery();

            }
        });

        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateProductData();

            }
        });

    }




    //function for opening gallery after user clicks on the image
    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);

    }

    //getting results after picking an imag egetting the image URi


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick  && resultCode == RESULT_OK  && data != null){

            //getting the image uri
            ImageUri = data.getData();

            //displaying the image uri in our image view
            InputProductImage.setImageURI(ImageUri);


        }


    }


    private void ValidateProductData() {

        //getting the user inputs
        Description = InputProductDesccription.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();


        //verifying if not null
        if (ImageUri == null){

            Toast.makeText(AdminAddNewProductActivity.this, "product image required", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(Description)){

            Toast.makeText(AdminAddNewProductActivity.this, "product description required", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(Price)){

            Toast.makeText(AdminAddNewProductActivity.this, "product price required", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(Pname)){

            Toast.makeText(AdminAddNewProductActivity.this, "product name required", Toast.LENGTH_SHORT).show();

        }

        else{

            //method for storing image info
            StoreProductInformation();

        }

    }

    private void StoreProductInformation() {


        loadingbar.setTitle("Add New Product");
        loadingbar.setMessage("Please wait while  we are adding the new product");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        //storing the product info to the storage using the current date and time
        Calendar calendar = Calendar.getInstance();

        //getting current date
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        //getting current time
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
         saveCurrentTime = currentTime.format(calendar.getTime());

         //creating a custom random key
        productRandomKey = saveCurrentDate + saveCurrentTime;




        //storing the image into our firebase storage
        //getLastPathSegment() this gets the image name

        final StorageReference filePath = ProductImagesref.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        //for uploading images to the database
        final UploadTask uploadTask = filePath.putFile(ImageUri);


        //checking if it was added success fully
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //getting the error message
                String message = e.toString();

                Toast.makeText(AdminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();

                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewProductActivity.this, "Product Image uploaded successfuly", Toast.LENGTH_SHORT).show();

                //getting the image uri so as to add it to the database
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){

                            //throwing the exception
                            throw task.getException();



                        }

                        //getting the image uri fro the firebase storage to the database
                        downloadImageUrl = filePath.getDownloadUrl().toString();

                        return filePath.getDownloadUrl();



                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){

                            //getting the image link now for it to go to the database
                            downloadImageUrl = task.getResult().toString();


                            Toast.makeText(AdminAddNewProductActivity.this, " Getting Product Image url  Successfuly..", Toast.LENGTH_SHORT).show();


                            //method for saving product info to database
                            saveProductInfoToDatabase();

                        }


                    }
                });

            }
        });


    }

    private void saveProductInfoToDatabase() {

        //creating  a hash map for storing multiple entries

        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid", productRandomKey );
        productMap.put("date", saveCurrentDate );
        productMap.put("time", saveCurrentTime );
        productMap.put("description", Description );
        productMap.put("image", downloadImageUrl );
        productMap.put("Category", CategoryName );
        productMap.put("price", Price );
        productMap.put("pname", Pname );

       Productsref.child(productRandomKey).updateChildren(productMap)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                       if (task.isSuccessful()){

                           Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);


                           startActivity(intent);

                           loadingbar.dismiss();

                           Toast.makeText(AdminAddNewProductActivity.this, " product is added successfully..", Toast.LENGTH_SHORT).show();

                       }

                       else {

                           loadingbar.dismiss();

                           //getting the error message
                           String message = task.getException().toString();

                           Toast.makeText(AdminAddNewProductActivity.this, "Error" +message , Toast.LENGTH_SHORT).show();

                       }


                   }
               });
    }
}