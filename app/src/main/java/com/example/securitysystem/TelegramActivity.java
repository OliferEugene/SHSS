package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TelegramActivity extends AppCompatActivity implements View.OnClickListener {

    String status, name, phone, password, surname, textFrom, telegramID;

    int index;

    DBHelper dbHelper;

    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telegram);

        TextView tvHintTg = findViewById(R.id.tvHintTg);
        Button btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);

        Intent statusIntent = getIntent();
        name = statusIntent.getStringExtra("name");
        phone = statusIntent.getStringExtra("phone");
        surname = statusIntent.getStringExtra("surname");
        password = statusIntent.getStringExtra("password");
        status = "noname";

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnDone:

                Log.d(TAG, "Button pressed");
            ExecutorService service = Executors.newFixedThreadPool(3);
            service.execute(new Runnable() {
                public void run() {
                    try {
                        Log.d(TAG, "Thread executed");

                        String line = null, response;
                        URL url = new URL("https://api.telegram.org/bot1854828309:AAEewx6CkPnmOofk5KbnfyF8X3RNvdU5JZY/getUpdates");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                                .getInputStream()));
                        while (rd.readLine() != null) {
                            line += rd.readLine() + "\n";
                        }
                        textFrom = line;
                        Log.d(TAG, line);

                        index = textFrom.lastIndexOf("chat\":{\"id\":");
                        Log.d(TAG, "Last index of: " + index);

                        int indexStart = index + 12;
                        telegramID = "";


                        for (int i = 0; i <= 8; i++) {
                            telegramID += line.charAt(indexStart + i);
                            Log.d(TAG, "Current char: " + line.charAt(indexStart + i));
                        }


                        Log.d(TAG, "Telegram id: " + telegramID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

                try {
                    Log.d(TAG, "Start waiting");
                    TimeUnit.SECONDS.sleep(2);
                    Log.d(TAG, "Stop waiting");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Telegram id after .run(): " + telegramID);

            if (checkId(telegramID)) {
                // create object for data
                ContentValues cv = new ContentValues();
                // connect to DB
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                cv.put("name", name);
                cv.put("surname", surname);
                cv.put("phone", phone);
                cv.put("password", password);
                cv.put("chatID", telegramID);
                cv.put("status", status);

                //inject the record and get it's ID
                long rowID = db.insert("Accounts", null, cv);

                // close DB connection
                dbHelper.close();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Ви не авторизувались у SSHSAuth_bot", Toast.LENGTH_LONG).show();
            }
            break;
            default:
                break;
        }
    }

    class DBHelper extends SQLiteOpenHelper {

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

    public boolean checkId(String infoToCheck) {
        boolean match;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("Accounts", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("chatID");

            do {
                if (infoToCheck.equals(c.getString(idColIndex))) {
                    match = true;
                } else {
                    match = false;
                }
            } while (c.moveToNext() & !match);
            if (match) {
                return false;
            } else {
                return true;
            }
        } else {
            c.close();
        }
        c.close();
        return true;
    }
    }


