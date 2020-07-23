package quinton.terence.eugynefamous.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import quinton.terence.eugynefamous.R;
import quinton.terence.eugynefamous.itemClickListener;

public class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity, txtProductSize, txtProductColor;
    private itemClickListener itemClickListener;

    public cartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductSize = itemView.findViewById(R.id.cart_product_size);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        txtProductColor = itemView.findViewById(R.id.cart_product_color);


    }


    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }


    public void setItemClickListener(quinton.terence.eugynefamous.itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
