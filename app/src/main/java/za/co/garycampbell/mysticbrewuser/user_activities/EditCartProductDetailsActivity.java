package za.co.garycampbell.mysticbrewuser.user_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import za.co.garycampbell.mysticbrewuser.R;

public class EditCartProductDetailsActivity extends AppCompatActivity {

    ImageView productImageView;
    TextView productName, productPrice;
    NumberPicker productQuantity;
    AppCompatButton editCartProduct, removeProduct;

    String productID, imageUrl, name, price, quantity;
    int intValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart_product_details);

        productID = getIntent().getStringExtra("productID");
        imageUrl = getIntent().getStringExtra("productImage");
        name = getIntent().getStringExtra("productName");
        price = getIntent().getStringExtra("productPrice");
        quantity = getIntent().getStringExtra("productQuantity");


        productImageView = findViewById(R.id.product_view_product_image);
        productName = findViewById(R.id.product_view_product_name);
        productPrice = findViewById(R.id.product_view_product_Price);
        productQuantity = findViewById(R.id.product_view_add_to_cart_quantity);
        productQuantity.setOnScrollListener(new NumberPicker.OnScrollListener() {
                @Override
                public void onScrollStateChange(NumberPicker view, int scrollState) {
                    productQuantity.setMinValue(0);
                    productQuantity.setMaxValue(100);
                }
            });

        editCartProduct = findViewById(R.id.edit_cart);

        Picasso.get().load(imageUrl).into(productImageView);
        productName.setText(name);
        productPrice.setText(price);
        if (quantity != null)
        {
            intValue = Integer.parseInt(quantity);
            if (intValue >= 0)
            {
                productQuantity.setMinValue(intValue);
                productQuantity.setMaxValue(intValue);
            }
        }

        editCartProduct = findViewById(R.id.edit_cart);
        editCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCartProduct();
            }
        });

        removeProduct = findViewById(R.id.delete_from_cart);
        removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveCartProduct();
            }
        });
    }

    private void EditCartProduct() {

    }

    private void RemoveCartProduct() {

    }

}