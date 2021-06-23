package za.co.garycampbell.mysticbrewuser.user_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;
import za.co.garycampbell.mysticbrewuser.Prevalent;
import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.OrdersModel;
import za.co.garycampbell.mysticbrewuser.models.UserModel;

public class CheckoutActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CHECKOUT = 0;
    public static final String CHECKOUT_RESULT_TRANSACTION = "";
    public static final String CHECKOUT_RESULT_RESOURCE_PATH = "";
    public static final int RESULT_ERROR = 1;
    public static final String CHECKOUT_RESULT_ERROR = "";
    FirebaseDatabase database;
    DatabaseReference userRef;
    FirebaseAuth mAuth;

    TextInputLayout checkoutName, checkoutSurname, checkoutMobile, checkoutEmail, checkoutAddress;
    TextView totalItemsTV, totalPriceTV;
    AppCompatButton checkoutButton;

    String name, surname, mobile, email, address;
    String prodName, prodQuantity, productID;
    String totalPrice, totalProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        checkoutName = findViewById(R.id.checkout_user_name);
        checkoutSurname = findViewById(R.id.checkout_user_surname);
        checkoutMobile = findViewById(R.id.checkout_user_mobile);
        checkoutEmail = findViewById(R.id.checkout_user_email);
        checkoutAddress = findViewById(R.id.checkout_user_delivery_address);
        totalItemsTV = findViewById(R.id.checkout_items_total);
        totalPriceTV = findViewById(R.id.checkout_total_amount);
        checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessOrder();
            }
        });
    }

    private void GetUserDetails()
    {
        String userID = mAuth.getUid();
        if (userID != null) {
            userRef.child("users").child(userID).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel user = snapshot.getValue(UserModel.class);
                    if (user != null) {
                         name = user.getUserName();
                         surname = user.getUserSurname();
                         mobile = user.getUserMobile();
                         email = user.getUserEmail();
                         address = user.getUserAddress();

                        checkoutName.getEditText().setText(name);
                        checkoutSurname.getEditText().setText(surname);
                        checkoutMobile.getEditText().setText(mobile);
                        checkoutEmail.getEditText().setText(email);
                        checkoutAddress.getEditText().setText(address);
                    } else {
                        for (DataSnapshot s : snapshot.getChildren())
                        {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            if (userModel != null)
                            {
                                name = userModel.getUserName();
                                surname = userModel.getUserSurname();
                                mobile = userModel.getUserMobile();
                                email = userModel.getUserEmail();
                                address = userModel.getUserAddress();
                                checkoutName.getEditText().setText(name);
                                checkoutSurname.getEditText().setText(surname);
                                checkoutMobile.getEditText().setText(mobile);
                                checkoutEmail.getEditText().setText(email);
                                checkoutAddress.getEditText().setText(address);
                            }
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
            Toast.makeText(CheckoutActivity.this, "UserID is NULL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        GetUserDetails();
        GetTotals();

    }

    private void GetTotals()
    {

        totalPrice = getIntent().getStringExtra("cartTotal");
        totalPriceTV.setText(totalPrice);
        Paper.book().write(Prevalent.currentCartTotalPrice, String.valueOf(totalPrice));
        totalProduct = getIntent().getStringExtra("totalProduct");
        totalItemsTV.setText(totalProduct);
        Paper.book().write(Prevalent.currentCartTotalProducts, String.valueOf(totalProduct));
    }

    private void ProcessOrder() {
        String userID = mAuth.getUid();
        if (userID != null) {
            userRef.child("cart").child("users").child(userID).child("orders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s : snapshot.getChildren())
                    {
                        OrdersModel order = snapshot.getValue(OrdersModel.class);
                        if (order != null) {
                            productID = order.getProductID();
                            name = checkoutName.getEditText().getText().toString();
                            surname = checkoutSurname.getEditText().getText().toString();
                            mobile = checkoutMobile.getEditText().getText().toString();
                            email = checkoutEmail.getEditText().getText().toString();
                            address = checkoutAddress.getEditText().getText().toString();
                            String businessID = getIntent().getStringExtra("businessID");
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM_dd_yyyy_");
                            String saveCurrentDate = currentDateFormat.format(calendar.getTime());
                            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH_mm_ssss");
                            String saveCurrentTime = currentTimeFormat.format(calendar.getTime());
                            String userFinalOrder = saveCurrentDate + saveCurrentTime;
                            String orderID = Paper.book().read(Prevalent.currentCartID);

                            HashMap<String, Object> userIDOrder = new HashMap<>();
                            userIDOrder.put("orderStatus", "In Checkout");
                            if (businessID != null) {
                                userRef.child("cart")
                                        .child("vendors")
                                        .child(businessID)
                                        .child("orders")
                                        .child(orderID)
                                        .updateChildren(userIDOrder)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            userRef.child("cart")
                                                    .child("users")
                                                    .child(userID)
                                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(CheckoutActivity.this, "Your Cart Has Been Processed, Please expect to be contacted by the relevant Vendors", Toast.LENGTH_SHORT).show();
                                                            Intent goHome = new Intent(CheckoutActivity.this, UserHomeActivity.class);
                                                            Paper.book().delete(Prevalent.currentProductKey);
                                                            Paper.book().delete(Prevalent.currentCartTotalPrice);
                                                            Paper.book().delete(Prevalent.currentCartTotalProducts);
                                                            Paper.book().delete(Prevalent.currentCartID);
                                                            startActivity(goHome);
                                                        }
                                                    });
                                                }
                                            }
                                });
                            }
                            else
                            {
                                Toast.makeText(CheckoutActivity.this, "VendorID is Null", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(CheckoutActivity.this, "Order Snapshot is Null", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}