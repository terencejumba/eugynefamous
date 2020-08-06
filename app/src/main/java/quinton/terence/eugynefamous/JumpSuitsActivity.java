package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.ViewHolder.ProductViewHolder;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class JumpSuitsActivity extends AppCompatActivity {

    private RecyclerView jumpSuitsList;

    private ImageView backBtn, searchBtn;

    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump_suits);


        jumpSuitsList = findViewById(R.id.jumpsuitslist_);
        jumpSuitsList.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.jump_back);

        searchBtn = findViewById(R.id.jumpsuit_search);
        nextBtn = findViewById(R.id.next_jumps);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(JumpSuitsActivity.this, Page2Activity.class);

                intent.putExtra("sex", "women");
                intent.putExtra("category", "jumpsuits");

                startActivity(intent);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JumpSuitsActivity.super.onBackPressed();

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(JumpSuitsActivity.this, SearchProductsActivity.class);

                intent.putExtra("sex", "women");
                intent.putExtra("category", "jumpsuits");

                startActivity(intent);




            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference jumpsRef = FirebaseDatabase.getInstance().getReference().child("jumpsuits").child("women");

        FirebaseRecyclerOptions<products> options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(jumpsRef.orderByChild("priority").startAt("1").endAt("1"), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();


        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(JumpSuitsActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

                                startActivity(intent);


                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent, false);

                        ProductViewHolder holder = new ProductViewHolder(view);

                        return holder;


                    }
                };

        jumpSuitsList.setAdapter(adapter);
        adapter.startListening();


    }

}