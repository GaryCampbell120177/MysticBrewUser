package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class VendorDetailsProductViewHolder extends RecyclerView.ViewHolder {
    public ImageView productImage;
    public TextView productName, productLongDescription, productPrice, productCategory;

    public VendorDetailsProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.vendor_details_product_logo_image);
        productName = itemView.findViewById(R.id.vendor_details_product_name);
        productLongDescription = itemView.findViewById(R.id.vendor_details_product_long_description);
        productCategory = itemView.findViewById(R.id.vendor_details_product_category);
        productPrice = itemView.findViewById(R.id.vendor_details_product_price);
    }
}
