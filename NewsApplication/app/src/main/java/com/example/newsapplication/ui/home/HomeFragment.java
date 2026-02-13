package com.example.newsapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.Articles;
import com.example.newsapplication.DateUtils;
import com.example.newsapplication.LoginManager;
import com.example.newsapplication.HealthCheckFormFragment;
import com.example.newsapplication.NewsActivity;
import com.example.newsapplication.NewsModal;
import com.example.newsapplication.NewsRVAdapter;
import com.example.newsapplication.R;
import com.example.newsapplication.RetrofitAPI;
import com.example.newsapplication.databinding.FragmentHomeBinding;
import com.example.newsapplication.ui.health.HealthFragment;
import com.example.newsapplication.ui.health.HealthyFragment;
import com.example.newsapplication.ui.health.InfectedFragment;
import com.example.newsapplication.ui.symptoms.SymptomsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private Button btnWell, btnUnwell, btnInfected;
    private TextView lblTotalConfirmedCases, lblTotalRecovered, lblTotalDeaths;
    private RecyclerView newsRV;
    private ArrayList<Articles> articlesArrayList;
    private Stats stats;
    private NewsRVAdapter newsRVAdapter;
    private FragmentHomeBinding binding;
    private LineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LoginManager loginManager = new LoginManager(getContext());
        if (!loginManager.isLoggedIn()) {
            Log.e("HomeFragment", "User is not logged in");
        } else {
            Log.i("HomeFragment", "User is logged in with email " + loginManager.getEmail());
        }

        System.out.println("HomeFragment CREATED");

        System.out.println("Fragment Count: " + getActivity().getSupportFragmentManager().getFragments().size());

        for (int i = 0; i < getActivity().getSupportFragmentManager().getFragments().size(); i++) {

            // Print all elements of List
            System.out.println("Fragment id: " + getActivity().getSupportFragmentManager().getFragments().get(i).getId());
            System.out.println("Fragment tag: " + getActivity().getSupportFragmentManager().getFragments().get(i).getTag());
        }

        // Clear out any leftover fragmentrs
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("FRAGMENT_SYMPTOMS");
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        fragment = getActivity().getSupportFragmentManager().findFragmentByTag("FRAGMENT_HEALTH");
        if (fragment != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        // My Health Status
        btnWell = root.findViewById(R.id.btnWell);
        btnUnwell = root.findViewById(R.id.btnUnwell);
        btnInfected = root.findViewById(R.id.btnInfected);

        // COVID Statistics
        lblTotalConfirmedCases = root.findViewById(R.id.lblTotalConfirmedCases);
        lblTotalRecovered = root.findViewById(R.id.lblTotalRecovered);
        lblTotalDeaths = root.findViewById(R.id.lblTotalDeaths);

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

        // Latest News set up
        articlesArrayList = new ArrayList<>();
        newsRV = root.findViewById(R.id.idRVNews);
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, getContext());
        newsRV.setAdapter(newsRVAdapter);
        newsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        getNews();

        // COVID Stats
        getCovidStats();

        // Stats set up
//        statsArrayList = new ArrayList<>();
        chart = root.findViewById(R.id.statsChart);
        getStats();

        return root;
    }

    private void getCovidStats() {

        String BASE_URL = "http://10.0.2.2:3000/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StatsAPI statsAPI = retrofit.create(StatsAPI.class);
        Call<CovidStats> call = statsAPI.fetchCovidStats();

        call.enqueue(new Callback<CovidStats>() {
            @Override
            public void onResponse(Call<CovidStats> call, Response<CovidStats> response) {

                if (response != null) {
                    CovidStats covidStats = response.body();

                    if (covidStats != null) {
                        lblTotalConfirmedCases.setText(covidStats.getTotal_cases());
                        lblTotalRecovered.setText(covidStats.getTotal_recovered());
                        lblTotalDeaths.setText(covidStats.getTotal_deaths());
                    }
                }

            }

            @Override
            public void onFailure(Call<CovidStats> call, Throwable throwable) {
                Toast.makeText(getContext(), "Failed to get news", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getNews() {
        articlesArrayList.clear();
        String BASE_URL = "http://10.0.2.2:3000/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call = retrofitAPI.getAllNews();

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body();
                System.out.println(response.toString());
                ArrayList<Articles> articles  = newsModal.getArticles();
                System.out.println("Sizes: " + articles.size());
                for (int i = 0; i < articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),
                            articles.get(i).getDescription(),
                            articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(),
                            articles.get(i).getContent()));
                }

                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable throwable) {
                Toast.makeText(getContext(), "Failed to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStats() {
//        statsArrayList.clear();
        String BASE_URL = "http://10.0.2.2:3000/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StatsAPI api = retrofit.create(StatsAPI.class);
        Call<Stats> call = api.fetchStats();

        call.enqueue(new Callback<Stats>() {
            @Override
            public void onResponse(Call<Stats> call, Response<Stats> response) {
                System.out.println("fetchStats successful");
                stats = response.body();
                setupChart();
            }

            @Override
            public void onFailure(Call<Stats> call, Throwable throwable) {
                System.out.println("fetchStats failed: " + throwable.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        System.out.println("HomeFragment DESTROY");
    }

    private void setupChart() {
        ArrayList<CountryStats> countryStats = stats.getStats();
        if (countryStats.isEmpty()) return;
        ArrayList<Case> cases = countryStats.get(0).getCases();

        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < cases.size(); i++) {
            // Convert date string to a format that can be used as a float, e.g., timestamp
            Case c = cases.get(i);
            entries.add(new Entry(DateUtils.convertDateToFloat(c.getDate()), c.getNumTotal()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Total Cases");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }


}