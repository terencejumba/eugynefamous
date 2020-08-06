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

public class LingerieActivity extends AppCompatActivity {

    private RecyclerView lingerieList;

    private ImageView backBtn, searchBtn;

    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lingerie);

        lingerieList = findViewById(R.id.lingerielist_);
        lingerieList.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.lingerie_back);

        searchBtn = findViewById(R.id.lingerie_search);

        nextBtn = findViewById(R.id.next_lingerie);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LingerieActivity.this, Page2Activity.class);

                intent.putExtra("sex", "women");
                intent.putExtra("category", "lingerie");

                startActivity(intent);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LingerieActivity.super.onBackPressed();

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LingerieActivity.this, SearchProductsActivity.class);

                intent.putExtra("sex", "women");
                intent.putExtra("category", "lingerie");

                startActivity(intent);


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference lingerieRefer = FirebaseDatabase.getInstance().getReference().child("lingerie").child("women");

        FirebaseRecyclerOptions<products> lingerieoptions = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(lingerieRefer.orderByChild("priority").startAt("1").endAt("1"), products.class)//the category shows what you want to show in the recycler view here we are searching using the pname
//                .setQuery(shirtsRef.orderByChild("priority").startAt("1").endAt("1"), products.class)
                .build();


        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<products, ProductViewHolder>(lingerieoptions) {
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

                                Intent intent = new Intent(LingerieActivity.this, ProductDetailsActivity.class);

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

        lingerieList.setAdapter(adapter);
        adapter.startListening();


    }


}