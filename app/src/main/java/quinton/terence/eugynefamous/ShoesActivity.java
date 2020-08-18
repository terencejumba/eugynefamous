package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import quinton.terence.eugynefamous.Admin.AdminMaintainProductsActivity;
import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.ViewHolder.StartProductViewHolder;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class ShoesActivity extends AppCompatActivity {

    private RecyclerView menshoesList, womenshoesList, menSandalList, womenSandalList;
    private TextView showmore, wmnshowmore, sandalShowMore, wmnSandalShowMore;

    private ImageView backBtn;
    private ProgressBar progressBar;

    private String type = "";

    //firebase variables
    DatabaseReference shoesRef, woshoesRef, mensandalRef, womensandalRef;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter, womenadapter, mensandaladapter, womensandaladapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("shoesHome").toString();

        }

        menshoesList = findViewById(R.id.shoeslist);
        menshoesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        womenshoesList = findViewById(R.id.women_shoeslist);
        womenshoesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        menSandalList = findViewById(R.id.men_sandallist);
        menSandalList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        womenSandalList = findViewById(R.id.women_sandallist);
        womenSandalList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        showmore = findViewById(R.id.show_more_);
        wmnshowmore = findViewById(R.id.womnshoes_more_);

        sandalShowMore = findViewById(R.id.mnsandal_more_);
        wmnSandalShowMore = findViewById(R.id.womnsandal_more_);

        backBtn = findViewById(R.id.shoes_back);
        progressBar = findViewById(R.id.progress_bar_shoes);


        showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoesActivity.this, MenShoesActivity.class).putExtra("menshoeType", type));


            }
        });


        wmnshowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoesActivity.this, WomenShoesActivity.class).putExtra("womenshoeType", type));


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

                startActivity(new Intent(ShoesActivity.this, MenSandalActivity.class).putExtra("menSandalType", type));


            }
        });

        wmnSandalShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ShoesActivity.this, WomenSandalActivity.class).putExtra("womenSandalType", type));


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        ShoesAsyncTask task = new ShoesAsyncTask(this);
        task.execute(5);


    }

    private class ShoesAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<ShoesActivity> activityWeakReference;


        public ShoesAsyncTask(ShoesActivity activity) {

            activityWeakReference = new WeakReference<ShoesActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ShoesActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setVisibility(View.VISIBLE);
            womenSandalList.setVisibility(View.VISIBLE);


            Toast.makeText(activity, "loading...", Toast.LENGTH_LONG).show();


        }


        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 2) {

                    shoesRef = FirebaseDatabase.getInstance().getReference().child("shoes").child("men");

                    FirebaseRecyclerOptions<products> options = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(shoesRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    adapter =
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

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(ShoesActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            }




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


                    woshoesRef = FirebaseDatabase.getInstance().getReference().child("shoes").child("women");

                    FirebaseRecyclerOptions<products> womenoptions = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(woshoesRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    womenadapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(womenoptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).into(holder.imageView);


                                    //setting a click listener to the recycler view layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(ShoesActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);




                                            }


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
                    mensandalRef = FirebaseDatabase.getInstance().getReference().child("sandals").child("men");

                    FirebaseRecyclerOptions<products> mensandaloptions = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(mensandalRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    mensandaladapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(mensandaloptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).into(holder.imageView);


                                    //setting a click listener to the recycler view

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(ShoesActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            }




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

                    womensandalRef = FirebaseDatabase.getInstance().getReference().child("sandals").child("women");

                    FirebaseRecyclerOptions<products> womensandaloptions = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(womensandalRef, products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    womensandaladapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(womensandaloptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());

                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).into(holder.imageView);


                                    //setting a click listener to the recycler view

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(ShoesActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);

                                            }




                                        }
                                    });


                                    woshoesRef = FirebaseDatabase.getInstance().getReference().child("shoes").child("women");

                                    FirebaseRecyclerOptions<products> womenoptions = new                             //.orderByChild("Category").startAt(shoes).endAt(shoes)
                                            FirebaseRecyclerOptions.Builder<products>()
                                            .setQuery(woshoesRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                                            .build();


                                    womenadapter =
                                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(womenoptions) {
                                                @Override
                                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                                                    holder.txtProductName.setText(model.getPname());

                                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                                    Picasso.get().load(model.getImage()).into(holder.imageView);


                                                    //setting a click listener to the recycler view

                                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            if (type.equals("Admin")){


                                                                Intent intent = new Intent(ShoesActivity.this, AdminMaintainProductsActivity.class);

                                                                intent.putExtra("pid", model.getPid());
                                                                intent.putExtra("category", model.getCategory());
                                                                intent.putExtra("sex", model.getSex());

                                                                startActivity(intent);


                                                            }
                                                            else{

                                                                Intent intent = new Intent(ShoesActivity.this, ProductDetailsActivity.class);

                                                                intent.putExtra("pid", model.getPid());
                                                                intent.putExtra("category", model.getCategory());
                                                                intent.putExtra("sex", model.getSex());

                                                                startActivity(intent);

                                                            }




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

                                }

                                @NonNull
                                @Override
                                public StartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.start_products_layout, parent, false);

                                    StartProductViewHolder holder = new StartProductViewHolder(view);

                                    return holder;


                                }
                            };


                }

                //this sleeps the thread to make it delay for the amount of seconds
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return "updated";

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            ShoesActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ShoesActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {

                return;

            }

            menshoesList.setAdapter(adapter);
            adapter.startListening();

            womenshoesList.setAdapter(womenadapter);
            womenadapter.startListening();

            menSandalList.setAdapter(mensandaladapter);
            mensandaladapter.startListening();

            womenSandalList.setAdapter(womensandaladapter);
            womensandaladapter.startListening();

            menshoesList.setVisibility(View.VISIBLE);
            womenshoesList.setVisibility(View.VISIBLE);
            menSandalList.setVisibility(View.VISIBLE);
            womenSandalList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();

        }


    }

}