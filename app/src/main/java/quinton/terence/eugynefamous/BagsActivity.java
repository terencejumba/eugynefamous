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

public class BagsActivity extends AppCompatActivity {

    private RecyclerView handbagsList, bagsList ;

    private TextView showMore, menshowMore;

    private ImageView backBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bags);

        handbagsList = findViewById(R.id.bagslist);
        handbagsList.setLayoutManager(new LinearLayoutManager( this , LinearLayoutManager.HORIZONTAL, false));

        bagsList = findViewById(R.id._bagslist);
        bagsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        showMore = findViewById(R.id.handbags_more_title);
        menshowMore = findViewById(R.id._bags_more_title);

        backBtn = findViewById(R.id.bags_back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BagsActivity.super.onBackPressed();

            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BagsActivity.this, HandBagsActivity.class));

            }
        });



        menshowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BagsActivity.this, MenBagsActivity.class));


            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference bagsRef = FirebaseDatabase.getInstance().getReference().child("bags").child("women");

        FirebaseRecyclerOptions<products> options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(bagsRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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

                                Intent intent = new Intent(BagsActivity.this, ProductDetailsActivity.class);

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

        handbagsList.setAdapter(adapter);
        adapter.startListening();


        DatabaseReference OverbagsRef = FirebaseDatabase.getInstance().getReference().child("bags").child("men");

        FirebaseRecyclerOptions<products> Options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(OverbagsRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();


        FirebaseRecyclerAdapter<products, StartProductViewHolder> Adapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(Options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(BagsActivity.this, ProductDetailsActivity.class);

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

        bagsList.setAdapter(Adapter);
        Adapter.startListening();




    }
}