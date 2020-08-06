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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import quinton.terence.eugynefamous.Model.products;
import quinton.terence.eugynefamous.ViewHolder.ProductViewHolder;
import quinton.terence.eugynefamous.ViewHolder.StartProductViewHolder;
import quinton.terence.eugynefamous.common.LogInActivity;
import quinton.terence.eugynefamous.prevalent.prevalent;
import quinton.terence.eugynefamous.productsdetails.ProductDetailsActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variable for the navigation drawer
    static final float End_SCALE = 0.7f;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuicon;
    private RelativeLayout contenttView;
    private FloatingActionButton cartbtn;


    //variable for database
    DatabaseReference shatiref, shoesRef,  ShortsRef, TrousersRef,  tShirtsRef;

    //populating our recyclerview with data
    private RecyclerView recyclerView, shoesList,  trouserList, tshirtsList, shortsList;
    RecyclerView.LayoutManager layoutManager;

    private TextView shirtsShowMore, shoesShowMore, shortShowMore, trouserShowMore, tShirtShowMore;



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
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        shoesList = findViewById(R.id.shoes_recycler);
        shoesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        trouserList = findViewById(R.id.trouser_recycler);
        trouserList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        tshirtsList = findViewById(R.id.tshirts_recycler);
        tshirtsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        shortsList = findViewById(R.id.short_recycler);
        shortsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        shirtsShowMore = findViewById(R.id.shirts_more_);
        shoesShowMore = findViewById(R.id.shoes_more_);
        trouserShowMore = findViewById(R.id.trouser_more_);
        tShirtShowMore = findViewById(R.id.tshirts_more_);
        shortShowMore = findViewById(R.id.shorts_more_);



        //hooks for database
        shatiref = FirebaseDatabase.getInstance().getReference().child("shirts").child("men");
        shoesRef = FirebaseDatabase.getInstance().getReference().child("shoes").child("men");
        ShortsRef = FirebaseDatabase.getInstance().getReference().child("shorts").child("men");
        TrousersRef = FirebaseDatabase.getInstance().getReference().child("trousers").child("men");
        tShirtsRef = FirebaseDatabase.getInstance().getReference().child("tShirts").child("men");


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

        shirtsShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ShirtsActivity.class));

                finish();


            }
        });

        shoesShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ShoesActivity.class));

                finish();


            }
        });

        trouserShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, TrousersActivity.class));

                finish();


            }
        });

        tShirtShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, tShirtsActivity.class));

                finish();


            }
        });

        shortShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ShortsActivity.class));

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

                final float diffScaledOffset = slideOffset * (1 - End_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contenttView.setScaleX(offsetScale);
                contenttView.setScaleY(offsetScale);

                //translate the accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contenttView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contenttView.setTranslationX(xTranslation);


            }

        });

    }


    //for closing the drawer when pressing back button
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        } else

            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {


            case R.id.nav_logout:

                Paper.book().destroy();

                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);



                break;

            case R.id.nav_settingsbin:

                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));



                break;

            case R.id.nav_cart:

                startActivity(new Intent(HomeActivity.this, CartActivity.class));



                break;
                
            case R.id.nav_tshirts:

                startActivity(new Intent(HomeActivity.this, tShirtsActivity.class));



                break;

            case R.id.nav_shirt:

                startActivity(new Intent(HomeActivity.this, ShirtsActivity.class));



                break;

            case R.id.nav_shoes:

                startActivity(new Intent(HomeActivity.this, ShoesActivity.class));



                break;

            case R.id.nav_socks:

                startActivity(new Intent(HomeActivity.this, SocksActivity.class));



                break;

            case R.id.nav_belts:

                startActivity(new Intent(HomeActivity.this, BeltsActivity.class));



                break;

            case  R.id.nav_hats:

                startActivity(new Intent(HomeActivity.this, HatsActivity.class));




                break;

            case R.id.nav_shorts:

                startActivity(new Intent(HomeActivity.this, ShortsActivity.class));



                break;

            case  R.id.nav_bags:

                startActivity(new Intent(HomeActivity.this, BagsActivity.class));



                break;

            case  R.id.nav_dresses:

                startActivity(new Intent(HomeActivity.this, DressesActivity.class));



                break;

            case R.id.nav_trouser:

                startActivity(new Intent(HomeActivity.this, TrousersActivity.class));


                break;

            case R.id.nav_sweaters:

                startActivity(new Intent(HomeActivity.this, SweaterActivity.class));



                break;

            case  R.id.nav_skirt:

                startActivity(new Intent(HomeActivity.this, SkirtsActivity.class));



                break;

            case  R.id.nav_lingerie:

                startActivity(new Intent(HomeActivity.this, LingerieActivity.class));



                break;

            case  R.id.nav_jumpsuits:

                startActivity(new Intent(HomeActivity.this, JumpSuitsActivity.class));



                break;

            case R.id.nav_tops:

                startActivity(new Intent(HomeActivity.this, TopsActtivity.class));




        }


        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();






        //adding a query to retrieve all products
        FirebaseRecyclerOptions<products> options =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery(shatiref, products.class)
                        .build();


        //adding a firebase recycler adapter

        FirebaseRecyclerAdapter<products, StartProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);




                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

                                startActivity(intent);


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


        recyclerView.setAdapter(adapter);
        adapter.startListening();



        //adding a query to retrieve all products
        FirebaseRecyclerOptions<products> shoeOptions =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery(shoesRef, products.class)
                        .build();


        //adding a firebase recycler adapter

        FirebaseRecyclerAdapter<products, StartProductViewHolder> shoeAdapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(shoeOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);




                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

                                startActivity(intent);


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


        shoesList.setAdapter(shoeAdapter);
        shoeAdapter.startListening();


        //adding a query to retrieve all products
        FirebaseRecyclerOptions<products> trouserOptions =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery(TrousersRef, products.class)
                        .build();


        //adding a firebase recycler adapter

        FirebaseRecyclerAdapter<products, StartProductViewHolder> trouserAdapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(trouserOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);




                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

                                startActivity(intent);


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


        trouserList.setAdapter(trouserAdapter);
        trouserAdapter.startListening();


        //adding a query to retrieve all products
        FirebaseRecyclerOptions<products> tShirtsOptions =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery(tShirtsRef, products.class)
                        .build();


        //adding a firebase recycler adapter

        FirebaseRecyclerAdapter<products, StartProductViewHolder> tShirtAdapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(tShirtsOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);




                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

                                startActivity(intent);


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


        tshirtsList.setAdapter(tShirtAdapter);
        tShirtAdapter.startListening();


        //adding a query to retrieve all products
        FirebaseRecyclerOptions<products> shortOptions =
                new FirebaseRecyclerOptions.Builder<products>()
                        .setQuery(ShortsRef, products.class)
                        .build();


        //adding a firebase recycler adapter

        FirebaseRecyclerAdapter<products, StartProductViewHolder> shortAdapter =
                new FirebaseRecyclerAdapter<products, StartProductViewHolder>(shortOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull StartProductViewHolder holder, int i, @NonNull final products model) {

                        holder.txtProductName.setText(model.getPname());

                        holder.txtProductPrice.setText(model.getPrice() + "Ksh");

                        Picasso.get().load(model.getImage()).into(holder.imageView);




                        //setting a click listener to the relative layout

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);

                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                intent.putExtra("sex", model.getSex());

                                startActivity(intent);


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


        shortsList.setAdapter(shortAdapter);
        shortAdapter.startListening();





    }
}