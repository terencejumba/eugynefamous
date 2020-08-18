package quinton.terence.eugynefamous.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import quinton.terence.eugynefamous.R;
import quinton.terence.eugynefamous.Interface.itemClickListener;

public class StartProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;

    //variable for accessing our interface
    public itemClickListener listener;



    public StartProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.start_product_img);
        txtProductName = itemView.findViewById(R.id.start_product_title);

        txtProductPrice = itemView.findViewById(R.id.start_product_price);


    }

    //setting the item click listener using our created interface

    public void setItemClickListener(itemClickListener listener){

        this.listener = listener;

    }



    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);


    }




}
