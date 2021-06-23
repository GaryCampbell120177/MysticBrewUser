package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class VendorOrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView userName, userMobile, userEmail;

    public VendorOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.vendor_order_user_name);
        userMobile = itemView.findViewById(R.id.vendor_order_user_mobile);
        userEmail = itemView.findViewById(R.id.vendor_order_user_email);

    }
}
