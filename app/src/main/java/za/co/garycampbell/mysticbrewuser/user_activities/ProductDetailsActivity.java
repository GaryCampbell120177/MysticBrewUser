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
import java.util.Objects;

import io.paperdb.Paper;
import za.co.garycampbell.mysticbrewuser.Prevalent;
import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.ProductModel;
import za.co.garycampbell.mysticbrewuser.models.UserModel;
import za.co.garycampbell.mysticbrewuser.models.VendorModel;


public class ProductDetailsActivity extends AppCompatActivity {

    ImageView productDetailsProductImage;

    TextView
            productDetailsProductName,
            productDetailsProductLongDescription,
            productDetailsProductPrice,
            productDetailsProductCategory,
            ProductDetailsProductQuantity;

    MaterialCheckBox
            productDetailsXXXLCheckBox,
            productDetailsXXLCheckBox,
            productDetailsXLCheckBox,
            productDetailsLargeCheckBox,
            productDetailsMediumCheckBox,
            productDetailsSmallCheckBox;

    NumberPicker
            addToCartQuantity;

    AppCompatButton
            addToCartButton,
            goToVendorButton,
            goToHomeButton;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    String productID, _productID, productImage, _productImage, productName, _productName, productLongDescription, _productLongDescription;
    String productCategory,  _productCategory, productPrice, _productPrice , productSizesSmall, _productSizesSmall, productSizesMedium;
    String _productSizesMedium, productSizesLarge,_productSizesLarge,productSizesXL, _productSizesXL,productSizesXXL,_productSizesXXL,productSizesXXXL,_productSizesXXXL,productQuantity,_productQuantity,productCartQuantity, _productCartQuantity, businessID, _businessID,saveCurrentDate,saveCurrentTime,cartID, userID, productSelected,vendorID;
    String userName, userSurname,  userEmail, userMobile, userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        receiveData();
        userID = mAuth.getUid();


        Intent getProductData = getIntent();
        productID = getProductData.getStringExtra("productID");
        productName = getProductData.getStringExtra("productName");
        productImage = getProductData.getStringExtra("productImage");
        productLongDescription = getProductData.getStringExtra("productLongDescription");
        productCategory = getProductData.getStringExtra("productCategory");
        productPrice = getProductData.getStringExtra("productPrice");
        productSizesSmall = getProductData.getStringExtra("productSizesSmall");
        productSizesMedium = getProductData.getStringExtra("productSizesMedium");
        productSizesLarge = getProductData.getStringExtra("productSizesLarge");
        productSizesXL = getProductData.getStringExtra("productSizesXL");
        productSizesXXL = getProductData.getStringExtra("productSizesXXL");
        productSizesXXXL = getProductData.getStringExtra("productSizesXXXL");
        productQuantity = getProductData.getStringExtra("productQuantity");
        businessID = getProductData.getStringExtra("businessID");


        productDetailsProductImage = findViewById(R.id.product_view_product_image);
        productDetailsProductName = findViewById(R.id.product_view_product_name);
        productDetailsProductLongDescription = findViewById(R.id.product_view_product_long_description);
        productDetailsProductPrice = findViewById(R.id.product_view_product_Price);
        productDetailsProductCategory = findViewById(R.id.product_view_product_category);
        ProductDetailsProductQuantity = findViewById(R.id.product_view_product_quantity);


        productDetailsXXXLCheckBox = findViewById(R.id.product_view_size_xxxl_checkbox);
        productDetailsXXLCheckBox = findViewById(R.id.product_view_size_xxl_checkbox);
        productDetailsXLCheckBox = findViewById(R.id.product_view_size_xl_checkbox);
        productDetailsLargeCheckBox = findViewById(R.id.product_view_size_large_checkbox);
        productDetailsMediumCheckBox = findViewById(R.id.product_view_size_medium_checkbox);
        productDetailsSmallCheckBox = findViewById(R.id.product_view_size_small_checkbox);

        addToCartQuantity = findViewById(R.id.product_view_add_to_cart_quantity);
        productCartQuantity = String.valueOf(addToCartQuantity.getValue());

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
                Intent goHome = new Intent(ProductDetailsActivity.this, UserHomeActivity.class);
                startActivity(goHome);
                finish();
            }
        });

        goToVendorButton = findViewById(R.id.go_to_vendor_button);
        goToVendorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vendorID != null)
                {
                    businessID = vendorID;
                }
                else
                {
                    businessID = getProductData.getStringExtra("businessID");
                }
                dbRef.child("vendors").child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            VendorModel model = snapshot.getValue(VendorModel.class);
                            Intent goToVendorDetails = new Intent(ProductDetailsActivity.this, VendorDetailsActivity.class);
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
                            Toast.makeText(ProductDetailsActivity.this, "Snapshot Not Retrieved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        GetProductDetails();
        GetUserDetails();
    }

    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        productSelected = Paper.book().read(Prevalent.currentProductKey);
        vendorID = i.getStringExtra("vendorID");
        if (productSelected != null) {

            productName = i.getStringExtra("productName");
            productImage = i.getStringExtra("imageUrl");
            productLongDescription = i.getStringExtra("productLongDescription");
            productCategory = i.getStringExtra("productCategory");
            productPrice = i.getStringExtra("productPrice");
            productSizesSmall = i.getStringExtra("productSizesSmall");
            productSizesMedium = i.getStringExtra("productSizesMedium");
            productSizesLarge = i.getStringExtra("productSizesLarge");
            productSizesXL = i.getStringExtra("productSizesXL");
            productSizesXXL = i.getStringExtra("productSizesXXL");
            productSizesXXXL = i.getStringExtra("productSizesXXXL");
            productQuantity = i.getStringExtra("productQuantity");
        }
    }


    private void GetProductDetails() {

        productSelected = Paper.book().read(Prevalent.currentProductKey);
        if(productSelected != null)
        {
            dbRef.child("vendors")
                    .child(vendorID)
                    .child("product")
                    .child(productSelected)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        if (productSelected != null) {
                            ProductModel product = snapshot.getValue(ProductModel.class);

                            _productID = product.getiD();
                            _businessID = product.getVendorID();
                            _productName = product.getName();
                            _productImage = product.getImageUrl();
                            _productLongDescription = product.getLongDescription();
                            _productCategory = product.getCategory();
                            _productPrice = product.getPrice();
                            _productSizesSmall = product.getSmallSizeQuantity();
                            _productSizesMedium = product.getMediumSizeQuantity();
                            _productSizesLarge = product.getLargeSizeQuantity();
                            _productSizesXL = product.getXlSizeQuantity();
                            _productSizesXXL = product.getXxlSizeQuantity();
                            _productSizesXXXL = product.getXxxlSizeQuantity();
                            _productQuantity = product.getQuantity();

                            productDetailsProductName.setText(product.getName());
                            productDetailsProductLongDescription.setText(product.getLongDescription());
                            productDetailsProductCategory.setText(product.getCategory());
                            productDetailsProductPrice.setText(product.getPrice());
                            ProductDetailsProductQuantity.setText(product.getQuantity());
                            Picasso.get().load(product.getImageUrl()).into(productDetailsProductImage);
                            if (product.isHasSize()) {
                                productDetailsXXXLCheckBox.setVisibility(View.VISIBLE);
                                productDetailsXXLCheckBox.setVisibility(View.VISIBLE);
                                productDetailsXLCheckBox.setVisibility(View.VISIBLE);
                                productDetailsLargeCheckBox.setVisibility(View.VISIBLE);
                                productDetailsMediumCheckBox.setVisibility(View.VISIBLE);
                                productDetailsSmallCheckBox.setVisibility(View.VISIBLE);
                            } else {
                                productDetailsXXXLCheckBox.setVisibility(View.INVISIBLE);
                                productDetailsXXLCheckBox.setVisibility(View.INVISIBLE);
                                productDetailsXLCheckBox.setVisibility(View.INVISIBLE);
                                productDetailsLargeCheckBox.setVisibility(View.INVISIBLE);
                                productDetailsMediumCheckBox.setVisibility(View.INVISIBLE);
                                productDetailsSmallCheckBox.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            productDetailsProductName.setText(productName);
                            productDetailsProductLongDescription.setText(productLongDescription);
                            productDetailsProductCategory.setText(productCategory);
                            productDetailsProductPrice.setText(productPrice);
                            ProductDetailsProductQuantity.setText(productQuantity);

                            addToCartQuantity.setMinValue(1);
                            addToCartQuantity.setMaxValue(Integer.parseInt(productQuantity));
                            Picasso.get().load(productImage).into(productDetailsProductImage);
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Intent i = getIntent();
            vendorID = i.getStringExtra("vendorID");
            productName = i.getStringExtra("productName");
            productImage = i.getStringExtra("productImage");
            productLongDescription = i.getStringExtra("productLongDescription");
            productCategory = i.getStringExtra("productCategory");
            productPrice = i.getStringExtra("productPrice");
            productSizesSmall = i.getStringExtra("productSizesSmall");
            productSizesMedium = i.getStringExtra("productSizesMedium");
            productSizesLarge = i.getStringExtra("productSizesLarge");
            productSizesXL = i.getStringExtra("productSizesXL");
            productSizesXXL = i.getStringExtra("productSizesXXL");
            productSizesXXXL = i.getStringExtra("productSizesXXXL");
            productQuantity = i.getStringExtra("productQuantity");

            productDetailsProductName.setText(productName);
            productDetailsProductLongDescription.setText(productLongDescription);
            productDetailsProductCategory.setText(productCategory);
            productDetailsProductPrice.setText(productPrice);
            Picasso.get().load(productImage).into(productDetailsProductImage);

            dbRef.child("vendors")
                    .child(businessID)
                    .child("product")
                    .child(productID)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ProductModel product = snapshot.getValue(ProductModel.class);
                        if (product.isHasSize()) {
                            productDetailsXXXLCheckBox.setVisibility(View.VISIBLE);
                            productDetailsXXLCheckBox.setVisibility(View.VISIBLE);
                            productDetailsXLCheckBox.setVisibility(View.VISIBLE);
                            productDetailsLargeCheckBox.setVisibility(View.VISIBLE);
                            productDetailsMediumCheckBox.setVisibility(View.VISIBLE);
                            productDetailsSmallCheckBox.setVisibility(View.VISIBLE);
                        } else {
                            productDetailsXXXLCheckBox.setVisibility(View.INVISIBLE);
                            productDetailsXXLCheckBox.setVisibility(View.INVISIBLE);
                            productDetailsXLCheckBox.setVisibility(View.INVISIBLE);
                            productDetailsLargeCheckBox.setVisibility(View.INVISIBLE);
                            productDetailsMediumCheckBox.setVisibility(View.INVISIBLE);
                            productDetailsSmallCheckBox.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "There is no Snapshot for this Database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void GetUserDetails()
    {
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


    private void AddProductToCart()
    {


        if (_productID != null){
            Paper.book().write(Prevalent.currentProductKey, _productID);
        }
        else if (productID != null)
        {
            Paper.book().write(Prevalent.currentProductKey, productID);
        }
        if (vendorID != null)
        {
            businessID = vendorID;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM_dd_yyyy");
        saveCurrentDate = currentDateFormat.format(calendar.getTime());
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH_mm_ss_");
        saveCurrentTime = currentTimeFormat.format(calendar.getTime());
        String orderID = saveCurrentDate + saveCurrentTime + productID;
        Paper.book().write(Prevalent.currentCartID, orderID);
        String getProductCartQuantity = String.valueOf(addToCartQuantity.getValue());
        HashMap<String, Object> cartDataMap = new HashMap<>();
        cartDataMap.put("a", "a");
        cartDataMap.put("cartID", cartID);
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
        dbRef.child("cart")
                .child("users")
                .child(userID)
                .child("orders")
                .child(orderID)
                .updateChildren(cartDataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            dbRef.child("cart")
                                    .child("vendors")
                                    .child(businessID)
                                    .child("orders")
                                    .child(orderID)
                                    .updateChildren(cartDataMap);
                            Toast.makeText(ProductDetailsActivity.this, "User Order Reference Created", Toast.LENGTH_SHORT).show();
                            addToCartButton.setVisibility(View.GONE);
                            goToHomeButton.setVisibility(View.VISIBLE);
                            goToVendorButton.setVisibility(View.VISIBLE);

                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, "Product Not Added To Cart : Error : " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Paper.book().delete(Prevalent.currentProductKey);
    }
}

