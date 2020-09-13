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

public class MensSocksActivity extends AppCompatActivity {

    private RecyclerView mensList;

    private ImageView backBtn, searchBtn;
    private Button nextBtn;

    private ProgressBar progressBar;

    //getting intent
    private String type = "";

    //firebase variables
    DatabaseReference socksRefe;
    FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mens_socks);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            type = getIntent().getExtras().get("MenSocksType").toString();

        }


        mensList = findViewById(R.id.menssockslist_);
        mensList.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.mensocks_back);
        searchBtn = findViewById(R.id.mensocks_search);
        nextBtn = findViewById(R.id.next_mensocks);

        progressBar = findViewById(R.id.progress_bar_menSocks);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MensSocksActivity.this, Page2Activity.class);

                intent.putExtra("sex", "men");
                intent.putExtra("category", "socks");
                intent.putExtra("nextType", type);

                startActivity(intent);


            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    Intent intent = new Intent(MensSocksActivity.this, SearchProductsActivity.class);

                    intent.putExtra("sex", "men");
                    intent.putExtra("category", "socks");

                    startActivity(intent);



                }



            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MensSocksActivity.super.onBackPressed();

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        MenSocksAsyncTask task = new MenSocksAsyncTask(this);
        task.execute(1);

    }

    //creating an inner class
    private class MenSocksAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<MensSocksActivity> activityWeakReference;


        public MenSocksAsyncTask(MensSocksActivity activity) {

            activityWeakReference = new WeakReference<MensSocksActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            MensSocksActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }


            progressBar.setVisibility(View.VISIBLE);
            mensList.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 1) {

                    socksRefe = FirebaseDatabase.getInstance().getReference().child("socks").child("men");

                    FirebaseRecyclerOptions<products> socksoptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(socksRefe.orderByChild("priority").startAt("1").endAt("1"), products.class)//the category shows what you want to show in the recycler view here we are searching using the pname
//                .setQuery(shirtsRef.orderByChild("priority").startAt("1").endAt("1"), products.class)
                            .build();


                    adapter =
                            new FirebaseRecyclerAdapter<products, ProductViewHolder>(socksoptions) {
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

                                            if (type.equals("Admin")) {


                                                Intent intent = new Intent(MensSocksActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            } else {

                                                Intent intent = new Intent(MensSocksActivity.this, ProductDetailsActivity.class);

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

            MensSocksActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MensSocksActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            mensList.setAdapter(adapter);
            adapter.startListening();

            mensList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();


        }
    }

}