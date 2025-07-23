package com.example.nikeshop.ui.activities;

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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.nikeshop.R;
import com.example.nikeshop.ui.ViewModels.UserViewModel;
import com.example.nikeshop.data.local.entity.User;
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

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView btnBack, btnSearchAddress;
    private EditText etAddressMain, etAddressCity;
    private TextInputEditText etFloorNumber, etPostalCode;
    private Button btnSave;
    private ProgressBar mapLoading;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Geocoder geocoder;
    private LatLng currentLocation;

    // User management
    private UserViewModel userViewModel;
    private User currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initViews();
        initViewModel();
        getUserIdFromSession();
        setupMap();
        setupClickListeners();
        setupTextWatchers();

        geocoder = new Geocoder(this, Locale.getDefault());
        loadUserAddress();
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

    private void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    private void getUserIdFromSession() {
        // Get user ID from session/SharedPreferences
        userId = getSharedPreferences("user_session", MODE_PRIVATE)
                .getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "User session not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void loadUserAddress() {
        if (userId == -1) return;

        mapLoading.setVisibility(View.VISIBLE);

        userViewModel.getUserById(userId).observe(this, user -> {
            mapLoading.setVisibility(View.GONE);

            if (user != null) {
                currentUser = user;
                parseAndLoadAddress(user.getAddress());
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void parseAndLoadAddress(String address) {
        if (address == null || address.isEmpty()) {
            // Set default location if no address
            currentLocation = new LatLng(21.0285, 105.8542); // Hanoi default
            if (mMap != null) {
                updateMapLocation(currentLocation, "Default Location");
            }
            return;
        }

        // Parse address format: "Đống Đa-Hà Nội"
        String[] addressParts = address.split("-");

        if (addressParts.length >= 2) {
            String city = addressParts[0].trim(); // "Đống Đa"
            String mainAddress = addressParts[1].trim(); // "Hà Nội"

            etAddressCity.setText(city);
            etAddressMain.setText(mainAddress);

            // Search and update map with the parsed address
            searchAndUpdateMapFromUserAddress(address);
        } else {
            // If format is different, put entire address in main field
            etAddressMain.setText(address);
            searchAndUpdateMapFromUserAddress(address);
        }
    }

    private void searchAndUpdateMapFromUserAddress(String address) {
        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(address + ", Vietnam", 1);
                runOnUiThread(() -> {
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
                        // Default to Hanoi if address not found
                        currentLocation = new LatLng(21.0285, 105.8542);
                        updateMapLocation(currentLocation, "Default Location");
                    }
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    currentLocation = new LatLng(21.0285, 105.8542);
                    updateMapLocation(currentLocation, "Default Location");
                });
            }
        }).start();
    }

    private void setupMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
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
                etAddressMain.removeCallbacks(updateMapRunnable);
                etAddressMain.postDelayed(updateMapRunnable, 1500);
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Set initial location if available
        if (currentLocation != null) {
            updateMapLocation(currentLocation, "Current Location");
        }

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

        String fullAddress = city + ", " + mainAddress + ", Vietnam";
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

                        // Update address fields based on Vietnamese address format
                        String locality = address.getLocality(); // City/Province
                        String subLocality = address.getSubLocality(); // District
                        String thoroughfare = address.getThoroughfare(); // Street

                        if (locality != null) {
                            etAddressMain.setText(locality);
                        }
                        if (subLocality != null) {
                            etAddressCity.setText(subLocality);
                        } else if (thoroughfare != null) {
                            etAddressCity.setText(thoroughfare);
                        }

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

        if (currentUser == null) {
            Toast.makeText(this, "User data not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format address as "City-MainAddress" (e.g., "Đống Đa-Hà Nội")
        String formattedAddress = city + "-" + mainAddress;

        // Update user object
        currentUser.setAddress(formattedAddress);

        // Show loading state
        btnSave.setEnabled(false);
        btnSave.setText("Saving...");

        // Update user in database using background thread
        new Thread(() -> {
            try {
                userViewModel.update(currentUser);

                // Update UI on main thread
                runOnUiThread(() -> {
                    btnSave.setEnabled(true);
                    btnSave.setText("Save");

                    Toast.makeText(this, "Address saved successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnSave.setEnabled(true);
                    btnSave.setText("Save");
                    Toast.makeText(this, "Failed to save address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
