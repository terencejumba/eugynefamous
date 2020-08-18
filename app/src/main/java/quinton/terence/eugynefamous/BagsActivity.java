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

public class BagsActivity extends AppCompatActivity {

    private RecyclerView handbagsList, bagsList;

    private TextView showMore, menshowMore;
    private ProgressBar progressBar;

    //getting intent
    private String type = "";

    private ImageView backBtn;

    //firebase vars
    DatabaseReference bagsRef;
    DatabaseReference OverbagsRef;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> Adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bags);

        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("bagsHome").toString();

        }

        handbagsList = findViewById(R.id.bagslist);
        handbagsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        bagsList = findViewById(R.id._bagslist);
        bagsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        showMore = findViewById(R.id.handbags_more_title);
        menshowMore = findViewById(R.id._bags_more_title);

        backBtn = findViewById(R.id.bags_back);
        progressBar = findViewById(R.id.progress_bar_bags);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BagsActivity.super.onBackPressed();

            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BagsActivity.this, HandBagsActivity.class).putExtra("handbagsType", type));

            }
        });


        menshowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(BagsActivity.this, MenBagsActivity.class).putExtra("menbagsType", type));


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        bagsAsyncTask task = new bagsAsyncTask(this);

        task.execute(3);


    }


    private class bagsAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<BagsActivity> activityWeakReference;

        public bagsAsyncTask(BagsActivity activity) {

            activityWeakReference = new WeakReference<BagsActivity>(activity);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            BagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setVisibility(View.VISIBLE);
            bagsList.setVisibility(View.VISIBLE);
            handbagsList.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                //publish progress calls the on progress
                //this is for the progress bar
                publishProgress((i * 100) / integers[0]);

                if (i == 1) {


                    bagsRef = FirebaseDatabase.getInstance().getReference().child("bags").child("women");

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(bagsRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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


                                                Intent intent = new Intent(BagsActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else {

                                                Intent intent = new Intent(BagsActivity.this, ProductDetailsActivity.class);

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


                    OverbagsRef = FirebaseDatabase.getInstance().getReference().child("bags").child("men");

                    FirebaseRecyclerOptions<products> Options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(OverbagsRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    Adapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(Options) {
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


                                                Intent intent = new Intent(BagsActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else {

                                                Intent intent = new Intent(BagsActivity.this, ProductDetailsActivity.class);

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

            BagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            activity.progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            BagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }


            handbagsList.setAdapter(adapter);
            adapter.startListening();

            bagsList.setAdapter(Adapter);
            Adapter.startListening();

            handbagsList.setVisibility(View.VISIBLE);
            bagsList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(BagsActivity.this, s, Toast.LENGTH_SHORT).show();


        }

    }

}