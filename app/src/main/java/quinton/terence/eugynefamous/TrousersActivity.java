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

public class TrousersActivity extends AppCompatActivity {

    private RecyclerView trousersList, womensList;

    private TextView showMore, wmnShowMore;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trousers);

        trousersList = findViewById(R.id.trouserslist);
        trousersList.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));

        womensList = findViewById(R.id.womentrouserslist);
        womensList.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));

        showMore =  findViewById(R.id._more_title);
        wmnShowMore = findViewById(R.id._women_more_title);

        backBtn = findViewById(R.id.trousers_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TrousersActivity.super.onBackPressed();

            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(TrousersActivity.this, MenTrousersActivity.class));


            }
        });

        wmnShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TrousersActivity.this, WomenTrouserActivity.class));


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference trousersRef = FirebaseDatabase.getInstance().getReference().child("trousers").child("men");

        FirebaseRecyclerOptions<products> options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(trousersRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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

                                Intent intent = new Intent(TrousersActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

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

        trousersList.setAdapter(adapter);
        adapter.startListening();


        DatabaseReference womentrousersRef = FirebaseDatabase.getInstance().getReference().child("trousers").child("women");

        FirebaseRecyclerOptions<products> womenOptions = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(womentrousersRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();


        FirebaseRecyclerAdapter<products, StartProductViewHolder> womenAdapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(womenOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(TrousersActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

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

        womensList.setAdapter(womenAdapter);
        womenAdapter.startListening();





    }
}