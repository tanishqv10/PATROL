package com.example.newsapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InputFragment extends Fragment {

    private EditText editText;
    private Button confirmButton;

    public InputFragment() {
        // Required empty public constructor
    }

    private RetrofitAPI retrofitAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        // Initialize Retrofit API
        retrofitAPI = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitAPI.class);

        editText = view.findViewById(R.id.editTextInput);
        confirmButton = view.findViewById(R.id.buttonConfirm);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editText.getText().toString();
                System.out.println(inputText);
                addFavoritePlace(inputText);
                Toast.makeText(getActivity(), "Added to Favorite places: " + inputText, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void addFavoritePlace(String name) {
        String BASE_URL = "http://10.0.2.2:3000/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginManager loginManager = new LoginManager(getContext());

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<Void> call = retrofitAPI.addFavPlace(
                new FavPlacePost(loginManager.getEmail(),
                        new FavPlace(name)
                )
        );

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    editText.setText("");
                    Log.i("InputFragment", "addFavPlace() OK Response");
                } else {
                    Log.e("InputFragment", "addFavPlace() not OK Response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e("InputFragment", "addFavPlace() failed: " + throwable.getMessage());

            }
        });
    }
}
