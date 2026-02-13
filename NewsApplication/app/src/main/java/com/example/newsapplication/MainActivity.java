package com.example.newsapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView passwordTV;
    private TextView usernameTV;
    private Button loginBtn;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loginManager = new LoginManager(this);
        if (loginManager.isLoggedIn()) {
            // Log the error with the user's email to identify which user encountered the
            // error
            String email = loginManager.getEmail();
            Log.e("AppError", "User is logged in. Email: " + email);
        }

        passwordTV = this.findViewById(R.id.txtPassword);
        usernameTV = this.findViewById(R.id.txtUsername);
        loginBtn = this.findViewById(R.id.btnLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    makeLoginRequest(usernameTV.getText().toString(), passwordTV.getText().toString());
                }
            }
        });

        TextView lblRegister = findViewById(R.id.lblRegister);
        String text = "Don't have a username? Click here to register.";

        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Start a new Activity here
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        };

        int start = text.indexOf("Click here"); // Start index of 'Click here'
        int end = start + "Click here".length(); // End index
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lblRegister.setText(spannableString);
        lblRegister.setMovementMethod(LinkMovementMethod.getInstance()); // This line is necessary to make the link
                                                                         // clickable

        TextView lblForgotPassword = findViewById(R.id.lblForgotPassword);
        lblForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to ForgotPassword Activity
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInput() {
        String username = usernameTV.getText().toString();
        String password = passwordTV.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Username or password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Add more validation logic here if needed
        return true;
    }

    private void makeLoginRequest(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService service = retrofit.create(LoginService.class);
        Call<Void> loginCall = service.login(new User(username, password));
        System.out.println(loginCall.toString());
        loginCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    // TODO: HomeActivity
                    loginManager.setLogin(true, username);
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrofit API Interface
    interface LoginService {
        @retrofit2.http.POST("login")
        Call<Void> login(@retrofit2.http.Body User user);
    }

    // User class for the request body
    static class User {
        String email;
        String password;

        User(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}