package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import quinton.terence.eugynefamous.Model.cart;
import quinton.terence.eugynefamous.ViewHolder.cartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    private TextView products;
    private RecyclerView OrderProductsList;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference cartListRefe;

    private String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        OrderProductsList = findViewById(R.id.orders_Products_list);
        layoutManager = new LinearLayoutManager(this);
        OrderProductsList.setLayoutManager(layoutManager);

        userID = getIntent().getStringExtra("uid");

        cartListRefe = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("Admin View").child(userID).child("Products");




    }


    @Override
    protected void onStart() {
        super.onStart();

        //for setting the recycler view
        FirebaseRecyclerOptions<cart> options = new
                FirebaseRecyclerOptions.Builder<cart>()
                .setQuery(cartListRefe, cart.class)
                .build();


        FirebaseRecyclerAdapter<cart, cartViewHolder> adapter  = new
                FirebaseRecyclerAdapter<cart, cartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull cartViewHolder holder, int position, @NonNull cart model) {


                        //setting the texts to our recycler view items
                        holder.txtProductQuantity.setText("Quantity:" + model.getQuantity());
                        holder.txtProductPrice.setText("Price" + model.getPrice() + "Ksh");
                        holder.txtProductSize.setText("Size" + model.getSize());
                        holder.txtProductName.setText("Name" + model.getPname());
                        holder.txtProductColor.setText("Color" + model.getcolor());

                    }

                    @NonNull
                    @Override
                    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);

                        cartViewHolder holder = new cartViewHolder(view);
                        return holder;


                    }
                };


        OrderProductsList.setAdapter(adapter);
        adapter.startListening();

    }
}


