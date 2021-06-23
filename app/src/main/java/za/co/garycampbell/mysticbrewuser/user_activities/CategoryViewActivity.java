package za.co.garycampbell.mysticbrewuser.user_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.ProductModel;
import za.co.garycampbell.mysticbrewuser.models.VendorModel;
import za.co.garycampbell.mysticbrewuser.viewholders.MainViewHolder;
import za.co.garycampbell.mysticbrewuser.viewholders.ProductViewHolder;

public class CategoryViewActivity extends AppCompatActivity {

    RecyclerView mainRecycler;
    FirebaseRecyclerAdapter<VendorModel, MainViewHolder> vendorCategoryAdapter;
    FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> productCategoryAdapter;

    RecyclerView.LayoutManager manager;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    String category, businessID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        category = getIntent().getStringExtra("category");

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();


        mainRecycler = findViewById(R.id.category_recycler);
        manager = new LinearLayoutManager(this);
        mainRecycler.setLayoutManager(manager);

        dbRef.child("vendorCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    VendorModel model = snapshot.getValue(VendorModel.class);
                    if (category.equals(model.getBusinessCategory())){
                        businessID = model.getBusinessID();
                        GetCategories(businessID);
                    }
                }
                else
                    {
                        Toast.makeText(CategoryViewActivity.this, "Snapshot Not Retrieved", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void GetCategories(String businessID) {
        //        Query query = dbRef
//                .orderByChild("vendorCategories")
//                .startAt("A")
//                .getRef();

        FirebaseRecyclerOptions<VendorModel> mainOptions = new FirebaseRecyclerOptions.Builder<VendorModel>()
                .setQuery(dbRef.child("vendors").child(businessID).startAt(category).endAt(category), VendorModel.class)
                .build();
        vendorCategoryAdapter = new FirebaseRecyclerAdapter<VendorModel, MainViewHolder>(mainOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MainViewHolder mainHolder, int position, @NonNull VendorModel model)
            {
                mainHolder.vendorTitle.setText(model.getBusinessName());
                mainHolder.vendorContact.setText(model.getBusinessContactNumber());
                Picasso.get().load(model.getVendorImageUrl()).into(mainHolder.vendorImage);

                mainHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent goToVendorDetails = new Intent(CategoryViewActivity.this, VendorDetailsActivity.class);
                        goToVendorDetails.putExtra("businessID", model.getBusinessID());
                        goToVendorDetails.putExtra("businessName", model.getBusinessName());
                        goToVendorDetails.putExtra("businessAddress", model.getBusinessAddress());
                        goToVendorDetails.putExtra("businessContactNumber", model.getBusinessContactNumber());
                        goToVendorDetails.putExtra("businessEmail", model.getBusinessEmail());
                        goToVendorDetails.putExtra("businessImage", model.getVendorImageUrl());
                        goToVendorDetails.putExtra("businessLatitude", model.getBusinessLatitude());
                        goToVendorDetails.putExtra("businessLongitude", model.getBusinessLongitude());
                        startActivity(goToVendorDetails);
                    }
                });

//                Query query = dbRef.child(model.getBusinessID())
//                        .child("product")
//                        .orderByChild("productName")
//                        .startAt("A")
//                        .getRef();

                FirebaseRecyclerOptions <ProductModel> productOptions = new FirebaseRecyclerOptions.Builder<ProductModel>()
                        .setQuery(dbRef.child("vendors").child(model.getBusinessID()).child("product"), ProductModel.class)
                        .build();
                productCategoryAdapter = new FirebaseRecyclerAdapter<ProductModel, ProductViewHolder>(productOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productHolder, int position, @NonNull ProductModel model) {
                        productHolder.productName.setText(model.getName());
                        productHolder.productPrice.setText(model.getPrice());
                        productHolder.productCategory.setText(model.getCategory());

                        productHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent goToProductDetails = new Intent(CategoryViewActivity.this, ProductDetailsActivity.class);
                                goToProductDetails.putExtra("productID", model.getiD());
                                goToProductDetails.putExtra("productImage", model.getImageUrl());
                                goToProductDetails.putExtra("productName", model.getName());
                                goToProductDetails.putExtra("productLongDescription", model.getLongDescription());
                                goToProductDetails.putExtra("productCategory", model.getCategory());
                                goToProductDetails.putExtra("productPrice", model.getPrice());
                                goToProductDetails.putExtra("productSizesSmall", model.getSmallSizeQuantity());
                                goToProductDetails.putExtra("productSizesMedium", model.getMediumSizeQuantity());
                                goToProductDetails.putExtra("productSizesLarge", model.getLargeSizeQuantity());
                                goToProductDetails.putExtra("productSizesXL", model.getXlSizeQuantity());
                                goToProductDetails.putExtra("productSizesXXL", model.getXxlSizeQuantity());
                                goToProductDetails.putExtra("productSizesXXXL", model.getXxxlSizeQuantity());
                                goToProductDetails.putExtra("productQuantity", model.getQuantity());
                                goToProductDetails.putExtra("businessID", model.getVendorID());
                                startActivity(goToProductDetails);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View productView = LayoutInflater.from(getBaseContext())
                                .inflate(R.layout.product_item_view, parent, false);
                        return new ProductViewHolder(productView);
                    }
                };
                productCategoryAdapter.startListening();
                productCategoryAdapter.notifyDataSetChanged();
                mainHolder.productRecycler.setAdapter(productCategoryAdapter);
            }

            @NonNull
            @Override
            public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View mainView = LayoutInflater.from(getBaseContext())
                        .inflate(R.layout.main_recycler, parent, false);
                return new MainViewHolder(mainView);
            }
        };
        vendorCategoryAdapter.startListening();
        vendorCategoryAdapter.notifyDataSetChanged();
        mainRecycler.setAdapter(vendorCategoryAdapter);
    }
}