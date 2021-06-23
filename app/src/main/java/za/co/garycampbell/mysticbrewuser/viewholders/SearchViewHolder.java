package za.co.garycampbell.mysticbrewuser.viewholders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.ProductModel;


public class SearchViewHolder extends RecyclerView.Adapter<SearchViewHolder.MySearchViewHolder> {

    public Context c;
    public ArrayList<ProductModel> productModelArrayList;
    public SearchViewHolder(Context c, ArrayList<ProductModel> productModelArrayList)
    {
        this.c = c;
        this.productModelArrayList = productModelArrayList;
    }

    @NonNull
    @Override
    public MySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recycler, parent, false);
        return new MySearchViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySearchViewHolder holder, int position) {
        ProductModel productModel = productModelArrayList.get(position);

        holder.businessName.setText(productModel.getVendorID());
        holder.productName.setText(productModel.getName());
        holder.productShortDescription.setText(productModel.getShortDescription());
        holder.productPrice.setText(productModel.getPrice());
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class MySearchViewHolder extends RecyclerView.ViewHolder {

        public TextView businessName, productName, productShortDescription, productPrice;

        public MySearchViewHolder(@NonNull View itemView) {
            super(itemView);

            businessName = itemView.findViewById(R.id.search_view_business_name);
            productName = itemView.findViewById(R.id.search_view_product_name);
            productShortDescription = itemView.findViewById(R.id.search_view_product_short_description);
            productPrice = itemView.findViewById(R.id.search_view_product_price);
        }
    }
}
