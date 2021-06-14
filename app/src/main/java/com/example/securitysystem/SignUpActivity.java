package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    EditText etName;
    EditText etSurname;
    EditText etPhone;
    EditText etPassword;
    EditText etConfirmPassword;
    String name, surname, phone, password, conPass, status;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

        // creates object to manage DB versions
        dbHelper = new DBHelper(this);

    }

    @Override
    public void onClick(View v) {
        //get data from fields
        name = etName.getText().toString();
        surname = etSurname.getText().toString();
        phone = etPhone.getText().toString();
        password = etPassword.getText().toString();
        conPass = etConfirmPassword.getText().toString();

        switch (v.getId()) {

            case R.id.btnSubmit:

                if (checkFields()) {
                    if (checkEmail()) {
                        if (checkPasswords()) {
                            Intent intent1 = new Intent(this, TelegramActivity.class);
                            intent1.putExtra("name", name );
                            intent1.putExtra("surname", surname);
                            intent1.putExtra("phone", phone);
                            intent1.putExtra("password", password);
                            startActivity(intent1);
                            break;
                        } else {
                            Toast.makeText(this, "Паролі не збігаються!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Користувач з таким номером телефону вже зареєстрований!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                        Toast.makeText(this, "Заповніть усі поля!", Toast.LENGTH_SHORT).show();
                    }

                break;
            default:
                break;
        }
    }

    public boolean checkFields () {
        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || password.isEmpty() || conPass.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkPasswords() {
        if (password.equals(conPass)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmail() {

        boolean match;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("Accounts", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int phoneColIndex = c.getColumnIndex("phone");

            do {
                if (phone.equals(c.getString(phoneColIndex))) {
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
        return true;
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


}