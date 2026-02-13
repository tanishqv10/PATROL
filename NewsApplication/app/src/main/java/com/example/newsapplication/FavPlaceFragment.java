package com.example.newsapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.GsonBuilder;

import javax.security.auth.login.LoginException;

public class FavPlaceFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavPlaceAdapter adapter;
    private Map<String, Integer> placeRiskMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_place, container, false);

        initializePlaceRiskMap();  // Initialize the risk map

        recyclerView = view.findViewById(R.id.rvFavoritePlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavPlaceAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        fetchFavoritePlaces();

        return view;
    }

    private void initializePlaceRiskMap() {
        placeRiskMap = new HashMap<>();
        placeRiskMap.put("North Hills", 150);
        placeRiskMap.put("Diamond Bar", 275);
        placeRiskMap.put("Pacific Palisades", 230);
        placeRiskMap.put("West Hills", 200);
        placeRiskMap.put("Burbank", 195);
        placeRiskMap.put("Chatsworth", 220);
        placeRiskMap.put("Woodland Hills", 170);
        placeRiskMap.put("East Los Angeles", 300);
        placeRiskMap.put("Bel Air", 215);
        placeRiskMap.put("Santa Clarita", 180);
        placeRiskMap.put("La Mirada", 120);
        placeRiskMap.put("San Gabriel", 245);
        placeRiskMap.put("Lake View Terrace", 190);
        placeRiskMap.put("Lancaster", 280);
        placeRiskMap.put("Palmdale", 260);
        placeRiskMap.put("Glendale", 85);
        placeRiskMap.put("South Gate", 340);
        placeRiskMap.put("Lynwood", 195);
        placeRiskMap.put("Carson", 225);
        placeRiskMap.put("Mission Hills", 90);
        placeRiskMap.put("Downtown Los Angeles", 268);
        placeRiskMap.put("Hollywood", 90);
        placeRiskMap.put("Venice", 180);
        placeRiskMap.put("West Hollywood", 347);
        placeRiskMap.put("Silver Lake", 216);
        placeRiskMap.put("Echo Park", 297);
        placeRiskMap.put("Boyle Heights", 225);
        placeRiskMap.put("Watts", 109);
        placeRiskMap.put("Westwood", 432);
        placeRiskMap.put("Brentwood", 460);
    }


    private List<Destination> updateRiskLevels(List<Destination> destinations) {
        for (Destination destination : destinations) {
            Integer riskNumber = placeRiskMap.get(destination.getName());
            if (riskNumber != null) {
                if (riskNumber < 100) {
                    destination.setRiskLevel("Low Risk");
                } else if (riskNumber <= 500) {
                    destination.setRiskLevel("Moderate Risk");
                } else {
                    destination.setRiskLevel("High Risk");
                }
            } else {
                destination.setRiskLevel("Unknown Risk");
            }
        }
        return destinations;
    }

    private void fetchFavoritePlaces() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        FavPlaces service = retrofit.create(FavPlaces.class);
        LoginManager loginManager = new LoginManager(getContext());
        String email = loginManager.getEmail();

        service.getFavPlaces(email).enqueue(new Callback<DestinationResponse>() {
            @Override
            public void onResponse(Call<DestinationResponse> call, Response<DestinationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getFavPlaces() != null) {
                    List<Destination> updatedDestinations = updateRiskLevels(response.body().getFavPlaces());
                    adapter.updateData(updatedDestinations); // Updating adapter with the processed list
                } else {
                    Log.e("FavPlaceFragment", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DestinationResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
