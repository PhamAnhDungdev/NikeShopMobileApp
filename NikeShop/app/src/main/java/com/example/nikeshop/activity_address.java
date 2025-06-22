package com.example.nikeshop;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nikeshop.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class activity_address extends AppCompatActivity implements OnMapReadyCallback{

    private ImageView btnBack, btnSearchAddress;
    private EditText etAddressMain, etAddressCity;
    private TextInputEditText etFloorNumber, etPostalCode;
    private Button btnSave;
    private ProgressBar mapLoading;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Geocoder geocoder;

    // Default location (New York City)
    private LatLng currentLocation = new LatLng(40.7128, -74.0060);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initViews();
        setupMap();
        setupClickListeners();
        setupTextWatchers();

        geocoder = new Geocoder(this, Locale.getDefault());
        loadCurrentAddress();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSearchAddress = findViewById(R.id.btn_search_address);
        etAddressMain = findViewById(R.id.et_address_main);
        etAddressCity = findViewById(R.id.et_address_city);
        etFloorNumber = findViewById(R.id.et_floor_number);
        etPostalCode = findViewById(R.id.et_postal_code);
        btnSave = findViewById(R.id.btn_save);
        mapLoading = findViewById(R.id.map_loading);
    }

    private void setupMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync((OnMapReadyCallback) this);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnSearchAddress.setOnClickListener(v -> searchAndUpdateMap());

        btnSave.setOnClickListener(v -> saveAddress());
    }

    private void setupTextWatchers() {
        TextWatcher addressWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Auto-update map after user stops typing (with delay)
                etAddressMain.removeCallbacks(updateMapRunnable);
                etAddressMain.postDelayed(updateMapRunnable, 1500); // 1.5 second delay
            }
        };

        etAddressMain.addTextChangedListener(addressWatcher);
        etAddressCity.addTextChangedListener(addressWatcher);
    }

    private final Runnable updateMapRunnable = new Runnable() {
        @Override
        public void run() {
            searchAndUpdateMap();
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Configure map
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Set initial location
        updateMapLocation(currentLocation, "Current Location");

        // Allow user to tap on map to select location
        mMap.setOnMapClickListener(latLng -> {
            currentLocation = latLng;
            updateMapLocation(latLng, "Selected Location");
            reverseGeocode(latLng);
        });
    }

    private void searchAndUpdateMap() {
        String mainAddress = etAddressMain.getText().toString().trim();
        String city = etAddressCity.getText().toString().trim();

        if (mainAddress.isEmpty() && city.isEmpty()) {
            return;
        }

        String fullAddress = mainAddress + ", " + city;
        geocodeAddress(fullAddress);
    }

    private void geocodeAddress(String address) {
        mapLoading.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);

                runOnUiThread(() -> {
                    mapLoading.setVisibility(View.GONE);

                    if (addresses != null && !addresses.isEmpty()) {
                        Address location = addresses.get(0);
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        currentLocation = latLng;
                        updateMapLocation(latLng, address);

                        // Update postal code if available
                        if (location.getPostalCode() != null && etPostalCode.getText().toString().isEmpty()) {
                            etPostalCode.setText(location.getPostalCode());
                        }
                    } else {
                        Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                runOnUiThread(() -> {
                    mapLoading.setVisibility(View.GONE);
                    Toast.makeText(this, "Error searching address", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void reverseGeocode(LatLng latLng) {
        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocation(
                        latLng.latitude, latLng.longitude, 1);

                runOnUiThread(() -> {
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);

                        // Update address fields
                        String addressLine = address.getAddressLine(0);
                        if (addressLine != null) {
                            String[] parts = addressLine.split(",");
                            if (parts.length > 0) {
                                etAddressMain.setText(parts[0].trim());
                            }
                            if (parts.length > 1) {
                                etAddressCity.setText(parts[1].trim());
                            }
                        }

                        // Update postal code
                        if (address.getPostalCode() != null) {
                            etPostalCode.setText(address.getPostalCode());
                        }
                    }
                });

            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error getting address details", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateMapLocation(LatLng latLng, String title) {
        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(title));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private void loadCurrentAddress() {
        // Load saved data
        String savedMainAddress = getSharedPreferences("address_prefs", MODE_PRIVATE)
                .getString("main_address", "NYC Street No.23 Block 1");
        String savedCity = getSharedPreferences("address_prefs", MODE_PRIVATE)
                .getString("city", "New York City");
        String savedFloor = getSharedPreferences("address_prefs", MODE_PRIVATE)
                .getString("floor_number", "");
        String savedPostal = getSharedPreferences("address_prefs", MODE_PRIVATE)
                .getString("postal_code", "");

        etAddressMain.setText(savedMainAddress);
        etAddressCity.setText(savedCity);
        etFloorNumber.setText(savedFloor);
        etPostalCode.setText(savedPostal);

        // Update map with saved address
        searchAndUpdateMap();
    }

    private void saveAddress() {
        String mainAddress = etAddressMain.getText().toString().trim();
        String city = etAddressCity.getText().toString().trim();
        String floorNumber = etFloorNumber.getText().toString().trim();
        String postalCode = etPostalCode.getText().toString().trim();

        // Validate inputs
        if (mainAddress.isEmpty()) {
            etAddressMain.setError("Main address is required");
            etAddressMain.requestFocus();
            return;
        }

        if (city.isEmpty()) {
            etAddressCity.setError("City is required");
            etAddressCity.requestFocus();
            return;
        }

        // Save to SharedPreferences
        getSharedPreferences("address_prefs", MODE_PRIVATE)
                .edit()
                .putString("main_address", mainAddress)
                .putString("city", city)
                .putString("floor_number", floorNumber)
                .putString("postal_code", postalCode)
                .putFloat("latitude", (float) currentLocation.latitude)
                .putFloat("longitude", (float) currentLocation.longitude)
                .apply();

        Toast.makeText(this, "Address saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}