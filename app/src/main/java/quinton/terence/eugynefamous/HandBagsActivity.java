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

public class HandBagsActivity extends AppCompatActivity {

    private RecyclerView HandBagsList;

    private ImageView backBtn, searchBtn;
    private ProgressBar progressBar;

    private Button nextBtn;

    //getting intent
    private String type = "";

    //firebase variables
    private DatabaseReference bagsRefe;
   private FirebaseRecyclerAdapter<products, ProductViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_bags);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("handbagsType").toString();

        }

        HandBagsList = findViewById(R.id._handbagslist_);
        HandBagsList.setLayoutManager(new LinearLayoutManager(this));

        backBtn = findViewById(R.id.handbg_back);

        searchBtn = findViewById(R.id.handbags_search);
        nextBtn = findViewById(R.id.next_handbag);

        progressBar = findViewById(R.id.progress_bar_handbags);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HandBagsActivity.super.onBackPressed();

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HandBagsActivity.this, Page2Activity.class);

                intent.putExtra("sex", "women");
                intent.putExtra("category", "bags");
                intent.putExtra("nextType", type);

                startActivity(intent);

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HandBagsActivity.this, SearchProductsActivity.class);

                intent.putExtra("sex", "women");
                intent.putExtra("category", "bags");

                startActivity(intent);




            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        HandBagsAsyncTask task = new HandBagsAsyncTask(this);
        task.execute(3);




    }


    //async Task class
    private  class HandBagsAsyncTask extends AsyncTask<Integer, Integer, String>{

        //this is to avoid memory leaks
        //so we introduce a weak reference
        private WeakReference<HandBagsActivity> activityWeakReference;

        public HandBagsAsyncTask(HandBagsActivity activity) {

            activityWeakReference = new WeakReference<HandBagsActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           HandBagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()){

                return;

            }

            HandBagsList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {
                //this calls the on progress
                //on progress will use this values
                publishProgress( (i * 100) / integers[0] );

                if (i == 1){

                    bagsRefe = FirebaseDatabase.getInstance().getReference().child("bags").child("women");

                    FirebaseRecyclerOptions<products> Bagsoptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(bagsRefe.orderByChild("priority").startAt("1").endAt("1"), products.class)//the category shows what you want to show in the recycler view here we are searching using the pname
//                .setQuery(shirtsRef.orderByChild("priority").startAt("1").endAt("1"), products.class)
                            .build();


                    adapter =
                            new FirebaseRecyclerAdapter<products, ProductViewHolder>(Bagsoptions) {
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


                                                Intent intent = new Intent(HandBagsActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                            else {

                                                Intent intent = new Intent(HandBagsActivity.this, ProductDetailsActivity.class);

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

                //this freezes the thread for one second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            //this executes after the loop is finished

            return "updated";

        }



        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            HandBagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()){

                return;

            }

            activity.progressBar.setProgress(values[0]);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            HandBagsActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()){

                return;

            }

            HandBagsList.setAdapter(adapter);
            adapter.startListening();

            HandBagsList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            //s is the return value in do in background
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();



        }


    }


}