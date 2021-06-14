package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdminSearch, btnAdminStatus, btnStaffStatus, btnGuestStatus, btnNonameStatus;
    TextView tvAdminHint, tvName, tvSurname, tvEmail;
    EditText etAdminSearch;

    String email, status, selfEmail;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Intent statusIntent = getIntent();
        selfEmail = statusIntent.getStringExtra("email");

        btnAdminSearch = (Button) findViewById(R.id.btnAdminSeearch);
        btnAdminStatus = (Button) findViewById(R.id.btnAdminStatus);
        btnStaffStatus = (Button) findViewById(R.id.btnStaffStatus);
        btnGuestStatus = (Button) findViewById(R.id.btnGuestStatus);
        btnNonameStatus = (Button) findViewById(R.id.btnNonameStatus);

        btnAdminSearch.setOnClickListener(this);
        btnAdminStatus.setOnClickListener(this);
        btnStaffStatus.setOnClickListener(this);
        btnGuestStatus.setOnClickListener(this);
        btnNonameStatus.setOnClickListener(this);

        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvSurname = (TextView) findViewById(R.id.tvSurname);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAdminHint = (TextView) findViewById(R.id.tvAdminHint);

        etAdminSearch = (EditText) findViewById(R.id.etAdminSearch);

        dbHelper = new DBHelper(this);

        makeVisibility(false);

    }

    public void onClick(View v) {
        email = etAdminSearch.getText().toString();
        boolean match = false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnAdminSeearch:

                Cursor c = db.query("Accounts", null, null, null, null, null, null);

                if (c.moveToFirst()) {
                    int emailColIndex = c.getColumnIndex("email");
                    int nameColIndex = c.getColumnIndex("name");
                    int statusColIndex = c.getColumnIndex("status");
                    int surnameColIndex = c.getColumnIndex("surname");

                    do {
                        if (email.equals(c.getString(emailColIndex))) {
                            match = true;
                            break;
                        }
                        else {
                            match = false;
                        }
                    } while (c.moveToNext() & !match);
                    if (!match) {
                        Toast.makeText(this, "Користувач з таким E-Mail не зареєстрований!", Toast.LENGTH_SHORT).show();
                        makeVisibility(false);
                        break;
                    } else if (c.getString(emailColIndex).equals(selfEmail)) {
                        Toast.makeText(this, "Ви не можете керувати правами доступу свого облікового запису!", Toast.LENGTH_SHORT).show();
                        makeVisibility(false);
                        break;
                    } else {
                        tvEmail.setText(c.getString(emailColIndex));
                        tvName.setText(c.getString(nameColIndex));
                        tvSurname.setText(c.getString(surnameColIndex));
                        status = c.getString(statusColIndex);

                        makeVisibility(true);
                        changeButtonsColors(status);
                        break;
                    }
                } else {
                    c.close();
                    break;
                }
            case R.id.btnAdminStatus:
                changeButtonsColors("admin");
                Toast.makeText(this, "Рівень доступу змінено на" + " " + btnAdminStatus.getText().toString(), Toast.LENGTH_SHORT).show();
                changeStatus("admin");
                break;
            case R.id.btnStaffStatus:
                changeButtonsColors("staff");
                Toast.makeText(this, "Рівень доступу змінено на" + " " + btnStaffStatus.getText().toString(), Toast.LENGTH_SHORT).show();
                changeStatus("staff");
                break;
            case R.id.btnGuestStatus:
                changeButtonsColors("guest");
                Toast.makeText(this, "Рівень доступу змінено на" + " " + btnGuestStatus.getText().toString(), Toast.LENGTH_SHORT).show();
                changeStatus("guest");
                break;
            case R.id.btnNonameStatus:
                changeButtonsColors("noname");
                Toast.makeText(this, "Рівень доступу змінено на" + " " + btnNonameStatus.getText().toString(), Toast.LENGTH_SHORT).show();
                changeStatus("noname");
                break;
            default:
                break;
        }
    }

    static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // superclass constructor
            super(context, "accountList", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create table with fields
            db.execSQL("create table Accounts (id integer primary key autoincrement, name text, surname text, email text, password text, status text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void makeVisibility (boolean arg) {
        if (arg) {
            btnAdminStatus.setVisibility(View.VISIBLE);
            btnStaffStatus.setVisibility(View.VISIBLE);
            btnGuestStatus.setVisibility(View.VISIBLE);
            btnNonameStatus.setVisibility(View.VISIBLE);

            tvEmail.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.VISIBLE);
            tvSurname.setVisibility(View.VISIBLE);
        } else {
            tvEmail.setVisibility(View.INVISIBLE);
            tvName.setVisibility(View.INVISIBLE);
            tvSurname.setVisibility(View.INVISIBLE);

            btnAdminStatus.setVisibility(View.INVISIBLE);
            btnStaffStatus.setVisibility(View.INVISIBLE);
            btnGuestStatus.setVisibility(View.INVISIBLE);
            btnNonameStatus.setVisibility(View.INVISIBLE);
        }
    }

    public void changeButtonsColors (String status) {
        switch(status) {
            case "admin":
                btnAdminStatus.setBackgroundColor(Color.CYAN);
                btnStaffStatus.setBackgroundColor(Color.GRAY);
                btnGuestStatus.setBackgroundColor(Color.GRAY);
                btnNonameStatus.setBackgroundColor(Color.GRAY);
                break;
            case "staff":
                btnAdminStatus.setBackgroundColor(Color.GRAY);
                btnStaffStatus.setBackgroundColor(Color.CYAN);
                btnGuestStatus.setBackgroundColor(Color.GRAY);
                btnNonameStatus.setBackgroundColor(Color.GRAY);
                break;
            case "guest":
                btnAdminStatus.setBackgroundColor(Color.GRAY);
                btnStaffStatus.setBackgroundColor(Color.GRAY);
                btnGuestStatus.setBackgroundColor(Color.CYAN);
                btnNonameStatus.setBackgroundColor(Color.GRAY);
                break;
            case "noname":
                btnAdminStatus.setBackgroundColor(Color.GRAY);
                btnStaffStatus.setBackgroundColor(Color.GRAY);
                btnGuestStatus.setBackgroundColor(Color.GRAY);
                btnNonameStatus.setBackgroundColor(Color.CYAN);
                break;
            default:
                break;
        }
    }

    public void changeStatus (String status) {

        ContentValues cv = new ContentValues();
        String emailToChange = tvEmail.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put("status", status);

        int updCount = db.update("Accounts", cv, "email = ?",
                new String[] { emailToChange });

        dbHelper.close();
    }

}