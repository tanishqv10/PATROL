package com.example.newsapplication.ui.health;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.newsapplication.HealthCheckApi;
import com.example.newsapplication.HealthStatus;
import com.example.newsapplication.HealthStatusDetails;
import com.example.newsapplication.HealthStatusFragment;
import com.example.newsapplication.HomeActivity;
import com.example.newsapplication.LoginManager;
import com.example.newsapplication.R;
import com.example.newsapplication.Symptoms;
import com.example.newsapplication.databinding.FragmentHealthyBinding;
import com.example.newsapplication.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthyFragment extends Fragment {

    private HealthyViewModel mViewModel;
    private FragmentHealthyBinding binding;
    public static HealthyFragment newInstance() {
        return new HealthyFragment();
    }
    private Button btnDashboard, btnHealth;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        InfectedViewModel infectedViewModel =
                new ViewModelProvider(this).get(InfectedViewModel.class);

        binding = FragmentHealthyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HomeActivity activity = (HomeActivity) getActivity();

        activity.toolbar.setBackInvokedCallbackEnabled(true);

        activity.setSupportActionBar(activity.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Bach button click listener for fragments
        activity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // Remove out the previous fragments
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("FRAGMENT_HEALTH");

        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        btnDashboard = root.findViewById(R.id.btnDashboard);
        btnHealth = root.findViewById(R.id.btnHealth);

        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new HomeFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_HOME")
                        .addToBackStack("FRAGMENT_HOME")
                        .commit();

                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);

            }
        });

        btnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new HealthStatusFragment();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_HEALTH")
                        .addToBackStack("FRAGMENT_HOME")
                        .commit();

            }
        });

        reportHealthyStatus();

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HealthyViewModel.class);
        // TODO: Use the ViewModel
    }

    private void reportHealthyStatus() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HealthCheckApi api = retrofit.create(HealthCheckApi.class);

        // Collecting the symptoms from checkboxes (simplified for example)
        HealthStatusDetails details = new HealthStatusDetails(
                false,
                false,
                new Symptoms(
                        false,
                        false,
                        false,
                        false,
                        false,
                        ""
                ),
                ""
        );

        LoginManager loginManager = new LoginManager(getContext());
        //TODO: Replace test with user's email
        HealthStatus healthStatus = new HealthStatus(loginManager.getEmail(), details);

        Call<Void> call = api.submitHealthStatus(healthStatus);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle success
                } else {
                    // Handle failure
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle error
            }
        });
    }

}