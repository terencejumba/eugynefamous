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

public class TopsActtivity extends AppCompatActivity {


    private RecyclerView blouses, tanks, tunics;

    private TextView showMore, TunicsShowMore, TanksShowmore;
    private ProgressBar progressBar;

    private ImageView backBtn;

    //getting intent
    private String type = "";

    // firebase variables
    DatabaseReference blousesRef, tanksRef, tunicsRef;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter, tankadapter, tunicadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tops_acttivity);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("topsHome").toString();

        }


        blouses = findViewById(R.id.__blouses);
        blouses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        tanks = findViewById(R.id.__tanks);
        tanks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        tunics = findViewById(R.id.__tunics);
        tunics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        showMore = findViewById(R.id.blouses_more);
        TunicsShowMore = findViewById(R.id.tunics_more);
        TanksShowmore = findViewById(R.id.tanks_more);
        progressBar = findViewById(R.id.progress_bar_tops);

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

                startActivity(new Intent(TopsActtivity.this, BlouseActivity.class).putExtra("blouseType", type));


            }
        });

        TunicsShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TopsActtivity.this, TunicsActivity.class).putExtra("TunicsType", type));


            }
        });


        TanksShowmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TopsActtivity.this, TanksActivity.class).putExtra("tanksType", type));


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //calling our AsyncTask class

        TopsAsyncTask task = new TopsAsyncTask(this);
        task.execute(5);

        //the 5 means the loop will always run for 5 seconds each time the activity restarts or after being on pause

    }

    //creating an inner AsyncTask class
    private class TopsAsyncTask extends AsyncTask<Integer, Integer, String> {

        //creating a weak reference which helps to avoid memory leaks
        private WeakReference<TopsActtivity> acttivityWeakReference;


        public TopsAsyncTask(TopsActtivity activity) {

            acttivityWeakReference = new WeakReference<TopsActtivity>(activity);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            TopsActtivity activity = acttivityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            blouses.setVisibility(View.VISIBLE);
            tunics.setVisibility(View.VISIBLE);
            tanks.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 2) {

                    blousesRef = FirebaseDatabase.getInstance().getReference().child("blouse").child("women");

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(blousesRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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


                                                Intent intent = new Intent(TopsActtivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else {

                                                Intent intent = new Intent(TopsActtivity.this, ProductDetailsActivity.class);

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

                    tanksRef = FirebaseDatabase.getInstance().getReference().child("tanks").child("women");

                    FirebaseRecyclerOptions<products> tankoptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(tanksRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    tankadapter =
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

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(TopsActtivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(TopsActtivity.this, ProductDetailsActivity.class);

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

                    tunicsRef = FirebaseDatabase.getInstance().getReference().child("tunics").child("women");

                    FirebaseRecyclerOptions<products> tunicoptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(tunicsRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    tunicadapter =
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

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(TopsActtivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else {

                                                Intent intent = new Intent(TopsActtivity.this, ProductDetailsActivity.class);

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


            TopsActtivity activity = acttivityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            TopsActtivity activity = acttivityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            blouses.setAdapter(adapter);
            adapter.startListening();

            tanks.setAdapter(tankadapter);
            tankadapter.startListening();

            tunics.setAdapter(tunicadapter);
            tunicadapter.startListening();

            blouses.setVisibility(View.VISIBLE);
            tanks.setVisibility(View.VISIBLE);
            tunics.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();


        }

    }

}