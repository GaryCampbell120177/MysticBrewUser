package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class MainViewHolder extends RecyclerView.ViewHolder {

    public TextView vendorTitle, vendorContact,vendorCategory;
    public ImageView vendorImage;
    public RecyclerView productRecycler;
    RecyclerView.LayoutManager layoutManager;
    public RecyclerView serviceRecycler;
    RecyclerView.LayoutManager servicesLayoutManager;

    public MainViewHolder(@NonNull View itemView) {
        super(itemView);
        layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        vendorImage = itemView.findViewById(R.id.vendor_image);
        vendorTitle = itemView.findViewById(R.id.vendor_name);
        vendorContact = itemView.findViewById(R.id.vendor_contact);
        vendorCategory = itemView.findViewById(R.id.vendor_category);
    }
}
