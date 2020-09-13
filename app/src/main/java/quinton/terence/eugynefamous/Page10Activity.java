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

public class Page10Activity extends AppCompatActivity {

    private TextView title;
    private ImageView backBtn, searchBtn;
    private RecyclerView pageList;
    private ProgressBar progressBar;



    //getting intent
    private String type = "";
    private String sex, category;

    //firebase variables
    DatabaseReference pageRefer;
    FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page10);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            type = getIntent().getExtras().get("nextType").toString();

        }


        //getting the intents
        sex = getIntent().getStringExtra("sex");
        category = getIntent().getStringExtra("category");

        title = findViewById(R.id.page_title10);
        backBtn = findViewById(R.id.page10_back);
        searchBtn = findViewById(R.id.page10_search);
        pageList = findViewById(R.id.pagelist_10);
        progressBar = findViewById(R.id.progress_bar_pg10);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Page10Activity.super.onBackPressed();

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    Intent intent = new Intent(Page10Activity.this, SearchProductsActivity.class);

                    intent.putExtra("sex", sex);
                    intent.putExtra("category", category);

                    startActivity(intent);
                }





            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

        title.setText(category);

        Page10AsyncTask task = new Page10AsyncTask(this);
        task.execute(1);

    }

    private class Page10AsyncTask extends AsyncTask<Integer, Integer, String> {

       private WeakReference<Page10Activity> activityWeakReference;


        public Page10AsyncTask(Page10Activity activity) {

            activityWeakReference = new WeakReference<Page10Activity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Page10Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            pageList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 1) {

                    pageRefer = FirebaseDatabase.getInstance().getReference().child(category).child(sex);

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(pageRefer.orderByChild("priority").startAt("10").endAt("10"), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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

                                            if (type.equals("Admin")) {


                                                Intent intent = new Intent(Page10Activity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            } else {

                                                Intent intent = new Intent(Page10Activity.this, ProductDetailsActivity.class);

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

            Page10Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Page10Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            pageList.setAdapter(adapter);
            adapter.startListening();

            pageList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();

        }

    }
}