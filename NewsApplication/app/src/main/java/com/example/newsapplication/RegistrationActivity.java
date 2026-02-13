package com.example.newsapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {
    private TextView passwordTV;
    private TextView usernameTV;
    private TextView firstNameTV, lastNameTV, cityTV, phoneTV;
    private EditText dobTV;

    private LoginManager loginManager;

    final Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginManager = new LoginManager(this);
        if (loginManager.isLoggedIn()) {
            // Log the error with the user's email to identify which user encountered the error
            String email = loginManager.getEmail();
            Log.e("AppError", "User is logged in. Email: " + email);
        }

        passwordTV = this.findViewById(R.id.txtPassword);
        usernameTV = this.findViewById(R.id.txtUsername);
        firstNameTV = this.findViewById(R.id.txtFirstName);
        lastNameTV = this.findViewById(R.id.txtLastName);
        dobTV = this.findViewById(R.id.txtDOB);
        cityTV = this.findViewById(R.id.txtCity);
        phoneTV = this.findViewById(R.id.txtPhone);

        Button registerBtn = this.findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    makeLoginRequest(new RegistrationActivity.User(
                            usernameTV.getText().toString(),
                            passwordTV.getText().toString(),
                            firstNameTV.getText().toString(),
                            lastNameTV.getText().toString(),
                            dobTV.getText().toString(),
                            cityTV.getText().toString(),
                            phoneTV.getText().toString())
                    );
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
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };

        int start = text.indexOf("Login Instead?"); // Start index of 'Click here'
        int end = start + "Login Instead?".length(); // End index
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lblLogin.setText(spannableString);
        lblLogin.setMovementMethod(LinkMovementMethod.getInstance()); // This  line is necessary to make the link clickable
    }

    public void showDatePicker(View view) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobTV.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean validateInput() {
        String username = usernameTV.getText().toString();
        String password = passwordTV.getText().toString();
        String firstName = firstNameTV.getText().toString();
        String lastName = lastNameTV.getText().toString();
        String dob = dobTV.getText().toString();
        String city = cityTV.getText().toString();
        String phone = phoneTV.getText().toString();

        // Example validations
        if (password.length() < 8) {
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Email format validation
        if (!username.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}")) {
            Toast.makeText(this, "Username must be a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (!firstName.matches("[a-zA-Z]+")) {
//            Toast.makeText(this, "First Name must contain only letters", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!lastName.matches("[a-zA-Z]+")) {
//            Toast.makeText(this, "First Name must contain only letters", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!city.matches("[a-zA-Z]+")) {
//            Toast.makeText(this, "First Name must contain only letters", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!dob.matches("\\d{2}/\\d{2}/\\d{4}")) {
//            Toast.makeText(this, "Invalid Date of Birth. Format should be MM/dd/yyyy", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (!phone.matches("\\d{3}\\d{3}\\d{4}")) {
//            Toast.makeText(this, "Invalid Phone Number. Format should be 1234567890", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        // Add more validation logic here if needed
        return true;
    }

    private void makeLoginRequest(RegistrationActivity.User user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegistrationActivity.RegisterService service = retrofit.create(RegistrationActivity.RegisterService.class);
        Call<Void> loginCall = service.login(user);
        System.out.println(loginCall.toString());
        loginCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    // TODO: Switch to HomeActivity
                    loginManager.setLogin(true, user.email);
                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrofit API Interface
    interface RegisterService {
        @retrofit2.http.POST("addUser")
        Call<Void> login(@retrofit2.http.Body RegistrationActivity.User user);
    }

    // User class for the request body
    static class User {
        String email;
        String password;

        String fname;
        String lname;
        String dob;
        String city;
        String phone;

        public User(String email, String password, String fname, String lname, String dob, String city, String phone) {
            this.email = email;
            this.password = password;
            this.fname = fname;
            this.lname = lname;
            this.dob = dob;
            this.city = city;
            this.phone = phone;
        }
    }
}