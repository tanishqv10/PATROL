package com.example.newsapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private HeatmapTileProvider mProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        checkLocationPermission();
        return view;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            } else {
                Toast.makeText(getActivity(), "Permission denied to access your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        fetchLocationFromIP();
        addHeatMap();
    }

    private void fetchLocationFromIP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    // Replace YOUR_API_KEY with your actual IPInfo API key
                    URL url = new URL("https://ipinfo.io/json?token=" + BuildConfig.IPINFO_TOKEN);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Parse JSON response and update the map
                        updateMapWithLocation(new JSONObject(response.toString()));
                    } else {
                        System.out.println("GET request not worked");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void updateMapWithLocation(JSONObject jsonObject) throws Exception {
        String location = jsonObject.getString("loc");
        String[] latLong = location.split(",");
        double latitude = Double.parseDouble(latLong[0]);
        double longitude = Double.parseDouble(latLong[1]);

        // Ensure that getActivity() does not return null
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng latLng = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        });
    }

    private void addHeatMap() {
        List<WeightedLatLng> data = getHeatmapData(); // Replace with your data fetching method
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(data)
                .build();
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private List<WeightedLatLng> getHeatmapData() {
        List<WeightedLatLng> list = new ArrayList<>();
        list.add(new WeightedLatLng(new LatLng(34.0407, -118.2468), 268)); // Downtown Los Angeles
        list.add(new WeightedLatLng(new LatLng(34.0983, -118.3267), 90)); // Hollywood
        list.add(new WeightedLatLng(new LatLng(33.9850, -118.4695), 180)); // Venice
        list.add(new WeightedLatLng(new LatLng(34.0900, -118.3617), 347)); // West Hollywood
        list.add(new WeightedLatLng(new LatLng(34.0869, -118.2707), 216)); // Silver Lake
        list.add(new WeightedLatLng(new LatLng(34.0782, -118.2606), 297)); // Echo Park
        list.add(new WeightedLatLng(new LatLng(34.0332, -118.2044), 225)); // Boyle Heights
        list.add(new WeightedLatLng(new LatLng(33.9386, -118.2386), 109)); // Watts
        list.add(new WeightedLatLng(new LatLng(34.0635, -118.4455), 432)); // Westwood
        list.add(new WeightedLatLng(new LatLng(34.0521, -118.4730), 460)); // Brentwood
        list.add(new WeightedLatLng(new LatLng(34.2490, -118.4138), 150)); // North Hills
        list.add(new WeightedLatLng(new LatLng(33.9161, -118.0120), 275)); // Diamond Bar
        list.add(new WeightedLatLng(new LatLng(34.0197, -118.4912), 230)); // Pacific Palisades
        list.add(new WeightedLatLng(new LatLng(34.1653, -118.6082), 200)); // West Hills
        list.add(new WeightedLatLng(new LatLng(34.1606, -118.5107), 195)); // Burbank
        list.add(new WeightedLatLng(new LatLng(34.2355, -118.5447), 220)); // Chatsworth
        list.add(new WeightedLatLng(new LatLng(34.2095, -118.5754), 170)); // Woodland Hills
        list.add(new WeightedLatLng(new LatLng(34.0600, -118.2388), 300)); // East Los Angeles
        list.add(new WeightedLatLng(new LatLng(34.0525, -118.4459), 215)); // Bel Air
        list.add(new WeightedLatLng(new LatLng(34.4262, -118.5596), 180)); // Santa Clarita
        list.add(new WeightedLatLng(new LatLng(33.8464, -118.0467), 120)); // La Mirada
        list.add(new WeightedLatLng(new LatLng(34.1077, -118.0579), 245)); // San Gabriel
        list.add(new WeightedLatLng(new LatLng(34.2490, -118.2927), 190)); // Lake View Terrace
        list.add(new WeightedLatLng(new LatLng(34.6868, -118.1542), 280)); // Lancaster
        list.add(new WeightedLatLng(new LatLng(34.5794, -118.1165), 260)); // Palmdale
        list.add(new WeightedLatLng(new LatLng(34.1425, -118.2551), 85)); // Glendale
        list.add(new WeightedLatLng(new LatLng(33.9728, -118.2490), 340)); // South Gate
        list.add(new WeightedLatLng(new LatLng(33.8883, -118.3539), 195)); // Lynwood
        list.add(new WeightedLatLng(new LatLng(33.8175, -118.2193), 225)); // Carson
        list.add(new WeightedLatLng(new LatLng(34.3058, -118.4572), 90)); // Mission Hills
        return list;
    }
}
