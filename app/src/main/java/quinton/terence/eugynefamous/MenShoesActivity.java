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

public class MenShoesActivity extends AppCompatActivity {

    private RecyclerView mensList;

    private ImageView backBtn, searchBtn;
    private ProgressBar progressBar;
    private Button nextBtn;

    private String type = "";

    //firebase variables
    DatabaseReference shoeRefer;
    FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_shoes);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("menshoeType").toString();

        }


        mensList = findViewById(R.id._mensshoeslist_);
        mensList.setLayoutManager(new LinearLayoutManager(this));


        backBtn = findViewById(R.id.menshoes_back);

        searchBtn = findViewById(R.id.menshoes_search);
        nextBtn = findViewById(R.id.next_menshoes);
        progressBar = findViewById(R.id.progress_bar_menshoe);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenShoesActivity.this, Page2Activity.class);

                intent.putExtra("sex", "men");
                intent.putExtra("category", "shoes");
                intent.putExtra("nextType", type);

                startActivity(intent);


            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("Admin")) {

                    Intent intent = new Intent(MenShoesActivity.this, SearchProductsActivity.class);

                    intent.putExtra("sex", "men");
                    intent.putExtra("category", "shoes");

                    startActivity(intent);


                }




            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MenShoesActivity.super.onBackPressed();

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

         MenShoeAsyncTask task = new MenShoeAsyncTask(this);
         task.execute(1);



    }

    private class MenShoeAsyncTask extends AsyncTask<Integer, Integer, String>{

        private WeakReference<MenShoesActivity> activityWeakReference;

        public MenShoeAsyncTask(MenShoesActivity activity) {

            activityWeakReference = new WeakReference<MenShoesActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MenShoesActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            mensList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress( (i * 100) / integers[0] );

                if (i == 0){

                    shoeRefer = FirebaseDatabase.getInstance().getReference().child("shoes").child("men");

                    FirebaseRecyclerOptions<products> shoeoptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(shoeRefer.orderByChild("priority").startAt("1").endAt("1"), products.class)//the category shows what you want to show in the recycler view here we are searching using the pname
//                .setQuery(shirtsRef.orderByChild("priority").startAt("1").endAt("1"), products.class)
                            .build();


                    adapter =
                            new FirebaseRecyclerAdapter<products, ProductViewHolder>(shoeoptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final products model) {

                                    holder.txtProductName.setText(model.getPname());
                                    holder.txtProductDescription.setText(model.getDescription());
                                    holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                                    Picasso.get().load(model.getImage()).placeholder(R.drawable.progressload).into(holder.imageView);


                                    //setting a click listener to the relative layout

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (type.equals("Admin")){


                                                Intent intent = new Intent(MenShoesActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }
                                            else {

                                                Intent intent = new Intent(MenShoesActivity.this, ProductDetailsActivity.class);

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

            MenShoesActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);

        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            MenShoesActivity activity = activityWeakReference.get();

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