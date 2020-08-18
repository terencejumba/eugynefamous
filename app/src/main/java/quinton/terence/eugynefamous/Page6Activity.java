package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class Page6Activity extends AppCompatActivity {

    private TextView title;
    private ImageView backBtn, searchBtn;
    private RecyclerView pageList;
    private ProgressBar progressBar;

    private Button nextBtn;

    //getting intent
    private String type = "";
    private  String sex, category;

    //firebase variables
    DatabaseReference pageRefer;
    FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page6);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("nextType").toString();

        }


        //getting the intents
        sex = getIntent().getStringExtra("sex");
        category = getIntent().getStringExtra("category");

        title = findViewById(R.id.page_title6);
        backBtn = findViewById(R.id.page6_back);
        searchBtn = findViewById(R.id.page6_search);
        pageList = findViewById(R.id.pagelist_6);
        progressBar = findViewById(R.id.progress_bar_pg6);
        nextBtn = findViewById(R.id.next_page6);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Page6Activity.super.onBackPressed();


            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    Intent intent = new Intent(Page6Activity.this, SearchProductsActivity.class);

                    intent.putExtra("sex", sex);
                    intent.putExtra("category", category);

                    startActivity(intent);
                }




            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Page6Activity.this, Page7Activity.class);

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

        title.setText(category);

        Page6AsyncTask task = new Page6AsyncTask(this);
        task.execute(3);

    }

    private class Page6AsyncTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<Page6Activity> activityWeakReference;

        public Page6AsyncTask(Page6Activity activity) {

            activityWeakReference = new WeakReference<Page6Activity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Page6Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()){

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

                if (i == 1){


                    pageRefer = FirebaseDatabase.getInstance().getReference().child(category).child(sex);

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(pageRefer.orderByChild("priority").startAt("6").endAt("6") , products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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


                                                Intent intent = new Intent(Page6Activity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else{

                                                Intent intent = new Intent(Page6Activity.this, ProductDetailsActivity.class);

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

            Page6Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()){

                return;

            }

            progressBar.setProgress(values[0]);


        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Page6Activity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()){

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