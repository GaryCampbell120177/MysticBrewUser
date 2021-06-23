package za.co.garycampbell.mysticbrewuser.user_activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;
import za.co.garycampbell.mysticbrewuser.Prevalent;
import za.co.garycampbell.mysticbrewuser.R;
import za.co.garycampbell.mysticbrewuser.adapters.TabsAccessorAdapter;
import za.co.garycampbell.mysticbrewuser.fragments.VendorDashboardProductsFragment;
import za.co.garycampbell.mysticbrewuser.models.ProductModel;
import za.co.garycampbell.mysticbrewuser.models.VendorModel;
import za.co.garycampbell.mysticbrewuser.viewholders.VendorDetailsProductViewHolder;


public class VendorDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static String businessID;
    TextView businessNameTV, businessContactNumberTV, businessEmailTV, businessAddressTV;
    ImageView businessLogoImage;
    RecyclerView vendorDetailsProductRecycler;
    RecyclerView.LayoutManager manager;

    FirebaseDatabase database;
    DatabaseReference dbRef;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<ProductModel, VendorDetailsProductViewHolder> productAdapter;

    public static final int ROUND = 10;
    private GoogleMap mMap;
    public GoogleApiClient googleApiClient;
    private ChildEventListener mChildEventListener;
    Marker marker;
    public FusedLocationProviderClient fusedLocationProviderClient;

    String businessName, businessAddress, businessContactNumber, businessEmail, businessImage, businessLatitude, businessLongitude;
    double _businessLatitude, _businessLongitude;

    TabLayout tabLayout;
    ViewPager viewPager;
    TabsAccessorAdapter tabsAccessorAdapter;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Paper.init(this);

        Intent getData = getIntent();
        businessID = getData.getStringExtra("businessID");
        getData.getStringExtra("businessImage");
        getData.getStringExtra("businessName");
        getData.getStringExtra("businessContactNumber");
        getData.getStringExtra("businessAddress");
        getData.getStringExtra("businessEmail");


        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        businessLogoImage = findViewById(R.id.vendor_details_logo_image);
        businessNameTV = findViewById(R.id.business_name);
        businessContactNumberTV = findViewById(R.id.business_contact_number);
        businessEmailTV = findViewById(R.id.business_email);
        businessAddressTV = findViewById(R.id.business_address);

        tabLayout = findViewById(R.id.vendor_details_tab_layout);
        viewPager = findViewById(R.id.vendor_details_view_pager);
        tabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        getSupportFragmentManager().beginTransaction().add(R.id.vendor_details_view_pager, VendorDashboardProductsFragment.newInstance(businessID),"VendorDashboardProductsFragment").commit();

        viewPager.setAdapter(tabsAccessorAdapter);
        tabLayout.setupWithViewPager(viewPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        GetVendorDetails();
    }

    public static Bundle getMyData() {
        Bundle hm = new Bundle();
        hm.putString("businessID",businessID);
        return hm;
    }

    private void GetVendorDetails() {

        String businessName = getIntent().getStringExtra("businessName");
        String businessAddress = getIntent().getStringExtra("businessAddress");
        String businessContactNumber = getIntent().getStringExtra("businessContactNumber");
        String businessEmail = getIntent().getStringExtra("businessEmail");
        String businessImage = getIntent().getStringExtra("businessImage");

        Bundle bundle = new Bundle();
        bundle.putString("businessID",getIntent().getStringExtra("businessID"));
        VendorDashboardProductsFragment fragInfo = new VendorDashboardProductsFragment();
        fragInfo.setArguments(bundle);

        businessNameTV.setText(businessName);
        businessAddressTV.setText(businessAddress);
        businessContactNumberTV.setText(businessContactNumber);
        businessEmailTV.setText(businessEmail);
        Picasso.get().load(businessImage).into(businessLogoImage);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String Consent = Paper.book().read(Prevalent.userLocationAcceptKey);
        if (Consent == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Background Location Access Required")
                    .setMessage("We use your device location in the background for the following:" +
                            "\n To show your location relative to the businesses in your area" +
                            "\n The following Features ustilise you location in the background" +
                            "\n -- Google Maps" +
                            "\n \n We do not store your location data ....")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Paper.book().write(Prevalent.userLocationAcceptKey, "true");
                            if (ActivityCompat.checkSelfPermission(VendorDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(VendorDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                        }
                    })
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Paper.book().write(Prevalent.userLocationAcceptKey, "false");
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(true);
            mMap.setMaxZoomPreference(9);
            googleMap.setOnMarkerClickListener(this);
            //setting the size of marker in map by using Bitmap Class
            int height = 40;
            int width = 40;
            @SuppressLint("UseCompatLoadingForDrawables")
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.business_location_icon);
            Bitmap b = bitmapdraw.getBitmap();
            final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            dbRef.child("vendors").child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        VendorModel user = dataSnapshot.getValue(VendorModel.class);
                        LatLng location = new LatLng(user.getBusinessLatitude(), user.getBusinessLongitude());
                        mMap.addMarker(new MarkerOptions().position(location).title(user.getBusinessName())).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            if (Consent.equals("true")) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setTrafficEnabled(true);
                mMap.setMaxZoomPreference(9);

                googleMap.setOnMarkerClickListener(this);
                //setting the size of marker in map by using Bitmap Class
                int height = 40;
                int width = 40;
                @SuppressLint("UseCompatLoadingForDrawables")
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.business_location_icon);
                Bitmap b = bitmapdraw.getBitmap();
                final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                dbRef.child("vendors").child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            VendorModel user = dataSnapshot.getValue(VendorModel.class);
                            LatLng location = new LatLng(user.getBusinessLatitude(), user.getBusinessLongitude());
                            mMap.addMarker(new MarkerOptions().position(location).title(user.getBusinessName())).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if (Consent.equals("false"))
            {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.setTrafficEnabled(true);
                mMap.setMaxZoomPreference(9);
                googleMap.setOnMarkerClickListener(this);
                //setting the size of marker in map by using Bitmap Class
                int height = 40;
                int width = 40;
                @SuppressLint("UseCompatLoadingForDrawables")
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.business_location_icon);
                Bitmap b = bitmapdraw.getBitmap();
                final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                dbRef.child("vendors").child(businessID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            VendorModel user = dataSnapshot.getValue(VendorModel.class);
                            LatLng location = new LatLng(user.getBusinessLatitude(), user.getBusinessLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}
