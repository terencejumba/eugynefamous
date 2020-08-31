package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import quinton.terence.eugynefamous.Model.cart;
import quinton.terence.eugynefamous.ViewHolder.cartViewHolder;
import quinton.terence.eugynefamous.prevalent.prevalent;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView totalAmount, txtmMsg1;
    private ImageView backBtn;

    //for storing all the total price
    private int overTotalPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.cart_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn = findViewById(R.id.next_process_btn);
        totalAmount = findViewById(R.id.total_price);
        txtmMsg1 = findViewById(R.id.msg_1);

        backBtn = findViewById(R.id.cart_back);


        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);

                //String.valueOf(overTotalPrice) converts it to string data type

                intent.putExtra("Total Price", String.valueOf(overTotalPrice));

                totalAmount.setText( "Total Price : "  + String.valueOf(overTotalPrice));


                startActivity(intent);

                finish();


            }
        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CartActivity.super.onBackPressed();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        totalAmount.setVisibility(View.VISIBLE);




        checkOrderState();


        //creating a database refernce
        final DatabaseReference cartlistrefer = FirebaseDatabase.getInstance().getReference().child("Cart List");

        //setting up the recycler view
        FirebaseRecyclerOptions<cart> options =
                new FirebaseRecyclerOptions.Builder<cart>()
                        .setQuery(cartlistrefer.child("User View")
                                .child(prevalent.currentOnlineUser.getPhone())
                                .child("Products"), cart.class)
                        .build();


        FirebaseRecyclerAdapter<cart, cartViewHolder> adapter = new FirebaseRecyclerAdapter<cart, cartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull cartViewHolder holder, int i, @NonNull final cart model) {

                //setting the texts to our recycler view items
                holder.txtProductQuantity.setText("Quantity:" + model.getQuantity());
                holder.txtProductPrice.setText("Price" + model.getPrice() + "Ksh");
                holder.txtProductSize.setText("Size" + model.getSize());
                holder.txtProductName.setText("Name" + model.getPname());
                holder.txtProductColor.setText("Color" + model.getcolor());


                //getting individual total price by calculating price and quantity and later adding it to the overall total price
                int oneTypeProductTPrice  = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());

                //adding this int to our overall total price
                overTotalPrice = overTotalPrice + oneTypeProductTPrice;





                //setting an onClick listener
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //creating a dialog box
                        CharSequence[] options = new CharSequence[]
                                {

                                        "Edit",

                                        "Remove"

                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);

                        builder.setTitle("Cart options");

                        //setting a click listener on the twoo buttons
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (which == 0) {

                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);

                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("category", model.getCategory());
                                    intent.putExtra("sex", model.getSex());

                                    startActivity(intent);


                                } else if (which == 1) {

                                    cartlistrefer.child("User View")
                                            .child(prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        cartlistrefer.child("Admin View")
                                                                .child(prevalent.currentOnlineUser.getPhone())
                                                                .child("Products")
                                                                .child(model.getPid())
                                                                .removeValue();

                                                        Toast.makeText(CartActivity.this, "item removed", Toast.LENGTH_SHORT).show();


                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);


                                                        startActivity(intent);

                                                    }


                                                }
                                            });

                                }


                            }
                        });

                        builder.show();


                    }
                });


            }

            @NonNull
            @Override
            public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);

                cartViewHolder holder = new cartViewHolder(view);
                return holder;

            }
        };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
        recyclerView.setVisibility(View.VISIBLE);


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

                    String username = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")){


                        totalAmount.setText("dear" + username +   "your order has been placed" );
                        recyclerView.setVisibility(View.GONE);

                        txtmMsg1.setVisibility(View.VISIBLE);

                        txtmMsg1.setText("congrats your final order has been shipped successfully soon you will recieve your order at your doorstep");

                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products once you receive your final order", Toast.LENGTH_SHORT).show();


                    }
                    else if (shippingState.equals("not shipped")){

                        totalAmount.setText("shipping state = not shipped" );
                        recyclerView.setVisibility(View.GONE);

                        txtmMsg1.setVisibility(View.VISIBLE);

                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products once you receive your final order", Toast.LENGTH_SHORT).show();



                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}