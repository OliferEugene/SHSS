package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etPhone;
    EditText etPassword;
    LinearLayout loginLayout;
    CheckBox chbRememberUser;
    Button btnLogin;
    Button btnSignUp;
    Button btnRestorePass;
    SharedPreferences loginData;

    boolean correctData;
    String login, password;

    DBHelper dbHelper;

    final String LOGIN_TEXT = "Login";
    final String PASSWORD_TEXT = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);

        loginLayout = findViewById(R.id.loginLayout);

        chbRememberUser = findViewById(R.id.chbRememberUser);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnRestorePass = findViewById(R.id.btnRestorePass);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRestorePass.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        loadText();
        autoLogin();
    }

    @Override
    public void onClick(View v) {
        login = etPhone.getText().toString();
        password = etPassword.getText().toString();
        correctData = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnSignUp:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:

                Cursor c = db.query("Accounts", null, null, null, null, null, null);

                if (c.moveToFirst()) {
                    int phoneColIndex = c.getColumnIndex("phone");
                    int passwordColIndex = c.getColumnIndex("password");
                    int statusColIndex = c.getColumnIndex("status");

                    do {
                        if (login.equals(c.getString(phoneColIndex)) & password.equals(c.getString(passwordColIndex))) {
                            correctData = true;
                            if (chbRememberUser.isChecked()){
                                saveText();
                            }
                            Intent intent1 = new Intent(this, activity_Menu.class);
                            intent1.putExtra("status", c.getString(statusColIndex));
                            intent1.putExtra("phone", c.getString(phoneColIndex));
                            startActivity(intent1);
                            break;
                        }
                        else {
                            correctData = false;
                        }
                    } while (c.moveToNext() & !correctData);
                    if (!correctData) {
                        Toast.makeText(this, "Невірна комбанація логіну та паролю!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else {
                    c.close();
                    break;
                }
            case R.id.btnRestorePass:
                Intent intent2 = new Intent(this, ValidationActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }

    static class DBHelper extends SQLiteOpenHelper {

            public DBHelper (Context context) {
                // superclass constructor
                super(context, "SHSS", null, 1);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                // create table with fields
                db.execSQL("create table Accounts (id integer primary key autoincrement, name text, surname text, phone text, password text, status text, chatID text);");
            }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    void saveText() {
        loginData = getSharedPreferences("Login data", MODE_PRIVATE);
        SharedPreferences.Editor ed = loginData.edit();
        ed.putString(LOGIN_TEXT, etPhone.getText().toString());
        ed.putString(PASSWORD_TEXT, etPassword.getText().toString());
        ed.apply();
        /*
        If I want to create file with custom name (It will be MyPref in this case)
        Instead of getPreferences(MODE_PRIVATE) method we use getSharedPreferences("My pref", MODE_PRIVATE) method
         */
    }
    /*
    - Use getPreferences if we work with current Activity data and don't want to choose filename
    - Use getSharedPreferences if we save data which is common to multiple activities and choose filename for yourself
    */

    void loadText() {
        loginData = getSharedPreferences("Login data", MODE_PRIVATE);
        String savedLogin = loginData.getString(LOGIN_TEXT, "");
        String savedPassword = loginData.getString(PASSWORD_TEXT, "");
        etPhone.setText(savedLogin);
        etPassword.setText(savedPassword);
        /*
        If I want to load file with custom name (It will be MyPref in this case)
        Instead of getPreferences(MODE_PRIVATE) method we use getSharedPreferences("My pref", MODE_PRIVATE) method
         */
    }

    void autoLogin () {
        login = etPhone.getText().toString();
        password = etPassword.getText().toString();

        if (!login.isEmpty() & !password.isEmpty()) {
            btnLogin.performClick();
        }
    }
}
