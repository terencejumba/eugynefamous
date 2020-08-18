package quinton.terence.eugynefamous.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import quinton.terence.eugynefamous.R;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn, deleteBtn;

    private EditText price, name, description, priority;

    private ImageView imageView;

    private String prodID = "", sex = "", category = "";

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        applyChangesBtn = findViewById(R.id.maintain_apply);
        price = findViewById(R.id.maintain_price);
        name = findViewById(R.id.maintain_name);
        description = findViewById(R.id.maintain_description);
        priority = findViewById(R.id.maintain_priority);
        deleteBtn = findViewById(R.id.maintain_delete);

        imageView = findViewById(R.id.maintain_image);

        //getting the intent to displayy products in this activity
        prodID = getIntent().getStringExtra("pid");
        sex = getIntent().getStringExtra("sex");
        category = getIntent().getStringExtra("category");

        productRef = FirebaseDatabase.getInstance().getReference().child(category).child(sex).child(prodID);


        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                applyChanges();

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteThisProduct();

            }
        });

    }

    private void deleteThisProduct() {

        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(AdminMaintainProductsActivity.this, "product deleted successfully", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class));
                finish();

            }
        });

    }

    private void applyChanges() {

        //getting the name of the products from the edittext

        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pPrice = price.getText().toString();
        String pPriority = priority.getText().toString();


        if (pName.equals("")) {

            Toast.makeText(this, "product name required", Toast.LENGTH_SHORT).show();

        } else if (pDescription.equals("")) {

            Toast.makeText(this, "product description required", Toast.LENGTH_SHORT).show();

        } else if (pPrice.equals("")) {

            Toast.makeText(this, "product price required", Toast.LENGTH_SHORT).show();

        } else if (pPriority.equals("")) {

            Toast.makeText(this, "product priority required", Toast.LENGTH_SHORT).show();

        } else {


            //creating  a hash map for storing multiple entries

            HashMap<String, Object> productMap = new HashMap<>();

            productMap.put("pid", prodID);
            productMap.put("description", pDescription);
            productMap.put("priority", pPriority);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        Toast.makeText(AdminMaintainProductsActivity.this, "changes applied successfully", Toast.LENGTH_SHORT).show();

                        startActivity( new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class));

                        finish();

                    }


                }
            });


        }


    }

    private void displaySpecificProductInfo() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    //retrieving the info

                    String pName = snapshot.child("pname").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();
                    String pPriority = snapshot.child("priority").getValue().toString();


                    name.setText(pName);
                    description.setText(pDescription);
                    price.setText(pPrice);
                    priority.setText(pPriority);
                    Picasso.get().load(pImage).into(imageView);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}