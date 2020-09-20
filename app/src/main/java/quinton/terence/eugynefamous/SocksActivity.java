package quinton.terence.eugynefamous;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SocksActivity extends AppCompatActivity {

    private RecyclerView socksList, womensList;
    private TextView showmore, womenShowMore;

    private ImageView backBtn;
    private ProgressBar progressBar;

    //getting intent
    private String type = "";


    //firebase variables
    DatabaseReference socksRef, wosocksRef ;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter, Womenadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socks);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("socksHome").toString();

        }

        socksList = findViewById(R.id.sockslist);
        socksList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        womensList = findViewById(R.id.womensockslist);
        womensList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        showmore = findViewById(R.id.men_more_title);
        womenShowMore = findViewById(R.id.women_more_title);

        backBtn = findViewById(R.id.socks_back);
        progressBar = findViewById(R.id.progress_bar_socks);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SocksActivity.super.onBackPressed();


            }
        });



        showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SocksActivity.this, MensSocksActivity.class).putExtra("MenSocksType", type));

                finish();

            }
        });


        womenShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SocksActivity.this, WomenSocksActivity.class).putExtra("WomenSocksType", type));

                finish();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        SocksAsyncTask task = new SocksAsyncTask(this);
        task.execute(1);


    }


    private class SocksAsyncTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<SocksActivity> activityWeakReference;

        public SocksAsyncTask(SocksActivity activity ) {

            activityWeakReference = new WeakReference<SocksActivity>(activity);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SocksActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            socksList.setVisibility(View.VISIBLE);
            womensList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress( (i * 100) / integers[0] );

                if (i == 0){

                    socksRef = FirebaseDatabase.getInstance().getReference().child("socks").child("men");

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(socksRef.limitToFirst(5) , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();



                    adapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());
                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(SocksActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                            else {

                                                Intent intent = new Intent(SocksActivity.this, ProductDetailsActivity.class);

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

                    wosocksRef = FirebaseDatabase.getInstance().getReference().child("socks").child("women");

                    FirebaseRecyclerOptions<products> Wooptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(wosocksRef.limitToFirst(5) , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();



                    Womenadapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(Wooptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int position, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());
                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(SocksActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                            else{

                                                Intent intent = new Intent(SocksActivity.this, ProductDetailsActivity.class);

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

            return "uploaded";

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            SocksActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            SocksActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }


            socksList.setAdapter(adapter);
            adapter.startListening();

            womensList.setAdapter(Womenadapter);
            Womenadapter.startListening();

            socksList.setVisibility(View.VISIBLE);
            womensList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();


        }

    }

}