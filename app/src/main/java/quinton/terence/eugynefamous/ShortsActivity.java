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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.ViewHolder.ProductViewHolder;
import quinton.terence.eugynefamous.ViewHolder.StartProductViewHolder;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class ShortsActivity extends AppCompatActivity {

    private RecyclerView shortsList, womenshortsList;

    private TextView showMore, womenShowMore;

    private ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shorts);

        shortsList = findViewById(R.id.shortslist);
        shortsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        womenshortsList = findViewById(R.id.women_shortslist);
        womenshortsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        showMore = findViewById(R.id._moree_title);
        womenShowMore = findViewById(R.id.women_shorts_more_title);

        backBtn = findViewById(R.id.shorts_back);

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShortsActivity.this, MenshortsActivity.class));

            }
        });


        womenShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShortsActivity.this, WomenShortsActivity.class));

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShortsActivity.super.onBackPressed();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();




        DatabaseReference shortsRef = FirebaseDatabase.getInstance().getReference().child("shorts").child("men");

        FirebaseRecyclerOptions<products> options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(shortsRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();


        FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(ShortsActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("sex", model.getSex());
                                intent.putExtra("category", model.getCategory());

                                startActivity(intent);


                            }
                        });


                    }

                    @NonNull
                    @Override
                    public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                        StartProductViewHolder holder = new StartProductViewHolder(view);

                        return holder;


                    }
                };

        shortsList.setAdapter(adapter);
        adapter.startListening();


        DatabaseReference womenShortsRef = FirebaseDatabase.getInstance().getReference().child("shorts").child("women");

        FirebaseRecyclerOptions<products> Woptions = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(womenShortsRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();


        FirebaseRecyclerAdapter<products, StartProductViewHolder> womenadapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(Woptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(ShortsActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("sex", model.getSex());
                                intent.putExtra("category", model.getCategory());

                                startActivity(intent);


                            }
                        });


                    }

                    @NonNull
                    @Override
                    public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                        StartProductViewHolder holder = new StartProductViewHolder(view);

                        return holder;


                    }
                };

        womenshortsList.setAdapter(womenadapter);
        womenadapter.startListening();



    }
}