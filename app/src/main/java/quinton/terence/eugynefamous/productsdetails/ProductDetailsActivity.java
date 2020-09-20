package quinton.terence.eugynefamous.productsdetails;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import quinton.terence.eugynefamous.HomeActivity;
import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.R;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class ProductDetailsActivity extends AppCompatActivity {


    private ImageView productImage;
    private Button addToCartBtn;
    private ElegantNumberButton numberButton;
    private TextView price, productDescription, productName;
    private EditText color, size;

    private ImageView backBtn;


    //for getting the intent
    private String prodID = "", state = "normal", sex = "", category = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();


        //hooks

        productImage = findViewById(R.id.product_image_details);
        price = findViewById(R.id.product_price_details);
        productDescription = findViewById(R.id.product_desc_details);
        productName = findViewById(R.id.product_name_details);
        numberButton = findViewById(R.id.number_btn);
        color = findViewById(R.id.product_details_color);
        size = findViewById(R.id.product_details_size);
        addToCartBtn = findViewById(R.id.add_to_cart_pd);
        backBtn = findViewById(R.id.product_back);


        //getting the intent to displayy products in this activity
        prodID =getIntent().getStringExtra("pid");
        sex = getIntent().getStringExtra("sex");
        category = getIntent().getStringExtra("category");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductDetailsActivity.super.onBackPressed();

            }
        });


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {







                if (state.equals("Order placed")  || state.equals("Order shipped")  ){


                    Toast.makeText(ProductDetailsActivity.this, "you can purchase more products once your order has been shiped or confirmed", Toast.LENGTH_LONG).show();

                }
                else {

                    addingToCartList(); 

                }


            }
        });


        //retrieving the specific product details using the productID

        getProductDetails();





    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();

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
        carteMap.put("pid", prodID);
        carteMap.put("pname", productName.getText().toString());
        carteMap.put("price", price.getText().toString());
        carteMap.put("size", size.getText().toString());
        carteMap.put("color", color.getText().toString());
        carteMap.put("date", saveCurrentDate);
        carteMap.put("time", saveCurrentTime);
        carteMap.put("quantity", numberButton.getNumber());
        carteMap.put("discount", "");
        carteMap.put("category", category);
        carteMap.put("sex", sex);

        cartListRefer.child("User View").child(prevalent.currentOnlineUser.getPhone()).child("Products").child(prodID)
                .updateChildren(carteMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            cartListRefer.child("Admin View").child(prevalent.currentOnlineUser.getPhone()).child("Products").child(prodID)
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

    private void getProductDetails() {

        //creating database refernce for the product node
        DatabaseReference shatiRefer = FirebaseDatabase.getInstance().getReference().child(category).child(sex);

        //now we are searching for the specific child in our node
        shatiRefer.child(prodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    //using the products class to get the values
                    products Product = snapshot.getValue(products.class);

                    productName.setText(Product.getPname());
                    price.setText(Product.getPrice());
                    productDescription.setText(Product.getDescription());

                    Picasso.get().load(Product.getImage()).placeholder(R.drawable.progressload).into(productImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void  checkOrderState(){

        DatabaseReference ordersrefer;

        ordersrefer = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevalent.currentOnlineUser.getPhone());

        ordersrefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot .exists()){


                    //getting values from our orders child node
                    String shippingState = snapshot.child("state").getValue().toString();


                    if (shippingState.equals("shipped")){


                        state = "Order shipped";




                    }
                    else if (shippingState.equals("not shipped")){

                        state = "Order placed";




                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}