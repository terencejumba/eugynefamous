package quinton.terence.eugynefamous.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import quinton.terence.eugynefamous.R;
import quinton.terence.eugynefamous.itemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;

    //variable for accessing our interface
    public itemClickListener listener;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.home_img);
        txtProductName = itemView.findViewById(R.id.home_title);
        txtProductDescription = itemView.findViewById(R.id.home_description);
        txtProductPrice = itemView.findViewById(R.id.home_price);


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
