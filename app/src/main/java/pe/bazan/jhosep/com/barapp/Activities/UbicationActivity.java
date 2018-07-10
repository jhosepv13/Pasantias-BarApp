package pe.bazan.jhosep.com.barapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pe.bazan.jhosep.com.barapp.R;

public class UbicationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = UbicationActivity.class.getSimpleName();
    private GoogleMap mMap;

    private Marker marker; //marcador
    double lat = 0.0; //latitud
    double lng = 0.0; //longitud

    private Geocoder geocoder;
    private List<Address> addresses;

//    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubication);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }else{
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(), 10);
            dialog.show();
        }

        //Check Permisos

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }else{
                    Toast.makeText(this, "Se requiere proporcionar los permisos", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        myUbicación();

        if(addresses != null){
            Log.d(TAG, "Addresses: "+addresses);
            Intent intent = new Intent(this, MainActivity.class);

            String address = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String city = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalcode = addresses.get(0).getPostalCode();

            String fullAddress = address+ " "+area+ " "+city+ " "+country+ " "+postalcode;

            intent.putExtra("fullAddress", fullAddress);

            startActivity(intent);
            finish();
        }

        /*
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        float zoomlevel = 16;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));
        */
    }

    private void addMarker(double lat, double lng, String fullAddress){

        // Habilitar Zoom en Maps
        //UiSettings uiSettings = mMap.getUiSettings();
        //uiSettings.setZoomControlsEnabled(true);

        LatLng coord = new LatLng(lat, lng);
        CameraUpdate ubication = CameraUpdateFactory.newLatLngZoom(coord, 16);
        if(marker != null)marker.remove();
        marker = mMap.addMarker(new MarkerOptions()
        .position(coord)
        .title(fullAddress));
        mMap.animateCamera(ubication);

    }

    private void updateMyUbication(Location location){
        if(location != null){
            lat = location.getLatitude();
            lng = location.getLongitude();

            //GET LOCAL ADDRESS

            geocoder = new Geocoder(this, Locale.getDefault());

            try{
                addresses = geocoder.getFromLocation(lat, lng, 1);

                String address = addresses.get(0).getAddressLine(0);
                String area = addresses.get(0).getLocality();
                String city = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalcode = addresses.get(0).getPostalCode();

                String fullAddress = address+ " "+area+ " "+city+ " "+country+ " "+postalcode;

                Log.e(TAG, "Address: "+fullAddress);

                addMarker(lat, lng, fullAddress);

            }catch (IOException e){
                e.printStackTrace();
            }


        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateMyUbication(location);
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        public void onProviderEnabled(String s) {

        }

        public void onProviderDisabled(String s) {

        }

    };

    private void myUbicación(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        updateMyUbication(location);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000,0, locationListener);

    }
}
