package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import za.co.garycampbell.mysticbrewuser.R;

public class ServiceViewHolder extends RecyclerView.ViewHolder{

    public ImageView serviceImage;
    public TextView serviceName, serviceCategory, servicePrice;

    public ServiceViewHolder(@NonNull View itemView) {
        super(itemView);

        serviceImage = itemView.findViewById(R.id.service_logo_image);
        serviceName = itemView.findViewById(R.id.service_name);
        servicePrice = itemView.findViewById(R.id.service_price);
    }
}
