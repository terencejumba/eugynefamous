package quinton.terence.eugynefamous;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import quinton.terence.eugynefamous.ViewHolder.ProductViewHolder;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class HatsActivity extends AppCompatActivity {

    private RecyclerView hatsList;

    private ImageView backBtn, searchBtn;
    private ProgressBar progressBar;

    private Button nextBtn;

    //getting intent
    private String type = "";

    //firebase Variables

    private DatabaseReference hatsRef;
    private FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hats);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("hatsHome").toString();

        }

        hatsList = findViewById(R.id.hatslist);
        hatsList.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.hats_back);
        searchBtn = findViewById(R.id.hats_search);
        nextBtn = findViewById(R.id.next_hats);
        progressBar = findViewById(R.id.progress_bar_hats);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HatsActivity.this, Page2Activity.class);

                intent.putExtra("sex", "men");
                intent.putExtra("category", "hats");
                intent.putExtra("nextType", type);

                startActivity(intent);


            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HatsActivity.super.onBackPressed();

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    Intent intent = new Intent(HatsActivity.this, SearchProductsActivity.class);

                    intent.putExtra("sex", "men");
                    intent.putExtra("category", "hats");

                    startActivity(intent);

                }




            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        HatsAsyncTask task = new HatsAsyncTask(this);
        task.execute(1);


    }

    private class HatsAsyncTask extends AsyncTask<Integer, Integer, String> {

        //this is to avoid memory leaks
        //so we introduce a weak reference
        private WeakReference<HatsActivity> activityWeakReference;

        public HatsAsyncTask(HatsActivity activity) {

            activityWeakReference = new WeakReference<HatsActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            HatsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            hatsList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {
                //this calls the on progress
                //on progress will use this values
                publishProgress((i * 100) / integers[0]);

                if (i == 1) {

                    hatsRef = FirebaseDatabase.getInstance().getReference().child("hats").child("men");

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(hatsRef.orderByChild("priority").startAt("1").endAt("1"), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    adapter =
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

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(HatsActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                            else {

                                                Intent intent = new Intent(HatsActivity.this, ProductDetailsActivity.class);

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
                                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent, false);

                                    ProductViewHolder holder = new ProductViewHolder(view);

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

            HatsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            //this updates the progress bar while its being executed on the background thread using the loop
            progressBar.setProgress(values[0]);


        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            HatsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            hatsList.setAdapter(adapter);
            adapter.startListening();

            hatsList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();


        }

    }


}