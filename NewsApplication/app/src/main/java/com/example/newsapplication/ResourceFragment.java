package com.example.newsapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.google.gson.GsonBuilder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResourceFragment extends Fragment {

    private RecyclerView recyclerView;
    private HospitalAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new HospitalAdapter(new ArrayList<>(), getActivity());

        recyclerView.setAdapter(adapter);
        fetchHospitals(); // Call method to fetch hospitals
        return view;
    }

    private void fetchHospitals() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();


        HospitalService service = retrofit.create(HospitalService.class);
        service.getNearbyHospitals(5000).enqueue(new Callback<List<Hospitals>>() {
            @Override
            public void onResponse(Call<List<Hospitals>> call, Response<List<Hospitals>> response) {
                if (response.isSuccessful()) {
                    adapter.setData(response.body()); // Update adapter data
                } else {
                    Toast.makeText(getActivity(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Hospitals>> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getActivity(), "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
