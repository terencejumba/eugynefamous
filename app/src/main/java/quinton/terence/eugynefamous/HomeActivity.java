package quinton.terence.eugynefamous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.ViewHolder.ProductViewHolder;
import quinton.terence.eugynefamous.common.LogInActivity;
import quinton.terence.eugynefamous.prevalent.prevalent;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variablle for the navigation drawer
    static final float End_SCALE = 0.7f;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuicon;
    private RelativeLayout  contenttView;
    private FloatingActionButton cartbtn;


    //variable for database
    DatabaseReference pRoductsref;

    //populating our recyclerview with data
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        cartbtn = findViewById(R.id.cart_floating);

        //hooks for the drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuicon = findViewById(R.id.menuicon);
        contenttView = findViewById(R.id.content_view);

        recyclerView = findViewById(R.id.recyclermenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //hooks for database
        pRoductsref = FirebaseDatabase.getInstance().getReference().child("Products");


        //initializing header view
        //for drawer menu
        View headerview = navigationView.getHeaderView(0);
        TextView userNameTextView = headerview.findViewById(R.id.nav_username);
        CircleImageView profileImageView = headerview.findViewById(R.id.user_profile_image);


        //for setting the name to the text view
        userNameTextView.setText(prevalent.currentOnlineUser.getUsername());

        //for setting the image view to our drawer layout
        Picasso.get().load(prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.usericon).into(profileImageView);


        Paper.init(this);

        //for opening the user drawer
        navigationDrawer();



        //cart button
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, CartActivity.class));

                finish();


            }
        });


    }

    //navigation drawer methods

    private void navigationDrawer() {

        //navigation drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        //if you wnt a scrim color
        //drawerLayout.setScrimColor(getResources().getColor(R.color.colorminor));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //scale the view based on the current scale offset

                final float diffScaledOffset = slideOffset *(1 - End_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contenttView.setScaleX(offsetScale);
                contenttView.setScaleY(offsetScale);

                //translate the accounting for the scaled width
                final  float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contenttView.getWidth() * diffScaledOffset/2;
                final float xTranslation = xOffset - xOffsetDiff;
                contenttView.setTranslationX(xTranslation);


            }

        });

    }


    //for closing the drawer when pressing back button
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);

        }
        else

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){


            case  R.id.nav_logout:

                Paper.book().destroy();

              Intent intent =  new Intent(getApplicationContext(), LogInActivity.class);

              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );

                startActivity(intent);

                finish();

                break;

            case R.id.nav_settingsbin:

                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));

                finish();

                break;

            case R.id.nav_cart:

                startActivity(new Intent(HomeActivity.this, CartActivity.class));

                finish();

        }


        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        //adding a query to retrieve all products
        FirebaseRecyclerOptions<products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                .setQuery(pRoductsref, products.class)
                .build();


        //adding a firebase recycler adapter

        FirebaseRecyclerAdapter<products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(model.getPrice() + "Ksh" );

                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());

                                startActivity(intent);




                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent, false);

                        ProductViewHolder holder = new ProductViewHolder(view);

                                return holder;


                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}