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

public class MenBagsActivity extends AppCompatActivity {

    private RecyclerView BagsList;

    private Button nextBtn;
    private ImageView backBtn, searchBtn;
    private ProgressBar progressBar;

    //getting intent
    private String type = "";

    //firebase variables
    DatabaseReference bagsRefe;
    FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_bags);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("menbagsType").toString();

        }

        BagsList = findViewById(R.id.__bagslist_);
        BagsList.setLayoutManager(new LinearLayoutManager(this));


        backBtn = findViewById(R.id.menbags_back);

        searchBtn = findViewById(R.id.search_menbags);
        nextBtn = findViewById(R.id.next_menbag);
        progressBar = findViewById(R.id.progress_bar_menbags);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenBagsActivity.this, Page2Activity.class);

                intent.putExtra("sex", "men");
                intent.putExtra("category", "bags");
                intent.putExtra("nextType", type);

                startActivity(intent);


            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MenBagsActivity.super.onBackPressed();


            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    Intent intent = new Intent(MenBagsActivity.this, SearchProductsActivity.class);

                    intent.putExtra("sex", "men");
                    intent.putExtra("category", "bags");

                    startActivity(intent);

                }


            }
        });



    }


    @Override
    protected void onStart() {

        super.onStart();



      MenBagsAsyncTask task = new MenBagsAsyncTask(this);
      task.execute(1);


    }

    private class  MenBagsAsyncTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<MenBagsActivity> activityWeakReference;


        public MenBagsAsyncTask(MenBagsActivity activity) {

            activityWeakReference = new WeakReference<MenBagsActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MenBagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            BagsList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress( (i * 100) / integers[0] );

                if (i == 1){


                    bagsRefe = FirebaseDatabase.getInstance().getReference().child("bags").child("men");

                    FirebaseRecyclerOptions<products> bagsoptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(bagsRefe.orderByChild("priority").startAt("1").endAt("1"), products.class)//the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    adapter =
                            new FirebaseRecyclerAdapter<products, ProductViewHolder>(bagsoptions) {
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


                                                Intent intent = new Intent(MenBagsActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                            else {

                                                Intent intent = new Intent(MenBagsActivity.this, ProductDetailsActivity.class);

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

            MenBagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);

        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MenBagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            BagsList.setAdapter(adapter);
            adapter.startListening();

            BagsList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();


        }

    }


}