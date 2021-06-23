package za.co.garycampbell.mysticbrewuser.user_activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import za.co.garycampbell.mysticbrewuser.common_activities.SignInActivity;
import za.co.garycampbell.mysticbrewuser.models.ProductModel;
import za.co.garycampbell.mysticbrewuser.models.ServicesModel;
import za.co.garycampbell.mysticbrewuser.models.VendorModel;
import za.co.garycampbell.mysticbrewuser.viewholders.MainViewHolder;
import za.co.garycampbell.mysticbrewuser.viewholders.ProductViewHolder;
import za.co.garycampbell.mysticbrewuser.viewholders.ServiceViewHolder;


public class UserHomeActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    RecyclerView mainRecycler;
    FirebaseRecyclerAdapter<VendorModel, MainViewHolder> vendorAdapter;
    FirebaseRecyclerAdapter<ProductModel, ProductViewHolder> productAdapter;
    RecyclerView.LayoutManager manager;
    Marker marker;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference vendorRef;
    private GoogleMap mMap;
    public static final int ROUND = 10;
    public GoogleApiClient googleApiClient;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    public FusedLocationProviderClient fusedLocationProviderClient;

    ImageView cartButton, searchButton, locationButton, logOutButton;

    String businessID;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Paper.init(this);
        ChildEventListener mChildEventListener;

        firebaseDatabase = FirebaseDatabase.getInstance();
        vendorRef = firebaseDatabase.getReference();

        cartButton = findViewById(R.id.cartImageView);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCart = new Intent(UserHomeActivity.this, CartActivity.class);
                startActivity(goToCart);
//                ToDo:
            }
        });

        searchButton = findViewById(R.id.searchImageView);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent goToSearch = new Intent(UserHomeActivity.this, SearchActivity.class);
//                startActivity(goToSearch);
            }
        });

        locationButton = findViewById(R.id.locationImageView);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write(Prevalent.userLocationAcceptKey, "true");
                if (ActivityCompat.checkSelfPermission(UserHomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserHomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        });

        logOutButton = findViewById(R.id.log_out_ImageView);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logMeOut = new Intent(UserHomeActivity.this, SignInActivity.class);
                Paper.book().destroy();
                Paper.book().write(Prevalent.userLocationAcceptKey, "true");
                startActivity(logMeOut);
            }
        });

        mainRecycler = findViewById(R.id.mainRecyclerView);
        manager = new GridLayoutManager(this, 3);
        mainRecycler.setLayoutManager(manager);

        FirebaseRecyclerOptions<VendorModel> mainOptions = new FirebaseRecyclerOptions.Builder<VendorModel>()
                .setQuery(vendorRef.child("vendors"), VendorModel.class)
                .build();
        vendorAdapter = new FirebaseRecyclerAdapter<VendorModel, MainViewHolder>(mainOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MainViewHolder mainHolder, int position, @NonNull VendorModel model) {
                mainHolder.vendorTitle.setText(model.getBusinessName());
                mainHolder.vendorContact.setText(model.getBusinessContactNumber());
                mainHolder.vendorCategory.setText(model.getBusinessCategory());
                Picasso.get().load(model.getVendorImageUrl()).into(mainHolder.vendorImage);
                businessID = model.getBusinessID();
                LatLng vendorMapPosition = new LatLng(model.getBusinessLatitude(), model.getBusinessLongitude());
                if (mainHolder.itemView.isFocused()) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(vendorMapPosition));
                }

                mainHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent goToVendorDetails = new Intent(UserHomeActivity.this, VendorDetailsActivity.class);
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
            }


            @NonNull
            @Override
            public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mainView = LayoutInflater.from(getBaseContext())
                        .inflate(R.layout.main_recycler, parent, false);
                return new MainViewHolder(mainView);
            }
        };
        vendorAdapter.startListening();
        vendorAdapter.notifyDataSetChanged();
        mainRecycler.setAdapter(vendorAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String Consent = Paper.book().read(Prevalent.userLocationAcceptKey);
        if (Consent == null) {
            new AlertDialog.Builder(this)
                    .setTitle(" Background Location Access Required")
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
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            if (ActivityCompat.checkSelfPermission(UserHomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UserHomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(false);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        googleMap.setOnMarkerClickListener(this);
        //setting the size of marker in map by using Bitmap Class
        int height = 40;
        int width = 40;
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.business_location_icon);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        vendorRef.child("vendors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    VendorModel user = s.getValue(VendorModel.class);
                    LatLng location = new LatLng(user.getBusinessLatitude(), user.getBusinessLongitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(user.getBusinessName())).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } else
        {
            if (Consent.equals("true")) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setTrafficEnabled(true);
                mMap.setBuildingsEnabled(true);
                googleMap.setOnMarkerClickListener(this);
                //setting the size of marker in map by using Bitmap Class
                int height = 40;
                int width = 40;
                @SuppressLint("UseCompatLoadingForDrawables")
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.business_location_icon);
                Bitmap b = bitmapdraw.getBitmap();
                final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                vendorRef.child("vendors").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            VendorModel user = s.getValue(VendorModel.class);
                            LatLng location = new LatLng(user.getBusinessLatitude(), user.getBusinessLongitude());
                            mMap.addMarker(new MarkerOptions().position(location).title(user.getBusinessName())).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return false;
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else if (Consent.equals("false"))
            {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setTrafficEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.setMaxZoomPreference(9);
                googleMap.setOnMarkerClickListener(this);
                //setting the size of marker in map by using Bitmap Class
                int height = 40;
                int width = 40;
                @SuppressLint("UseCompatLoadingForDrawables")
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.business_location_icon);
                Bitmap b = bitmapdraw.getBitmap();
                final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                vendorRef.child("vendors").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            VendorModel user = s.getValue(VendorModel.class);
                            LatLng location = new LatLng(user.getBusinessLatitude(), user.getBusinessLongitude());
                            mMap.addMarker(new MarkerOptions().position(location).title(user.getBusinessName())).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
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

    public void GetVendorLocations(String businessID) {
        //setting the size of marker in map by using Bitmap Class
        int height = 20;
        int width = 20;
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.business_location_icon);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        vendorRef.child("vendors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    VendorModel user = s.getValue(VendorModel.class);
                    if (user!= null) {
                        LatLng location = new LatLng(user.getBusinessLatitude(), user.getBusinessLongitude());
                        mMap.addMarker(new MarkerOptions().position(location).title(user.getBusinessName())).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 8));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

