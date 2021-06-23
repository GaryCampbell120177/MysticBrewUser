package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class VendorViewHolder extends RecyclerView.ViewHolder {

    public ImageView vendorImage;
    public TextView vendorName, vendorCategory, vendorContact;

    public VendorViewHolder(@NonNull View itemView)
    {
        super(itemView);

        vendorImage = itemView.findViewById(R.id.vendor_image);
        vendorName = itemView.findViewById(R.id.vendor_name);
        vendorContact = itemView.findViewById(R.id.vendor_contact);
    }
}
