package za.co.garycampbell.mysticbrewuser.user_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;
import za.co.garycampbell.mysticbrewuser.Prevalent;
import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.models.OrdersModel;
import za.co.garycampbell.mysticbrewuser.viewholders.CartViewHolder;


public class CartActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ordersRef;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<OrdersModel, CartViewHolder> ordersAdapter;

    RecyclerView ordersRecycler;
    RecyclerView.LayoutManager manager;
    TextView totalCartPriceTV;

    AppCompatButton checkout;

    String userID, userName, userSurname, userMobile, userEmail,userAddress;
    String sProductPrice, sProductQuantity, sTotalPrice;
    String businessID, productID;
    int totalCartPrice, product, totalProduct;
    int productQuantity, productPrice;


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ordersRef = database.getReference();
        totalCartPrice = 0;
        product = 1;
        totalProduct = 0;
        totalCartPriceTV = findViewById(R.id.cart_total_price);

        ordersRecycler = findViewById(R.id.cart_recycler_view);
        manager = new LinearLayoutManager(this);
        ordersRecycler.setLayoutManager(manager);
        userID = mAuth.getUid();

        checkout = findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitCheckout();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<OrdersModel> mainOptions = new FirebaseRecyclerOptions.Builder<OrdersModel>()
                .setQuery(ordersRef.child("cart").child("users").child(userID).child("orders"), OrdersModel.class)
                .build();
        ordersAdapter = new FirebaseRecyclerAdapter<OrdersModel, CartViewHolder>(mainOptions) {
            @Override
            protected void onBindViewHolder(@androidx.annotation.NonNull CartViewHolder holder, int position, @androidx.annotation.NonNull OrdersModel model) {

                businessID = model.getBusinessID();
                productID = model.getProductID();
                userName = model.getUserName();
                userEmail = model.getUserEmail();

                holder.cartProductName.setText(model.getProductName());
                holder.itemQuantity.setText(model.getProductQuantity());
                sProductQuantity = model.getProductQuantity();
                sProductPrice = model.getProductPrice();
                productQuantity = Integer.parseInt(model.getProductQuantity());
                productPrice = Integer.parseInt(model.getProductPrice());
                holder.cartProductPrice.setText(model.getProductPrice());
                if (productPrice >= 1)
                {
                    int totalPrice = ((productQuantity) * (productPrice));
                    holder.cartProductPrice.setText(String.valueOf(totalPrice));
                    totalCartPrice = totalCartPrice + totalPrice;
                    if (Paper.book().read((Prevalent.currentCartTotalPrice)) == null)
                    {
                        totalCartPriceTV.setText(String.format(" R %s", totalCartPrice));
                    }
                    else
                    {
                        String tempCartProdTotal = Paper.book().read(Prevalent.currentCartTotalPrice);
                        totalCartPriceTV.setText(tempCartProdTotal);
                    }

                    totalProduct = totalProduct + productQuantity;
                }
                holder.itemView.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Edit Item",
                                        "Remove Item"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0)
                                {
                                    Intent editItem = new Intent(CartActivity.this, EditCartProductDetailsActivity.class);
                                    editItem.putExtra("productID", model.getProductID());
                                    editItem.putExtra("productName", model.getProductName());
                                    editItem.putExtra("productPrice", model.getProductPrice());
                                    editItem.putExtra("productQuantity", model.getProductQuantity());
                                    startActivity(editItem);
                                }
                                if (which == 1)
                                {
                                    DeleteProductFromCart(model.getBusinessID());
                                }
                            }
                        });
                        builder.show();
                    }

                });
            }

            @androidx.annotation.NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
                android.view.View ordersView = LayoutInflater.from(getBaseContext())
                        .inflate(R.layout.cart_item_view, parent, false);
                return new CartViewHolder(ordersView);
            }
        };
        ordersAdapter.startListening();
        ordersAdapter.notifyDataSetChanged();
        ordersRecycler.setAdapter(ordersAdapter);
    }

    private void DeleteProductFromCart(String vid)
    {
        String pid = Paper.book().read(Prevalent.currentProductKey);
        DatabaseReference removeProductRef = database.getReference("cart");
        removeProductRef.child("users").child(userID).child("orders").child(pid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(CartActivity.this, "Product Removed From Database", Toast.LENGTH_SHORT).show();
                    removeProductRef.child("vendors").child(vid).child("orders").child(userID).child(pid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(CartActivity.this, "Product Removed From Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void InitCheckout() {

        Intent checkOut = new Intent(CartActivity.this, CheckoutActivity.class);
        //ToDo: Initiate payment Fragment
        checkOut.putExtra("userName", userName);
        checkOut.putExtra("userSurname", userSurname);
        checkOut.putExtra("userAddress", userAddress);
        checkOut.putExtra("userMobile", userMobile);
        checkOut.putExtra("userEmail", userEmail);
        checkOut.putExtra("businessID", businessID);
        checkOut.putExtra("productID", productID);
        checkOut.putExtra("totalProduct", String.valueOf(totalProduct));
        checkOut.putExtra("cartTotal", String.valueOf(totalCartPrice));
        startActivity(checkOut);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goBack = new Intent(this, UserHomeActivity.class);
        startActivity(goBack);
        finish();
    }
}
