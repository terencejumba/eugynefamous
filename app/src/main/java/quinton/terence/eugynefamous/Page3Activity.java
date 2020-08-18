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
import android.widget.Button;
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
import quinton.terence.eugynefamous.ViewHolder.ProductViewHolder;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class Page3Activity extends AppCompatActivity {

    private ImageView backBtn, searchBtn;
    private RecyclerView pageList;
    private TextView title;
    private ProgressBar progressBar;
    private Button nextBtn;

    //getting intents
    //getting intent
    private String type = "";
    private String category, sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            type = getIntent().getExtras().get("nextType").toString();

        }

        backBtn = findViewById(R.id.page3_back);
        searchBtn = findViewById(R.id.page3_search);
        title = findViewById(R.id.page_title3);
        progressBar = findViewById(R.id.progress_bar_);
        nextBtn = findViewById(R.id.next_page3);

        pageList = findViewById(R.id.pagelist_3);
        pageList.setLayoutManager(new LinearLayoutManager(this));


        //getting intents
        sex = getIntent().getStringExtra("sex");
        category = getIntent().getStringExtra("category");


        title.setText(category);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Page3Activity.super.onBackPressed();

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    Intent intent = new Intent(Page3Activity.this, SearchProductsActivity.class);

                    intent.putExtra("sex", sex);
                    intent.putExtra("category", category);

                    startActivity(intent);


                }




            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Page3Activity.this, Page4Activity.class);

                intent.putExtra("sex", sex);
                intent.putExtra("category", category);
                intent.putExtra("nextType", type);

                startActivity(intent);


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        page3AsyncTask task = new page3AsyncTask(this);
        task.execute(3);


    }

    private class page3AsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<Page3Activity> activityWeakReference;

        // global variables for this class
        DatabaseReference pageRefer;
        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;

        //a constructor for the weak reference
        page3AsyncTask(Page3Activity activity) {

            activityWeakReference = new WeakReference<Page3Activity>(activity);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Page3Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            pageList.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_LONG).show();


        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 1) {


                    pageRefer = FirebaseDatabase.getInstance().getReference().child(category).child(sex);

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(pageRefer.orderByChild("priority").startAt("3").endAt("3"), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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


                                                Intent intent = new Intent(Page3Activity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else {

                                                Intent intent = new Intent(Page3Activity.this, ProductDetailsActivity.class);

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

            Page3Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            activity.progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Page3Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }


            pageList.setAdapter(adapter);
            adapter.startListening();

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();


        }
    }

}