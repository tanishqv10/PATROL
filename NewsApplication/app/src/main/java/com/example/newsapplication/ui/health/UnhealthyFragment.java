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

import com.example.newsapplication.HealthStatusFragment;
import com.example.newsapplication.HomeActivity;
import com.example.newsapplication.R;
import com.example.newsapplication.databinding.FragmentHealthyBinding;
import com.example.newsapplication.databinding.FragmentUnhealthyBinding;
import com.example.newsapplication.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UnhealthyFragment extends Fragment {

    private UnhealthyViewModel mViewModel;
    private FragmentUnhealthyBinding binding;
    private Button btnDashboard, btnHealth;
    public static UnhealthyFragment newInstance() {
        return new UnhealthyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        InfectedViewModel infectedViewModel =
                new ViewModelProvider(this).get(InfectedViewModel.class);

        binding = FragmentUnhealthyBinding.inflate(inflater, container, false);
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

        return root;
        //return inflater.inflate(R.layout.fragment_unhealthy, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UnhealthyViewModel.class);
        // TODO: Use the ViewModel
    }

}