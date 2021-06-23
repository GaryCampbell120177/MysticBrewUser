package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class UserProductsViewHolder extends RecyclerView.ViewHolder {

    public TextView productName, productPrice,  productQuantity;
    public ImageView productImage;

    public UserProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.vendor_orders_product_name);
        productPrice = itemView.findViewById(R.id.vendor_orders_product_price);
        productImage = itemView.findViewById(R.id.vendor_orders_product_image);

    }
}
