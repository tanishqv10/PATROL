package com.example.newsapplication.ui.symptoms;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.newsapplication.R;
import com.example.newsapplication.databinding.FragmentSymptomsBinding;
import com.example.newsapplication.ui.health.HealthFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SymptomsFragment extends Fragment {

    Button btnBack;
    private SymptomsViewModel mViewModel;
    private FragmentSymptomsBinding binding;

    public static SymptomsFragment newInstance() {
        return new SymptomsFragment();
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SymptomsViewModel symptomsViewModel =
                new ViewModelProvider(this).get(SymptomsViewModel.class);

        binding = FragmentSymptomsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        System.out.println("SymptomsFragment CREATED");

        // Clear out any leftover fragments
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("FRAGMENT_HEALTH");
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        btnBack = root.findViewById(R.id.btnBack);

        for (int i = 0; i < getActivity().getSupportFragmentManager().getFragments().size(); i++) {

            // Print all elements of List
            System.out.println("Fragment[Symptoms] id: " + getActivity().getSupportFragmentManager().getFragments().get(i).getId());
            System.out.println("Fragment[Symptoms] tag: " + getActivity().getSupportFragmentManager().getFragments().get(i).getTag());
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("Fragment Count: " + getActivity().getSupportFragmentManager().getFragments().size());

                        //Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);

                        //if (fragment == null)
                        Fragment fragment = new HealthFragment();

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
                        //bottomNavigationView.setonItemSelectedListener (myNavigationItemListener);
                        //bottomNavigationView.setOnItemSelectedListener(myNavigationItemListener);
                        bottomNavigationView.setSelectedItemId(R.id.navigation_health);

                        //getActivity().getSupportFragmentManager().popBackStack();
                        //System.out.println("Fragment Count: " + getActivity().getSupportFragmentManager().getFragments().size());

                    }
                });


        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        System.out.println("SmyptomsFragment DESTROY");
    }

}