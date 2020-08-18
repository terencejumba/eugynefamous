package quinton.terence.eugynefamous.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import quinton.terence.eugynefamous.Model.AdminOrders;
import quinton.terence.eugynefamous.R;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView OrdersList;

    private DatabaseReference ordersRefer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        //removing the top status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hides the action bar
        getSupportActionBar().hide();

        ordersRefer = FirebaseDatabase.getInstance().getReference().child("Orders");

        OrdersList = findViewById(R.id.orders_list);
        OrdersList.setLayoutManager(new LinearLayoutManager(this));




    }


    @Override
    protected void onStart() {
        super.onStart();

        //for the recycler view
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRefer, AdminOrders.class)
                .build();


        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {


                        holder.username.setText("Name: " + model.getName());
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount: " + model.getTotalAmount());
                        holder.UserShippingAddress.setText("Shipping Address: " + model.getAddress() + "  " + model.getHome());
                        holder.userMpesaPhone.setText("Pay phone: " + model.getMpesaPhone());
                        holder.userMethod.setText("Pay Method: " + model.getMethod());
                        holder.userDateTime.setText("Order At: " + model.getDate() + "  " + model.getTime());

                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //getting the id
                                String uID  =  getRef(position).getKey();


                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);

                                intent.putExtra("uid", uID);

                                startActivity(intent);



                            }
                        });


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //creating a dialog box
                                //with two options

                                CharSequence options[]  = new CharSequence[]{

                                  "Yes",

                                  "No"


                                };


                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);

                                builder.setTitle("Have you shipped this order products ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        if (which == 0){

                                            String uID  =  getRef(position).getKey();

                                            RemoveOrder(uID);

                                        }

                                        else {

                                            finish();

                                        }

                                    }
                                });

                                builder.show();
                            }
                        });



                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);

                        return new AdminOrdersViewHolder(view);

                    }
                };

        OrdersList.setAdapter(adapter);
        adapter.startListening();

    }




    //creating a static class or the viewholder for our recycler view

    public static  class  AdminOrdersViewHolder extends  RecyclerView.ViewHolder{

        public TextView  username, userPhoneNumber, userTotalPrice, userDateTime, UserShippingAddress, userMpesaPhone, userMethod;
        public Button showOrdersBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {

            super(itemView);


            username = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            UserShippingAddress = itemView.findViewById(R.id.order_address_city);
            userMpesaPhone= itemView.findViewById(R.id.order_pay_phone);
            userMethod = itemView.findViewById(R.id.order_pay_method);
            showOrdersBtn = itemView.findViewById(R.id.show_all_productsBtn);


        }
    }


    private void RemoveOrder(String uID) {

        ordersRefer.child(uID).removeValue();


    }

}