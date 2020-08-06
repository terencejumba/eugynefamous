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

public class ShoesActivity extends AppCompatActivity {

    private RecyclerView menshoesList, womenshoesList, menSandalList, womenSandalList;
    private TextView showmore, wmnshowmore , sandalShowMore, wmnSandalShowMore;

    private ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes);

        menshoesList = findViewById(R.id.shoeslist);
        menshoesList.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));

        womenshoesList = findViewById(R.id.women_shoeslist);
        womenshoesList.setLayoutManager(new LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false));

        menSandalList = findViewById(R.id.men_sandallist);
        menSandalList.setLayoutManager(new LinearLayoutManager(this));

        womenSandalList = findViewById(R.id.women_sandallist);
        womenSandalList.setLayoutManager(new LinearLayoutManager(this));

        showmore = findViewById(R.id.show_more_);
        wmnshowmore = findViewById(R.id.womnshoes_more_);

        sandalShowMore = findViewById(R.id.mnsandal_more_);
        wmnSandalShowMore = findViewById(R.id.womnsandal_more_);

        backBtn = findViewById(R.id.shoes_back);



        showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoesActivity.this, MenShoesActivity.class));

                finish();

            }
        });


        wmnshowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoesActivity.this, WomenShoesActivity.class));

                finish();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShoesActivity.super.onBackPressed();

            }
        });

        sandalShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoesActivity.this, MenSandalActivity.class));

                finish();


            }
        });

        wmnSandalShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoesActivity.this, WomenSandalActivity.class));

                finish();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();




        DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference().child("shoes").child("men");

        FirebaseRecyclerOptions<products> options = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(shoesRef , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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

                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

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

        menshoesList.setAdapter(adapter);
        adapter.startListening();



        DatabaseReference woshoesRef = FirebaseDatabase.getInstance().getReference().child("shoes").child("women");

        FirebaseRecyclerOptions<products> womenoptions = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(woshoesRef , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();



        FirebaseRecyclerAdapter<products, StartProductViewHolder> womenadapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(womenoptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

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

        womenshoesList.setAdapter(womenadapter);
        womenadapter.startListening();


        DatabaseReference mensandalRef = FirebaseDatabase.getInstance().getReference().child("sandals").child("men");

        FirebaseRecyclerOptions<products> mensandaloptions = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(mensandalRef , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();



        FirebaseRecyclerAdapter<products, StartProductViewHolder> mensandaladapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(mensandaloptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

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

        menSandalList.setAdapter(mensandaladapter);
       mensandaladapter.startListening();


        DatabaseReference womensandalRef = FirebaseDatabase.getInstance().getReference().child("sandals").child("women");

        FirebaseRecyclerOptions<products> womensandaloptions = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                FirebaseRecyclerOptions.Builder<products>()
                .setQuery(womensandalRef , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                .build();



        FirebaseRecyclerAdapter<products, StartProductViewHolder> womensandaladapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(womensandaloptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

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

        womenSandalList.setAdapter(womensandaladapter);
        womensandaladapter.startListening();

    }
}