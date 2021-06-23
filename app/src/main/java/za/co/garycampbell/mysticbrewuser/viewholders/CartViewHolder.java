package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;


public class CartViewHolder extends RecyclerView.ViewHolder {

    public TextView cartProductName;
    public TextView itemQuantity;
    public TextView cartProductPrice;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartProductName = itemView.findViewById(R.id.user_cart_product_name);
        itemQuantity = itemView.findViewById(R.id.user_cart_product_quantity);
        cartProductPrice = itemView.findViewById(R.id.user_cart_product_price);

    }
}
