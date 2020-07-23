package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class ProductDetailsActivity extends AppCompatActivity {


    private ImageView productImage;
    private Button addToCartBtn;
    private ElegantNumberButton numberButton;
    private TextView price, productDescription, productName;
    private EditText color, size;


    //for getting the intent
    private String productID = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //hooks

        productImage = findViewById(R.id.product_image_details);
        price = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_desc_details);
        productName = findViewById(R.id.product_name_details);
        numberButton = findViewById(R.id.number_btn);
        color = findViewById(R.id.product_details_color);
        size = findViewById(R.id.product_details_size);
        addToCartBtn = findViewById(R.id.add_to_cart_pd);


        //getting the intent to displayy products in this activity
        productID =getIntent().getStringExtra("pid");

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addingToCartList();


            }
        });


        //retrieving the specific product details using the productID

        getProductDetails(productID);





    }

    private void addingToCartList() {

//validating the fields
        if (TextUtils.isEmpty(color.getText().toString())){

            Toast.makeText(ProductDetailsActivity.this, "color is required", Toast.LENGTH_SHORT).show();

        }

        else if (TextUtils.isEmpty(size.getText().toString())){

            Toast.makeText(ProductDetailsActivity.this, "size is required", Toast.LENGTH_SHORT).show();

        }

        else {

           savingCartInfo();


        }

    }

    private void savingCartInfo() {

        //variables for time
        String saveCurrentTime, saveCurrentDate;


        //getting the time the user added to cart
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        //creating a database reference
        final DatabaseReference cartListRefer = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> carteMap = new HashMap<>();
        carteMap.put("pid", productID);
        carteMap.put("pname", productName.getText().toString());
        carteMap.put("price", price.getText().toString());
        carteMap.put("size", size.getText().toString());
        carteMap.put("color", color.getText().toString());
        carteMap.put("date", saveCurrentDate);
        carteMap.put("time", saveCurrentTime);
        carteMap.put("quantity", numberButton.getNumber());
        carteMap.put("discount", "");

        cartListRefer.child("User View").child(prevalent.currentOnlineUser.getPhone()).child("Products").child(productID)
                .updateChildren(carteMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            cartListRefer.child("Admin View").child(prevalent.currentOnlineUser.getPhone()).child("Products").child(productID)
                                    .updateChildren(carteMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(ProductDetailsActivity.this, "added to cart list", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);

                                                startActivity(intent);


                                            }

                                        }
                                    });


                        }

                    }
                });


    }

    private void getProductDetails(String productID) {

        //creating database refernce for the product node
        DatabaseReference productsRefer = FirebaseDatabase.getInstance().getReference().child("Products");

        //now we are searching for the specific child in our products node
        productsRefer.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    //using the products class to get the values
                    products Product = snapshot.getValue(products.class);

                    productName.setText(Product.getPname());
                    price.setText(Product.getPrice());
                    productDescription.setText(Product.getDescription());

                    Picasso.get().load(Product.getImage()).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}