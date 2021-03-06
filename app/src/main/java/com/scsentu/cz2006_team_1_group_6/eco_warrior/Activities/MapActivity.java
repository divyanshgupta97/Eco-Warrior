package com.scsentu.cz2006_team_1_group_6.eco_warrior.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.kml.KmlLayer;
import com.scsentu.cz2006_team_1_group_6.eco_warrior.Adapters.CustomInfoWindowAdapter;
import com.scsentu.cz2006_team_1_group_6.eco_warrior.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * This class takes in an integer which is a reference to a kml file,
 * from the calling intent, and puts the kml layer onto a Google Map
 * Fragment.
 */

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapActivity";

    private GoogleMap mMap;

    private int kml_ref;

    private String mWasteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        kml_ref = getIntent().getIntExtra("kml_ref", 0);

        findWasteType();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try{
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.3521, 103.8198 ), 11.0f));
            LatLng singapore = new LatLng(1.3521, 103.8198);
            mMap.addMarker(new MarkerOptions().position(singapore).title("Marker is on"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));

            KmlLayer kml = new KmlLayer(googleMap,kml_ref ,getApplicationContext());
            kml.addLayerToMap();

            CustomInfoWindowAdapter customInfoWindowAdapter = new CustomInfoWindowAdapter(this);
            mMap.setInfoWindowAdapter(customInfoWindowAdapter);

            mMap.setOnInfoWindowClickListener(this);

        } catch(XmlPullParserException e){
            Log.d(TAG, "error: " + e.getMessage());
        } catch (IOException e){
            Log.d(TAG, "error: " + e.getMessage());
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Double latitude = marker.getPosition().latitude;
        Double longitude = marker.getPosition().longitude;

        Intent markerDialogIntent = new Intent(this, MarkerDialogActivity.class);
        markerDialogIntent.putExtra("latitude", latitude);
        markerDialogIntent.putExtra("longitude", longitude);
        markerDialogIntent.putExtra("wasteType", mWasteType);

        startActivity(markerDialogIntent);
    }

    private void findWasteType(){
        switch (kml_ref){
            case R.raw.e_waste_recycling_kml: {
                mWasteType = "eWaste";
                break;
            }
            case R.raw.lightning_waste_kml: {
                mWasteType = "lightningWaste";
                break;
            }
            case R.raw.second_hand_goods_collection_points_kml: {
                mWasteType = "secondHandWaste";
                break;
            }
            case R.raw.cash_for_trash_kml: {
                mWasteType = "cashForTrashWaste";
                break;
            }
        }
    }
}
