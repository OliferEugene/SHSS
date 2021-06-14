package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    String valcode, phoneNumber;
    private static final String TAG = "myLogs";

    EditText etValcode, etNewPass, etNewPassConf;
    Button btnSubmitNewPass;

    String userValcode, newPass, newPassConf;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        Intent statusIntent = getIntent();
        valcode = statusIntent.getStringExtra("valcode");
        phoneNumber = statusIntent.getStringExtra("phoneNumber");
        Log.d(TAG, "Valcode received: " + valcode);

        etValcode = findViewById(R.id.etValCode);
        etNewPass = findViewById(R.id.etNewPass);
        etNewPassConf = findViewById(R.id.etNewPassConf);
        btnSubmitNewPass = findViewById(R.id.btnSubmitNewPass);

        btnSubmitNewPass.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick (View v) {
        userValcode = etValcode.getText().toString();
        newPass = etNewPass.getText().toString();
        newPassConf = etNewPassConf.getText().toString();
        if (userValcode.isEmpty()) {
            Toast.makeText(this, "Введіть код підтвердження!", Toast.LENGTH_SHORT).show();
        } else {
            if (newPass.isEmpty()) {
                Toast.makeText(this, "Введіть новий пароль!", Toast.LENGTH_SHORT).show();
            } else {
                if (newPassConf.isEmpty()) {
                    Toast.makeText(this, "Підтвердіть пароль!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!newPass.equals(newPassConf)) {
                        Toast.makeText(this, "Паролі не співпадають!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!userValcode.equals(valcode)) {
                            Toast.makeText(this, "Невірний код підтвердження!", Toast.LENGTH_SHORT).show();
                        } else {
                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            Log.d(TAG, "--- Update SHSS: ---");
                            cv.put("password", newPass);
                            int updCount = db.update("Accounts", cv, "phone = ?",
                                    new String[] { phoneNumber });
                            Log.d(TAG, "updated rows count = " + updCount);
                            dbHelper.close();
                            Intent intent1 = new Intent(this, MainActivity.class);
                            startActivity(intent1);
                        }

                    }
                }
            }
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
}