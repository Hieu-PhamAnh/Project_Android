package com.example.login.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private TextView search_location;
    private EditText text_location;
    private FloatingActionButton add_location;
    public static String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        SharedPreferences sharedPreferences1=MapActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        search_location = findViewById(R.id.search_location);
        text_location = findViewById(R.id.text_location);
        add_location = findViewById(R.id.add_location);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_map);

        this.search_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = text_location.getText().toString();

                List<Address> addresses = null;

                if (location != null && !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapActivity.this);

                    try {
                        addresses = geocoder.getFromLocationName(location, 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses != null && addresses.size() != 0) {
                        Address address = addresses.get(0);
                        location = address.getAddressLine(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    } else {
                        Toast.makeText(MapActivity.this, "Can not find this location! Check your internet or your Location!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        text_location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    search_location.performClick();
                }
                return false;
            }
        });

        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor1.putString("address",location);
                editor1.commit();
                startActivity(new Intent(MapActivity.this, OrderActivity.class));
                MapActivity.this.finish();
            }
        });

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (location == null || location.equals("")) {
            LatLng hanoi = new LatLng(21.027763, 105.834160); // 14.0583° N, 108.2772° E
            mMap.addMarker(new MarkerOptions().position(hanoi).title("Ha Noi"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(hanoi));
        } else {
            List<Address> addresses = null;

            if (location != null && !location.equals("")) {
                Geocoder geocoder = new Geocoder(MapActivity.this);

                try {
                    addresses = geocoder.getFromLocationName(location, 3);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null && addresses.size() != 0) {
                    Address address = addresses.get(0);
                    location = address.getAddressLine(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                } else {
                    Toast.makeText(MapActivity.this, "Can not find this location! Check your internet or your Location!", Toast.LENGTH_LONG).show();
                }
            }
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                List<Address> addresses = null;
                Geocoder geocoder = new Geocoder(MapActivity.this);
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 3);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null && addresses.size() != 0) {
                    Address address = addresses.get(0);
                    location = address.getAddressLine(0);
                }


                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                mMap.addMarker(markerOptions);
            }
        });
    }
}
