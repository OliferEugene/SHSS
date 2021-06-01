package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    EditText etEmail;
    EditText etPassword;
    LinearLayout loginLayout;
    CheckBox chbRememberUser;
    Button btnLogin;
    Button btnSignUp;

    final String LOGIN_TEXT = "Login";
    final String PASSWORD_TEXT = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        loginLayout = findViewById(R.id.loginLayout);

        chbRememberUser = findViewById(R.id.chbRememberUser);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
    }
}