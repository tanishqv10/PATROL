package com.example.newsapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import com.example.newsapplication.ui.health.HealthFragment;
import com.example.newsapplication.ui.health.UnhealthyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthCheckFormFragment extends Fragment {
    private CheckBox covidPositiveCheckBox, covidExposedCheckBox;
    private CheckBox feverCheckbox, headacheCheckbox, soreThroatCheckbox, nauseaCheckbox, fatigueCheckbox;
    private EditText otherSymptoms;

    private LoginManager loginManager;
    Button btnHealth, btnSubmit;
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_health_check_form, container, false);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        loginManager = new LoginManager(getContext());

        if (!loginManager.isLoggedIn()) {
            Log.e("HealthCheckFormFragment", "User is not logged in");
        } else {
            Log.i("HealthCheckFormFragment", "User is logged in with email " + loginManager.getEmail());
        }
      
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

        covidPositiveCheckBox = rootView.findViewById(R.id.checkBoxCovidPositive);
        covidExposedCheckBox = rootView.findViewById(R.id.checkBoxCovidExposed);

        feverCheckbox = rootView.findViewById(R.id.checkBoxFever);
        headacheCheckbox = rootView.findViewById(R.id.checkBoxHeadache);
        soreThroatCheckbox = rootView.findViewById(R.id.checkBoxSoreThroat);
        nauseaCheckbox = rootView.findViewById(R.id.checkBoxNausea);
        fatigueCheckbox = rootView.findViewById(R.id.checkBoxFatigue);

        otherSymptoms = rootView.findViewById(R.id.txtOtherSymptoms);

        btnHealth = rootView.findViewById(R.id.btnHealth);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
                submitHealthCheck();
        });

        btnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new HealthStatusFragment();

                for (int i = 0; i < getActivity().getSupportFragmentManager().getFragments().size(); i++) {

                    // Print all elements of List
                    System.out.println("Fragment[Symptoms] id: " + getActivity().getSupportFragmentManager().getFragments().get(i).getId());
                    System.out.println("Fragment[Symptoms] tag: " + getActivity().getSupportFragmentManager().getFragments().get(i).getTag());
                }

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_HEALTH")
                        .addToBackStack("FRAGMENT_HEALTH")
                        .commit();

                BottomNavigationView bottomNavigationView;
                bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_health);

            }
        });

        return rootView;
    }

    private void switchBack() {

    }

    private void submitHealthCheck() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HealthCheckApi api = retrofit.create(HealthCheckApi.class);

        // Collecting the symptoms from checkboxes (simplified for example)
        String symptoms = "Fever, Headache"; // You should dynamically collect these based on checkbox states
        HealthStatusDetails details = new HealthStatusDetails(
                covidPositiveCheckBox.isChecked(),
                covidExposedCheckBox.isChecked(),
                new Symptoms(
                    feverCheckbox.isChecked(),
                    headacheCheckbox.isChecked(),
                    soreThroatCheckbox.isChecked(),
                    nauseaCheckbox.isChecked(),
                    fatigueCheckbox.isChecked(),
                    otherSymptoms.getText().toString()
                ),
                ""
        );
        HealthStatus healthStatus = new HealthStatus(loginManager.getEmail(), details);

        Call<Void> call = api.submitHealthStatus(healthStatus);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle success

                    Fragment fragment = new UnhealthyFragment();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_UNHEALTH")
                            .addToBackStack("FRAGMENT_UNHEALTHY")
                            .commit();

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

    private void getHealthStatuses() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.2.2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HealthCheckApi api = retrofit.create(HealthCheckApi.class);
        LoginManager loginManager = new LoginManager(getContext());
        String email = loginManager.getEmail();
        Call<HealthStatusesModal> call = api.getHealthStatuses(email);

        call.enqueue(new Callback<HealthStatusesModal>() {
            @Override
            public void onResponse(Call<HealthStatusesModal> call, Response<HealthStatusesModal> response) {
                if (response.isSuccessful()) {
                    // Handle success
                } else {
                    // Handle failure
                }
            }

            @Override
            public void onFailure(Call<HealthStatusesModal> call, Throwable t) {
                // Handle error
            }
        });
    }
}