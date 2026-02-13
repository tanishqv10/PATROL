package com.example.newsapplication.ui.health;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.newsapplication.R;
import com.example.newsapplication.databinding.FragmentHealthBinding;
import com.example.newsapplication.ui.symptoms.SymptomsFragment;

public class HealthFragment extends Fragment {

    Button btnSymptoms;
    private FragmentHealthBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HealthViewModel healthViewModel =
                new ViewModelProvider(this).get(HealthViewModel.class);

        binding = FragmentHealthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnSymptoms = root.findViewById(R.id.btnSymptoms);

        System.out.println("HealthFragment CREATED");

        for (int i = 0; i < getActivity().getSupportFragmentManager().getFragments().size(); i++) {

            // Print all elements of List
            System.out.println("Fragment id: " + getActivity().getSupportFragmentManager().getFragments().get(i).getId());
            System.out.println("Fragment tag: " + getActivity().getSupportFragmentManager().getFragments().get(i).getTag());
        }

        btnSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new SymptomsFragment();

                //System.out.println("ID " + R.id.nav_host_fragment_activity_main);
                //System.out.println("ID " + R.id.navigation_home);
                //System.out.println("ID " + R.id.navigation_notifications);
                for (int i = 0; i < getActivity().getSupportFragmentManager().getFragments().size(); i++) {

                    // Print all elements of List
                    System.out.println("Fragment id: " + getActivity().getSupportFragmentManager().getFragments().get(i).getId());
                    System.out.println("Fragment tag: " + getActivity().getSupportFragmentManager().getFragments().get(i).getTag());
                }

                System.out.println("Fragment Count[Health]: " + getActivity().getSupportFragmentManager().getFragments().size());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_home, fragment, "FRAGMENT_SYMPTOMS")
                        .addToBackStack("FRAGMENT_SYMPTOMS")
                        .commit();

                System.out.println("Fragment Count[Health]: " + getActivity().getSupportFragmentManager().getFragments().size());

            }
        });

        final TextView textView = binding.textNotifications;
        healthViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        System.out.println("HealthFragment DESTROY");
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this);

    }
}