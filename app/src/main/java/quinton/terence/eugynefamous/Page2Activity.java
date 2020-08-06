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
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class Page2Activity extends AppCompatActivity {

    private TextView title;
    private ImageView backBtn, searchBtn;
    private RecyclerView pageList;

    private  String sex, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        //getting the intents
        sex = getIntent().getStringExtra("sex");
        category = getIntent().getStringExtra("category");


        title = findViewById(R.id.page_title);
        backBtn = findViewById(R.id.page2_back);
        searchBtn = findViewById(R.id.page2_search);



        pageList = findViewById(R.id.pagelist_);
        pageList.setLayoutManager(new LinearLayoutManager(this));


        title.setText(category);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Page2Activity.this, SearchProductsActivity.class);

                intent.putExtra("sex", sex);
                intent.putExtra("category", category);

                startActivity(intent);



            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Page2Activity.super.onBackPressed();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        DatabaseReference pageRefer =  FirebaseDatabase.getInstance().getReference().child(category).child(sex);

        FirebaseRecyclerOptions<products> options = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(pageRefer.orderByChild("priority").startAt("2").endAt("2") , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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

                                Intent intent = new Intent(Page2Activity.this, ProductDetailsActivity.class);

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

        pageList.setAdapter(adapter);
        adapter.startListening();



    }
}