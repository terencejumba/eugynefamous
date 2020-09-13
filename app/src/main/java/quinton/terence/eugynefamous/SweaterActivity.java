package quinton.terence.eugynefamous;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class SweaterActivity extends AppCompatActivity {

    private RecyclerView sweatersList, womenList;

    private TextView showMore, womenShowMore;

    private ProgressBar progressBar;

    //getting intent
    private String type = "";

    //firebase variables

    DatabaseReference sweatersRef, womensweatersRef;
    FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter, womenadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweater);
        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){

            type = getIntent().getExtras().get("sweaterHome").toString();

        }

        sweatersList = findViewById(R.id.sweatersslist);
        sweatersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        womenList = findViewById(R.id.womensweatersslist);
        womenList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        showMore = findViewById(R.id.mensweat_ers_more);
        womenShowMore = findViewById(R.id.womensweat_ers_more);
        progressBar = findViewById(R.id.progress_bar_sweater);

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(SweaterActivity.this, menSweaterActivity.class).putExtra("MensweaterType", type));

            }
        });

        womenShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SweaterActivity.this, womenSweaterActivity.class).putExtra("WomensweaterType", type));


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();


        SweaterAsyncTask task = new SweaterAsyncTask(this);
        task.execute(1);

    }

    private class SweaterAsyncTask extends AsyncTask<Integer, Integer, String> {

        private WeakReference<SweaterActivity> activityWeakReference;


        public SweaterAsyncTask(SweaterActivity activity) {

            activityWeakReference = new WeakReference<SweaterActivity>(activity);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            SweaterActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            womenList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(activity, "loading...", Toast.LENGTH_SHORT).show();


        }


        @Override
        protected String doInBackground(Integer... integers) {

            for (int i = 0; i < integers[0]; i++) {

                publishProgress((i * 100) / integers[0]);

                if (i == 1) {

                    sweatersRef = FirebaseDatabase.getInstance().getReference().child("sweaters").child("men");

                    FirebaseRecyclerOptions<products> options = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(sweatersRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
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


                                                Intent intent = new Intent(SweaterActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                            else{

                                                Intent intent = new Intent(SweaterActivity.this, ProductDetailsActivity.class);

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


                    womensweatersRef = FirebaseDatabase.getInstance().getReference().child("sweaters").child("women");

                    FirebaseRecyclerOptions<products> womenoptions = new
                            FirebaseRecyclerOptions.Builder<products>()
                            .setQuery(womensweatersRef.limitToFirst(5), products.class)  //the category shows what you want to show in the recycler view here we are searching using the pname
                            .build();


                    womenadapter =
                            new FirebaseRecyclerAdapter<products, StartProductViewHolder>(womenoptions) {
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


                                                Intent intent = new Intent(SweaterActivity.this, AdminMaintainProductsActivity.class);

                                                intent.putExtra("pid", model.getPid());
                                                intent.putExtra("category", model.getCategory());
                                                intent.putExtra("sex", model.getSex());

                                                startActivity(intent);


                                            }

                                            else {

                                                Intent intent = new Intent(SweaterActivity.this, ProductDetailsActivity.class);

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

            SweaterActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            SweaterActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {

                return;

            }

            sweatersList.setAdapter(adapter);
            adapter.startListening();

            womenList.setAdapter(womenadapter);
            womenadapter.startListening();

            sweatersList.setVisibility(View.VISIBLE);
            womenList.setVisibility(View.VISIBLE);

            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();



        }

    }

}