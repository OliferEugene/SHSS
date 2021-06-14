package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ValidationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etPhoneNumber;
    Button btnSubmit;
    DBHelper dbHelper;
    WebView webView;
    boolean correctData = false;
    int valcode;
    String valcodeString, chatID;

    private static final String TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSubmit = findViewById(R.id.btnSubmit);
        webView = findViewById(R.id.webView);

        btnSubmit.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        valcode = randomize();
        valcodeString = String.valueOf(valcode);
        Log.d(TAG, valcodeString);
    }

    @Override
    public void onClick(View v) {

        Log.d(TAG, "Button pressed");
        String phoneNumber = etPhoneNumber.getText().toString();

        if (!phoneNumber.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = db.query("Accounts", null, null, null, null, null, null);

            if (c.moveToFirst()) {
                int phoneColIndex = c.getColumnIndex("phone");
                int chatIdColIndex = c.getColumnIndex("chatID");

                do {
                    if (phoneNumber.equals(c.getString(phoneColIndex))) {
                        correctData = true;
                        chatID = c.getString(chatIdColIndex);
                    }
                    else {
                        correctData = false;
                    }
                } while (c.moveToNext() & !correctData);
            } else {
                c.close();
            }


            if (correctData) {
                         webView.loadUrl("https://api.telegram.org/bot1854828309:AAEewx6CkPnmOofk5KbnfyF8X3RNvdU5JZY/sendMessage?chat_id=" + chatID + "?&text=" + valcodeString);
                         Log.d(TAG, "Message sent: https://api.telegram.org/bot1854828309:AAEewx6CkPnmOofk5KbnfyF8X3RNvdU5JZY/sendMessage?chat_id= " + chatID + "?&text=" + valcodeString);

                try {
                    Log.d(TAG, "Start waiting");
                    TimeUnit.SECONDS.sleep(2);
                    Log.d(TAG, "Stop waiting");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent1 = new Intent(this, NewPasswordActivity.class);
                intent1.putExtra("valcode", valcodeString);
                intent1.putExtra("phoneNumber", phoneNumber);
                startActivity(intent1);
            } else {
                Toast.makeText(this, "Невірний номер!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Введіть номер!", Toast.LENGTH_SHORT).show();
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

    public int randomize() {
        int min = 1000;
        int max = 9999;
        int diff = max - min;
        Random random = new Random();
        int i = random.nextInt(diff + 1);
        i += min;

        return i;
    }
}