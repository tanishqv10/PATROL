package com.example.newsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailTV = findViewById(R.id.txtEmail);
        Button forgotPasswordBtn = findViewById(R.id.btnForgotPassowrd);
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    makeForgotPasswordRequest(emailTV.toString());
                }
            }
        });

        // Login instead set up
        TextView lblLogin = findViewById(R.id.lblLogin);
        String text = "Login Instead?";

        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Start a new Activity here
                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };

        int start = text.indexOf("Login Instead?"); // Start index of 'Click here'
        int end = start + "Login Instead?".length(); // End index
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lblLogin.setText(spannableString);
        lblLogin.setMovementMethod(LinkMovementMethod.getInstance()); // This  line is necessary to make the link clickable
    }
    private boolean validateInput() {
        String username = emailTV.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(ForgotPasswordActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Add more validation logic here if needed
        return true;
    }

    private void makeForgotPasswordRequest(String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForgotPasswordActivity.ForgotPasswordService service = retrofit.create(ForgotPasswordActivity.ForgotPasswordService.class);
        Call<Void> forgotPasswordCall = service.forgotPassword(new ForgotPasswordActivity.User(email));

        forgotPasswordCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ForgotPasswordActivity.this, "Forgot password request sent!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(ForgotPasswordActivity.this, "Unable to send request.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrofit API Interface
    interface ForgotPasswordService {
        @retrofit2.http.POST("forgotPassword")
        Call<Void> forgotPassword(@retrofit2.http.Body ForgotPasswordActivity.User user);
    }

    // User class for the request body
    static class User {
        String email;

        User(String email) {
            this.email = email;
        }
    }
}