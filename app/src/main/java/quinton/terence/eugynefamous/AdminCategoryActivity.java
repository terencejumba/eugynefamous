package quinton.terence.eugynefamous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirts,shirts, shoes, socks, belt,hats,shorts,bags, dresses,trousers;

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

    }
}