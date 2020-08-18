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

public class TrousersActivity extends AppCompatActivity {

    //Global variables

    private RecyclerView trousersList, womensList;

    private TextView showMore, wmnShowMore;

    private ImageView backBtn;
    private ProgressBar progressBar;

    //getting intent
    private String type = "";

    //firebase variables
    DatabaseReference trousersRef, womentrousersRef;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter, womenAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trousers);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("trouserHome").toString();

        }

        trousersList = findViewById(R.id.trouserslist);
        trousersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        womensList = findViewById(R.id.womentrouserslist);
        womensList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        showMore = findViewById(R.id._more_title);
        wmnShowMore = findViewById(R.id._women_more_title);

        backBtn = findViewById(R.id.trousers_back);
        progressBar = findViewById(R.id.progress_bar_trousers);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TrousersActivity.super.onBackPressed();

            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(TrousersActivity.this, MenTrousersActivity.class).putExtra("menTrouserType", type));


            }
        });

        wmnShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TrousersActivity.this, WomenTrouserActivity.class).putExtra("womenTrouserType", type));


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        //calling our async Task class

        TrouserAsnycTask task = new TrouserAsnycTask(this);
        task.execute(5);



    }

    private class TrouserAsnycTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<TrousersActivity> activityWeakReference;


        public TrouserAsnycTask(TrousersActivity activity) {

            activityWeakReference = new WeakReference<TrousersActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            TrousersActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            trousersList.setVisibility(View.VISIBLE);
            womensList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 2) {

                    trousersRef = FirebaseDatabase.getInstance().getReference().child("trousers").child("men");

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(trousersRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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


                                                Intent intent = new Intent(TrousersActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(TrousersActivity.this, ProductDetailsActivity.class);

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

                    womentrousersRef = FirebaseDatabase.getInstance().getReference().child("trousers").child("women");

                    FirebaseRecyclerOptions<products> womenOptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(womentrousersRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    womenAdapter =
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

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(TrousersActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(TrousersActivity.this, ProductDetailsActivity.class);

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

            TrousersActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TrousersActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            trousersList.setAdapter(adapter);
            adapter.startListening();

            womensList.setAdapter(womenAdapter);
            womenAdapter.startListening();

            trousersList.setVisibility(View.VISIBLE);
            womensList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();


        }


    }

}