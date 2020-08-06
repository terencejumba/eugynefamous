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

public class TopsActtivity extends AppCompatActivity {



    private RecyclerView blouses, tanks, tunics ;

    private  TextView showMore, TunicsShowMore, TanksShowmore;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tops_acttivity);


       blouses = findViewById(R.id.__blouses);
       blouses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

       tanks = findViewById(R.id.__tanks);
       tanks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

       tunics = findViewById(R.id.__tunics);
       tunics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

       showMore = findViewById(R.id.blouses_more);
       TunicsShowMore = findViewById(R.id.tunics_more);
       TanksShowmore = findViewById(R.id.tanks_more);

       backBtn = findViewById(R.id.tops_back);

       backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               TopsActtivity.super.onBackPressed();

           }
       });

       showMore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               startActivity(new Intent(TopsActtivity.this, BlouseActivity.class));

               finish();

           }
       });

       TunicsShowMore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               startActivity(new Intent(TopsActtivity.this, TunicsActivity.class));

               finish();

           }
       });


       TanksShowmore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               startActivity(new Intent(TopsActtivity.this, TanksActivity.class));

               finish();

           }
       });



    }

    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference blousesRef = FirebaseDatabase.getInstance().getReference().child("blouse").child("women");

        FirebaseRecyclerOptions<products> options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(blousesRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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

                                Intent intent = new Intent(TopsActtivity.this, ProductDetailsActivity.class);

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

        blouses.setAdapter(adapter);
        adapter.startListening();


       DatabaseReference tanksRef = FirebaseDatabase.getInstance().getReference().child("tanks").child("women");

        FirebaseRecyclerOptions<products> tankoptions = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(tanksRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();


        FirebaseRecyclerAdapter<products, StartProductViewHolder> tankadapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(tankoptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(TopsActtivity.this, ProductDetailsActivity.class);

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

        tanks.setAdapter(tankadapter);
        tankadapter.startListening();


       DatabaseReference tunicsRef = FirebaseDatabase.getInstance().getReference().child("tunics").child("women");

        FirebaseRecyclerOptions<products> tunicoptions = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(tunicsRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();


        FirebaseRecyclerAdapter<products, StartProductViewHolder> tunicadapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(tunicoptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(TopsActtivity.this, ProductDetailsActivity.class);

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

        tunics.setAdapter(tunicadapter);
        tunicadapter.startListening();


      


    }

}