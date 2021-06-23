package za.co.garycampbell.mysticbrewuser.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.ServicesModel;
import za.co.garycampbell.mysticbrewuser.models.VendorModel;
import za.co.garycampbell.mysticbrewuser.user_activities.ProductDetailsActivity;
import za.co.garycampbell.mysticbrewuser.user_activities.ServiceDetailsActivity;
import za.co.garycampbell.mysticbrewuser.user_activities.VendorDetailsActivity;

public class VendorDashboardServicesFragment extends Fragment {

    private View productsFragmentView;
    private RecyclerView productsRecyclerView;
    FirebaseDatabase database;
    DatabaseReference productsRef;
    FirebaseAuth mAuth;

    String businessID, productID;

    public VendorDashboardServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle results = VendorDetailsActivity.getMyData();
        businessID = results.getString("businessID");

        // Inflate the layout for this fragment
        productsFragmentView = inflater.inflate(R.layout.fragment_vendor_dashboard_services, container, false);
        productsRecyclerView = productsFragmentView.findViewById(R.id.vendor_dashboard_services_recycler_view);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("vendors");
        mAuth = FirebaseAuth.getInstance();
        return productsFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    VendorModel product = snapshot.getValue(VendorModel.class);
                    if (product != null) {
                        if (businessID != null) {
                            FirebaseRecyclerOptions<ServicesModel> productOptions = new FirebaseRecyclerOptions.Builder<ServicesModel>()
                                    .setQuery(productsRef.child(businessID).child("service"), ServicesModel.class)
                                    .build();
                            FirebaseRecyclerAdapter<ServicesModel, VendorDashboardServicesFragment.VendorDetailsProductViewHolder> adapter = new FirebaseRecyclerAdapter<ServicesModel, VendorDashboardServicesFragment.VendorDetailsProductViewHolder>(productOptions) {
                                @Override
                                protected void onBindViewHolder(@NonNull VendorDashboardServicesFragment.VendorDetailsProductViewHolder holder, int position, @NonNull ServicesModel model) {

                                    productsRef.child(businessID).child("service").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            String productName = snapshot.child("productName").getValue().toString();
//                                            String productCategory = snapshot.child("productCategory").getValue().toString();
//                                            String productDescription = snapshot.child("productShortDescription").getValue().toString();
//                                            String productQuantity = snapshot.child("productQuantity").getValue().toString();
//                                            String productPrice = snapshot.child("productPrice").getValue().toString();

                                            holder.productName.setText(model.getName());
                                            holder.productCategory.setText(model.getCategory());
                                            holder.productLongDescription.setText(model.getLongDescription());
                                            holder.productPrice.setText(model.getPrice());
                                            Picasso.get().load(model.getImageUrl()).into(holder.productImage);
                                            productID = model.getiD();

                                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    sendData();
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
                                public VendorDashboardServicesFragment.VendorDetailsProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View v;
                                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_details_product_item_view, parent, false);
                                    return new VendorDashboardServicesFragment.VendorDetailsProductViewHolder(v);
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
    public static VendorDashboardServicesFragment newInstance(String businessID) {
        VendorDashboardServicesFragment fragment = new VendorDashboardServicesFragment();
        Bundle args = new Bundle();
        args.putString("businessID", businessID);
        fragment.setArguments(args);
        return fragment;
    }

    private void sendData()
    {
        //INTENT OBJ
        Intent i = new Intent(getActivity(), ServiceDetailsActivity.class);
        //PACK DATA
        i.putExtra("selectedProduct", productID);
        i.putExtra("vendorID", businessID);
        Objects.requireNonNull(getActivity()).startActivity(i);
    }
}