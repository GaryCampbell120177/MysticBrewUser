package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView productName, productPrice, productCategory;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productCategory = itemView.findViewById(R.id.product_category);
        productName = itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
    }
}
