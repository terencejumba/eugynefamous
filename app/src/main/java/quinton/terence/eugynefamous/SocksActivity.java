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
import quinton.terence.eugynefamous.ViewHolder.StartProductViewHolder;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class SocksActivity extends AppCompatActivity {

    private RecyclerView socksList, womensList;
    private TextView showmore, womenShowMore;

    private ImageView backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socks);

        socksList = findViewById(R.id.sockslist);
        socksList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        womensList = findViewById(R.id.womensockslist);
        womensList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        showmore = findViewById(R.id.men_more_title);
        womenShowMore = findViewById(R.id.women_more_title);

        backBtn = findViewById(R.id.socks_back);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SocksActivity.super.onBackPressed();


            }
        });



        showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SocksActivity.this, MensSocksActivity.class));

                finish();

            }
        });


        womenShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SocksActivity.this, WomenSocksActivity.class));

                finish();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference socksRef = FirebaseDatabase.getInstance().getReference().child("socks").child("men");

        FirebaseRecyclerOptions<products> options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(socksRef , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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

                                Intent intent = new Intent(SocksActivity.this, ProductDetailsActivity.class);

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

        socksList.setAdapter(adapter);
        adapter.startListening();



        DatabaseReference wosocksRef = FirebaseDatabase.getInstance().getReference().child("socks").child("women");

        FirebaseRecyclerOptions<products> Wooptions = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(wosocksRef , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();



        FirebaseRecyclerAdapter<products, StartProductViewHolder> Womenadapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(Wooptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(SocksActivity.this, ProductDetailsActivity.class);

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

        womensList.setAdapter(Womenadapter);
        Womenadapter.startListening();


    }


}