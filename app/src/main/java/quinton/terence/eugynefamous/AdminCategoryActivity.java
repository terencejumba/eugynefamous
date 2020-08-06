package quinton.terence.eugynefamous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirts,shirts, shoes, socks, belt,hats,shorts,bags, dresses,trousers, sweaters, skirts, lingerie, jumpsuits, blouse, tanks, tunics, sandals;

    private Button LogoutBtn, CheckOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        //hooks
        tshirts = findViewById(R.id.t_shirts);
        shirts = findViewById(R.id.shirts);
        shoes = findViewById(R.id.shoes);
        socks= findViewById(R.id.socks);
        belt = findViewById(R.id.belts);
        hats = findViewById(R.id.hats);
        shorts = findViewById(R.id.shorts);
        bags = findViewById(R.id.bags);
        dresses = findViewById(R.id.female_dresses);
        trousers = findViewById(R.id.trousers);
        sweaters = findViewById(R.id.sweaters);
        skirts = findViewById(R.id.skirts);
        lingerie = findViewById(R.id.lingerie);
        jumpsuits = findViewById(R.id.jumpsuits);
        blouse = findViewById(R.id.blouse);
        tanks = findViewById(R.id.tank);
        tunics = findViewById(R.id.tunic);
        sandals = findViewById(R.id.sandals);

        LogoutBtn = findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = findViewById(R.id.check_orders_btn);


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

                finish();


            }
        });



        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);



                startActivity(intent);


            }
        });




        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "tShirts");

                startActivity(intent);


            }
        });

        shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "shirts");

                startActivity(intent);


            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "shoes");

                startActivity(intent);


            }
        });


        socks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "socks");

                startActivity(intent);


            }
        });

        belt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "belts");

                startActivity(intent);


            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "hats");

                startActivity(intent);


            }
        });

        shorts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "shorts");

                startActivity(intent);


            }
        });

        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "bags");

                startActivity(intent);


            }
        });

        dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "dresses");

                startActivity(intent);


            }
        });

        trousers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "trousers");

                startActivity(intent);


            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "sweaters");

                startActivity(intent);

            }
        });

        skirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "skirts");

                startActivity(intent);


            }
        });

        lingerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "lingerie");

                startActivity(intent);
            }
        });

        jumpsuits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "jumpsuits");

                startActivity(intent);


            }
        });

        blouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "blouse");

                startActivity(intent);

            }
        });

        tanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "tanks");

                startActivity(intent);

            }
        });

        tunics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "tunics");

                startActivity(intent);

            }
        });


        sandals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);

                //passing values to the other activities
                intent.putExtra("category", "sandals");

                startActivity(intent);


            }
        });

    }
}