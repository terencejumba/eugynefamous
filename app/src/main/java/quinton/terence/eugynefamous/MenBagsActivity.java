package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.ViewHolder.ProductViewHolder;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class MenBagsActivity extends AppCompatActivity {

    private RecyclerView BagsList;

    private Button nextBtn;
    private ImageView backBtn, searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_bags);

        BagsList = findViewById(R.id.__bagslist_);
        BagsList.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.menbags_back);

        searchBtn = findViewById(R.id.search_menbags);
        nextBtn = findViewById(R.id.next_menbag);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenBagsActivity.this, Page2Activity.class);

                intent.putExtra("sex", "men");
                intent.putExtra("category", "bags");

                startActivity(intent);


            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MenBagsActivity.super.onBackPressed();


            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenBagsActivity.this, SearchProductsActivity.class);

                intent.putExtra("sex", "men");
                intent.putExtra("category", "bags");

                startActivity(intent);




            }
        });



    }


    @Override
    protected void onStart() {

        super.onStart();


        DatabaseReference bagsRefe = FirebaseDatabase.getInstance().getReference().child("bags").child("men");

        FirebaseRecyclerOptions<products> bagsoptions = new
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(bagsRefe, products.class)//the category shows what you want to show in the recycler view here we are searching using the pname
//                .setQuery(shirtsRef.orderByChild("priority").startAt("1").endAt("1"), products.class)
                .build();


        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<products, ProductViewHolder>(bagsoptions) {
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

                                Intent intent = new Intent(MenBagsActivity.this, ProductDetailsActivity.class);

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

        BagsList.setAdapter(adapter);
        adapter.startListening();


    }


}