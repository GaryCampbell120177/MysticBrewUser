package za.co.garycampbell.mysticbrewuser.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import za.co.garycampbell.mysticbrewuser.R;

public class UserHomeCategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView categoryView;

    public UserHomeCategoryViewHolder(@NonNull View itemView)
    {
        super(itemView);

        categoryView = itemView.findViewById(R.id.category_view_category_category);
    }
}
