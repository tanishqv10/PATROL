package com.example.newsapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.newsapplication.ui.health.HealthyFragment;
import com.example.newsapplication.ui.health.InfectedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthStatusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private HealthStatusDetailsAdapter healthStatusDetailsAdapter;

    private ArrayList<HealthStatusDetails> healthStatusesDetails;

    private LoginManager loginManager;

    private Button btnWell, btnUnwell, btnInfected;

    public HealthStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthStatusFragment newInstance(String param1, String param2) {
        HealthStatusFragment fragment = new HealthStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loginManager = new LoginManager(getContext());
        if (!loginManager.isLoggedIn()) {
            Log.e("HealthStatusFragment", "User is not logged in");
        } else {
            Log.i("HealthStatusFragment", "User is logged in with email " + loginManager.getEmail());
        }

        healthStatusesDetails = new ArrayList<>();
        healthStatusDetailsAdapter = new HealthStatusDetailsAdapter(healthStatusesDetails, getContext());
        View view = inflater.inflate(R.layout.fragment_health_status, container, false);
        RecyclerView checkinRV = view.findViewById(R.id.idRVLatestCheckins);
        checkinRV.setAdapter(healthStatusDetailsAdapter);
        checkinRV.setLayoutManager(new LinearLayoutManager(getContext()));

        int containerViewId = view.getId();

        getHealthStatuses();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the ID of the container holding this fragment
        int containerViewId = view.getId();

         btnWell = view.findViewById(R.id.btnWell);
         btnUnwell = view.findViewById(R.id.btnUnwell);
         btnInfected = view.findViewById(R.id.btnInfected);

        btnWell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new HealthyFragment();

                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_health);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_HEALTHY")
                        .addToBackStack(null)
                        .commit();

            }
        });

        btnUnwell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new HealthCheckFormFragment();

                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_health);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_HEALTH_CHECK")
                        .addToBackStack(null)
                        .commit();

            }
        });

        btnInfected.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                Fragment fragment = new InfectedFragment();

                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_health);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_INFECTED")
                        .addToBackStack(null)
                        .commit();

            }
        });


    }

    private void getHealthStatuses() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HealthCheckApi api = retrofit.create(HealthCheckApi.class);
        String email = loginManager.getEmail();
        Call<HealthStatusesModal> call = api.getHealthStatuses(email);
        System.out.println("HealthStatusFragment: getHealthStatuses()");
        call.enqueue(new Callback<HealthStatusesModal>() {
            @Override
            public void onResponse(Call<HealthStatusesModal> call, Response<HealthStatusesModal> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    HealthStatusesModal healthStatusesModal = response.body();
                    System.out.println(response.toString());
                    ArrayList<HealthStatusDetails> statuses  = healthStatusesModal.getHealthStatuses();
                    System.out.println("Sizes: " + statuses.size());
                    for (int i = 0; i < statuses.size(); i++) {
                        healthStatusesDetails.add(statuses.get(i));
                    }

                    healthStatusDetailsAdapter.notifyDataSetChanged();
                } else {
                    // Handle failure
                    System.out.println("Unable to fetch health statuses: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<HealthStatusesModal> call, Throwable t) {
                // Handle error
                System.out.println("Failure: " + t.toString());
            }
        });
    }
}