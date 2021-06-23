package za.co.garycampbell.mysticbrewuser.user_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;
import za.co.garycampbell.mysticbrewuser.Prevalent;
import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.ProductModel;
import za.co.garycampbell.mysticbrewuser.models.ServicesModel;
import za.co.garycampbell.mysticbrewuser.models.VendorModel;


public class ServiceDetailsActivity extends AppCompatActivity {

    ImageView serviceDetailsServiceImage;

    TextView
            serviceDetailsServiceName,
            serviceDetailsServiceLongDescription,
            serviceDetailsServicePrice,
            serviceDetailsServiceCategory,
            serviceDetailsServiceQuantity;

    AppCompatButton
            addToCartButton,
            goToVendorButton,
            goToHomeButton;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    String serviceID;
    String serviceImage;
    String serviceName;
    String serviceLongDescription;
    String serviceCategory;
    String servicePrice;
    String businessID;
    String saveCurrentDate;
    String saveCurrentTime;
    String orderID;
    String productID;
    String _productID;
    String productImage;
    String _productImage;
    String productName;
    String _productName;
    String productLongDescription;
    String _productLongDescription;
    String productCategory;
    String _productCategory;
    String productPrice;
    String _productPrice;
    String productSizesSmall;
    String _productSizesSmall;
    String productSizesMedium;
    String _productSizesMedium;
    String productSizesLarge;
    String _productSizesLarge;
    String productSizesXL;
    String _productSizesXL;
    String productSizesXXL;
    String _productSizesXXL;
    String productSizesXXXL;
    String _productSizesXXXL;
    String productQuantity;
    String _productQuantity;
    String productCartQuantity;
    String _productCartQuantity;
    String _businessID;
    String userID;
    String productSelected;
    String vendorID;
    String userName, userMobile, userEmail, userAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        Intent getProductData = getIntent();
        productID = getProductData.getStringExtra("productID");
        productName = getProductData.getStringExtra("productName");
        productLongDescription = getProductData.getStringExtra("productLongDescription");
        productCategory = getProductData.getStringExtra("productCategory");
        productPrice = getProductData.getStringExtra("productPrice");
        businessID = getProductData.getStringExtra("businessID");
        productImage = getProductData.getStringExtra("imageUrl");

        serviceDetailsServiceImage = findViewById(R.id.service_view_service_image);
        serviceDetailsServiceName = findViewById(R.id.service_view_service_name);
        serviceDetailsServiceLongDescription = findViewById(R.id.service_view_service_long_description);
        serviceDetailsServicePrice = findViewById(R.id.service_view_service_Price);
        serviceDetailsServiceCategory = findViewById(R.id.service_view_service_category);

        addToCartButton = findViewById(R.id.add_to_cart);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductToCart();
            }
        });

        goToHomeButton = findViewById(R.id.go_to_home_button);
        goToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(ServiceDetailsActivity.this, UserHomeActivity.class);
                startActivity(goHome);
                finish();
            }
        });

        goToVendorButton = findViewById(R.id.go_to_vendor_button);
        goToVendorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _businessID = getIntent().getStringExtra("businessID");
                dbRef.child("vendors").child(_businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            VendorModel model = snapshot.getValue(VendorModel.class);
                            Intent goToVendorDetails = new Intent(ServiceDetailsActivity.this, VendorDetailsActivity.class);
                            goToVendorDetails.putExtra("businessID", model.getBusinessID());
                            goToVendorDetails.putExtra("businessName", model.getBusinessName());
                            goToVendorDetails.putExtra("businessAddress", model.getBusinessAddress());
                            goToVendorDetails.putExtra("businessContactNumber", model.getBusinessContactNumber());
                            goToVendorDetails.putExtra("businessEmail", model.getBusinessEmail());
                            goToVendorDetails.putExtra("businessImage", model.getVendorImageUrl());
                            goToVendorDetails.putExtra("businessLatitude", model.getBusinessLatitude());
                            goToVendorDetails.putExtra("businessLongitude", model.getBusinessLongitude());
                            startActivity(goToVendorDetails);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ServiceDetailsActivity.this, "Snapshot Not Retrieved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        receiveData();
        GetUserDetails();
        GetProductDetails();
    }


    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        productSelected = i.getStringExtra("selectedProduct");
        vendorID = i.getStringExtra("vendorID");
    }

    public void GetUserDetails()
    {
        userID = mAuth.getUid();
        if (userID != null) {
            dbRef.child("users").child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        userName = snapshot.child("userName").getValue().toString();
                        userMobile = snapshot.child("userMobile").getValue().toString();
                        userEmail = snapshot.child("userEmail").getValue().toString();
                        userAddress = snapshot.child("userAddress").getValue().toString();
                    }
//                            UserModel userData = snapshot.getValue(UserModel.class);
//                            if (userData != null) {
//                                if (userData.getUserName() != null) {
//                                    userName = userData.getUserName();
//                                    userMobile = userData.getUserMobile();
//                                    userEmail = userData.getUserEmail();
//                                    userAddress = userData.getUserAddress();
//                                }
//                            } else {
//                                Toast.makeText(ProductDetailsActivity.this, "There is no snapshot for UserModel", Toast.LENGTH_SHORT).show();
//                            }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void GetProductDetails() {
        if (productSelected != null && vendorID != null) {
            dbRef.child("vendors").child(vendorID).child("service").child(productSelected).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ServicesModel product = snapshot.getValue(ServicesModel.class);
                        _productID = product.getVendorID();
                        _businessID = product.getVendorID();
                        _productName = product.getName();
                        _productImage = product.getImageUrl();
                        _productLongDescription = product.getLongDescription();
                        _productCategory = product.getCategory();
                        _productPrice = product.getPrice();
                        _productQuantity = product.getQuantity();
                        serviceDetailsServiceName.setText(product.getName());
                        serviceDetailsServiceLongDescription.setText(product.getLongDescription());
                        serviceDetailsServiceCategory.setText(product.getCategory());
                        serviceDetailsServicePrice.setText(product.getPrice());
                        Picasso.get().load(product.getImageUrl()).into(serviceDetailsServiceImage);
                    }
                    else {
                        Toast.makeText(ServiceDetailsActivity.this, "There is no Snapshot for this Database", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Paper.book().write(Prevalent.currentProductKey, productID);
            serviceDetailsServiceName.setText(productName);
            serviceDetailsServiceLongDescription.setText(productLongDescription);
            serviceDetailsServiceCategory.setText(productCategory);
            serviceDetailsServicePrice.setText(productPrice);
            Picasso.get().load(productImage).into(serviceDetailsServiceImage);
        }
    }

    private void AddProductToCart()
    {
        userID = mAuth.getUid();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM_dd_yyyy");
        saveCurrentDate = currentDateFormat.format(calendar.getTime());
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH_mm_ss_");
        saveCurrentTime = currentTimeFormat.format(calendar.getTime());
        productID = getIntent().getStringExtra("productID");
        orderID = saveCurrentTime + saveCurrentDate + productID;
        Paper.book().write(Prevalent.currentCartID, orderID);
        String getProductCartQuantity = "1";
        HashMap<String, Object> cartDataMap = new HashMap<>();
        cartDataMap.put("a", "a");
        cartDataMap.put("businessID", businessID);
        cartDataMap.put("userID", userID);
        cartDataMap.put("productQuantity", getProductCartQuantity);
        cartDataMap.put("imageUrl", productImage);
        cartDataMap.put("productID", _productID);
        cartDataMap.put("productName", productName);
        cartDataMap.put("orderID", orderID);
        cartDataMap.put("productPrice", productPrice);
        cartDataMap.put("productCategory", productCategory);
        cartDataMap.put("hasPaid", "false");
        cartDataMap.put("userName", userName);
        cartDataMap.put("userEmail", userEmail);
        cartDataMap.put("userMobile", userMobile);
        cartDataMap.put("userAddress", userAddress);
        cartDataMap.put("orderStatus", "In Cart");
        cartDataMap.put("paidStatusVerified", "Verification Required");
        cartDataMap.put("verificationID", "#0101201230");
        cartDataMap.put("onHold", "true");
        cartDataMap.put("onHoldReason", "awaiting Payment");
        if(_productID != null)
        {
            String tempProductKey = Paper.book().read(Prevalent.currentProductKey);
            Paper.book().write(Prevalent.currentProductKey, _productID);
        }
        dbRef.child("cart")
                .child("users")
                .child(userID)
                .child("orders")
                .child(orderID)
                .updateChildren(cartDataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> vendorDataMap = new HashMap<>();
                            vendorDataMap.put("a", "a");
                            vendorDataMap.put("businessID", businessID);
                            vendorDataMap.put("userID", userID);
                            vendorDataMap.put("productQuantity", getProductCartQuantity);
                            vendorDataMap.put("imageUrl", productImage);
                            vendorDataMap.put("productID", _productID);
                            vendorDataMap.put("productName", productName);
                            vendorDataMap.put("orderID", orderID);
                            vendorDataMap.put("productPrice", productPrice);
                            vendorDataMap.put("productCategory", productCategory);
                            vendorDataMap.put("hasPaid", "false");
                            vendorDataMap.put("userName", userName);
                            vendorDataMap.put("userEmail", userEmail);
                            vendorDataMap.put("userMobile", userMobile);
                            vendorDataMap.put("userAddress", userAddress);
                            vendorDataMap.put("orderStatus", "In Cart");
                            vendorDataMap.put("paidStatusVerified", "Verification Required");
                            vendorDataMap.put("verificationID", "#0101201230");
                            vendorDataMap.put("onHold", "true");
                            vendorDataMap.put("onHoldReason", "awaiting Payment");
                            if(_productID != null)
                            {
                                String tempProductKey = Paper.book().read(Prevalent.currentProductKey);
                                Paper.book().write(Prevalent.currentProductKey, _productID);
                            }
                            dbRef.child("cart")
                                    .child("vendors")
                                    .child(businessID)
                                    .child("orders")
                                    .child(orderID)
                                    .updateChildren(vendorDataMap);
                            Toast.makeText(ServiceDetailsActivity.this, "User Order Reference Created", Toast.LENGTH_SHORT).show();
                            addToCartButton.setVisibility(View.INVISIBLE);
                            goToHomeButton.setVisibility(View.VISIBLE);
                            goToVendorButton.setVisibility(View.VISIBLE);
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(ServiceDetailsActivity.this, "Product Not Added To Cart : Error : " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
