package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView categoryTV;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryTV = itemView.findViewById(R.id.category_view_category_category);

    }

}
