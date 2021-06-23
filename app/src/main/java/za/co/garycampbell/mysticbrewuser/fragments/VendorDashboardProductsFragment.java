package za.co.garycampbell.mysticbrewuser.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import io.paperdb.Paper;
import za.co.garycampbell.mysticbrewuser.Prevalent;
import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.ProductModel;
import za.co.garycampbell.mysticbrewuser.models.VendorModel;
import za.co.garycampbell.mysticbrewuser.user_activities.ProductDetailsActivity;
import za.co.garycampbell.mysticbrewuser.user_activities.VendorDetailsActivity;

public class VendorDashboardProductsFragment extends Fragment {

    private View productsFragmentView;
    private RecyclerView productsRecyclerView;
    FirebaseDatabase database;
    DatabaseReference productsRef;
    FirebaseAuth mAuth;

    String businessID, productID;
    String productName;
    String productCategory;
    String productDescription;
    String productQuantity;
    String productPrice;
    String productImage;



    public VendorDashboardProductsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle results = VendorDetailsActivity.getMyData();
        businessID = results.getString("businessID");

        // Inflate the layout for this fragment
        productsFragmentView = inflater.inflate(R.layout.fragment_vendor_dashboard_products, container, false);
        productsRecyclerView = productsFragmentView.findViewById(R.id.vendor_dashboard_products_recycler_view);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("vendors");
        mAuth = FirebaseAuth.getInstance();
        return productsFragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            businessID = getArguments().getString("businessID");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ProductModel product = snapshot.getValue(ProductModel.class);
                    if (product != null) {
                        if (businessID != null) {
                            FirebaseRecyclerOptions<ProductModel> productOptions = new FirebaseRecyclerOptions.Builder<ProductModel>()
                                    .setQuery(productsRef.child(businessID)
                                            .child("product"), ProductModel.class)
                                    .build();
                            FirebaseRecyclerAdapter<ProductModel, VendorDetailsProductViewHolder> adapter = new FirebaseRecyclerAdapter<ProductModel, VendorDetailsProductViewHolder>(productOptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull VendorDetailsProductViewHolder holder, int position, @NonNull ProductModel model) {

                                    productsRef.child(businessID)
                                            .child("product")
                                            .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    productName = model.getName();
                                                    productCategory = model.getCategory();
                                                    productDescription = model.getLongDescription();
                                                    productQuantity = model.getQuantity();
                                                    productPrice = model.getPrice();
                                                    productImage = model.getImageUrl();
                                                    productID = model.getiD();

                                                    holder.productName.setText(model.getName());
                                                    holder.productCategory.setText(model.getCategory());
                                                    holder.productLongDescription.setText(model.getLongDescription());
                                                    holder.productPrice.setText(model.getPrice());
                                                    Picasso.get().load(model.getImageUrl()).into(holder.productImage);



                                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            String productID = model.getiD();
                                                            sendData(productID);
                                                        }
                                                    });
                                                }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public VendorDetailsProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View v;
                                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_details_product_item_view, parent, false);
                                    return new VendorDetailsProductViewHolder(v);
                                }
                            };
                            productsRecyclerView.setAdapter(adapter);
                            adapter.startListening();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "businessID is NULL", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "ProductModel snapshot is NULL", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static class VendorDetailsProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public android.widget.TextView productName, productLongDescription, productPrice, productCategory;

        public VendorDetailsProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.vendor_details_product_logo_image);
            productName = itemView.findViewById(R.id.vendor_details_product_name);
            productLongDescription = itemView.findViewById(R.id.vendor_details_product_long_description);
            productCategory = itemView.findViewById(R.id.vendor_details_product_category);
            productPrice = itemView.findViewById(R.id.vendor_details_product_price);
        }
    }
    public static VendorDashboardProductsFragment newInstance(String businessID) {
        VendorDashboardProductsFragment fragment = new VendorDashboardProductsFragment();
        Bundle args = new Bundle();
        args.putString("businessID", businessID);
        fragment.setArguments(args);
        return fragment;
    }

    private void sendData(String productID)
    {
        //INTENT OBJ
        Intent i = new Intent(getActivity(), ProductDetailsActivity.class);
        //PACK DATA
        Paper.book().write(Prevalent.currentProductKey, productID);
        i.putExtra("selectedProduct", productID);
        i.putExtra("vendorID", businessID);
        i.putExtra("productName", productName);
        i.putExtra("productLongDescription", productDescription);
        i.putExtra("productCategory", productCategory);
        i.putExtra("productPrice", productPrice);
        i.putExtra("productQuantity", productQuantity);
        i.putExtra("imageUrl", productImage);
        Objects.requireNonNull(getActivity()).startActivity(i);
    }
}