package com.example.securitysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class activity_Menu extends AppCompatActivity implements View.OnClickListener {

    Button btnDoor;
    Button btnLight;
    Button btnCondition;
    Button btnCurtains;
    Button btnGas;
    Button btnAdmin;
    Button btnLogOut;

    TextView tvStatus;

    SharedPreferences loginData;

    String status, selfEmail;
    String adminStatus = "Ви увійшли як Адміністратор";
    String staffStatus = "Ви увійшли як Персонал";
    String guestStatus = "Ви увійшли як Гість";
    String nonameStatus = "Ви увійшли як Незнайомець";

    boolean doors = false;
    boolean light = false;
    boolean condition = false;
    boolean curtains = false;
    boolean gas = false;

    boolean doorsBtnAccess;
    boolean lightBtnAccess;
    boolean conditionBtnAccess;
    boolean curtainsBtnAccess;
    boolean gasBtnAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__menu);

        Intent statusIntent = getIntent();
        status = statusIntent.getStringExtra("status");
        selfEmail = statusIntent.getStringExtra("email");


        btnAdmin = (Button) findViewById(R.id.btnAdmin);
        btnDoor = (Button) findViewById(R.id.btnDoor);
        btnLight = (Button) findViewById(R.id.btnLight);
        btnCondition = (Button) findViewById(R.id.btnCondition);
        btnCurtains = (Button) findViewById(R.id.btnCurtains);
        btnGas = (Button) findViewById(R.id.btnGas);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        btnAdmin.setOnClickListener(this);
        btnDoor.setOnClickListener(this);
        btnLight.setOnClickListener(this);
        btnCondition.setOnClickListener(this);
        btnCurtains.setOnClickListener(this);
        btnGas.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        checkStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdmin:

                if (status.equals("admin")) {
                    Intent intent = new Intent(this, AdminActivity.class);
                    intent.putExtra("email", selfEmail);
                    startActivity(intent);
                }
                break;
            case R.id.btnLogOut:
                clearLoginData();
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnDoor:
                if (doorsBtnAccess) {
                    if(!doors) {
                        btnDoor.setText("Зачинити двері");
                        Toast.makeText(this, "Двері відчинено", Toast.LENGTH_SHORT).show();
                        doors = true;
                        break;
                    } else {
                        btnDoor.setText("Відчинити двері");
                        Toast.makeText(this, "Двері зачинено", Toast.LENGTH_SHORT).show();
                    } doors = false;
                } else {
                    Toast.makeText(this, "Немає доступу!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnLight:
                if (lightBtnAccess) {
                    if(!light) {
                        btnLight.setText("Вимкнути світло");
                        Toast.makeText(this, "Світло увімкнуте", Toast.LENGTH_SHORT).show();
                        light = true;
                        break;
                    } else {
                        btnLight.setText("Увімкнути світло");
                        Toast.makeText(this, "Світло вимкнуте", Toast.LENGTH_SHORT).show();
                    } light = false;
                } else {
                    Toast.makeText(this, "Немає доступу!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCondition:
                if (conditionBtnAccess) {
                    if(!condition) {
                        btnCondition.setText("Вимкнути кондиціонер");
                        Toast.makeText(this, "Кондиціонер увімкнено", Toast.LENGTH_SHORT).show();
                        condition = true;
                        break;
                    } else {
                        btnCondition.setText("Увімкнути кондиціонер");
                        Toast.makeText(this, "Кондиціонер вимкнено", Toast.LENGTH_SHORT).show();
                    } condition = false;
                    break;
                } else {
                    Toast.makeText(this, "Немає доступу!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCurtains:
                if (curtainsBtnAccess) {
                    if(!curtains) {
                        btnCurtains.setText("Зсунути штори");
                        Toast.makeText(this, "Штори розсунуті", Toast.LENGTH_SHORT).show();
                        curtains = true;
                        break;
                    } else {
                        btnCurtains.setText("Розсунути штори");
                        Toast.makeText(this, "Штори зсунуті", Toast.LENGTH_SHORT).show();
                    } curtains = false;
                    break;
                } else {
                    Toast.makeText(this, "Немає доступу!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnGas:
                if (gasBtnAccess) {
                    if(!gas) {
                        btnGas.setText("Вимкнути сенсор газу");
                        Toast.makeText(this, "Сенсор газу увімкнено", Toast.LENGTH_SHORT).show();
                        gas = true;
                        break;
                    } else {
                        btnGas.setText("Увімкнути сенсор газу");
                        Toast.makeText(this, "Сенсор газу вимкнено", Toast.LENGTH_SHORT).show();
                    } gas = false;
                    break;
                } else {
                    Toast.makeText(this, "Немає доступу!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    void checkStatus(){
        switch (status) {
            case "admin":
                tvStatus.setText(adminStatus);
                doorsBtnAccess = true;
                conditionBtnAccess = true;
                curtainsBtnAccess = true;
                gasBtnAccess = true;
                lightBtnAccess = true;
                break;
            case "guest":
                tvStatus.setText(guestStatus);
                doorsBtnAccess = false;
                btnDoor.setBackgroundColor(Color.GRAY);

                conditionBtnAccess = false;
                btnCondition.setBackgroundColor(Color.GRAY);

                curtainsBtnAccess = false;
                btnCurtains.setBackgroundColor(Color.GRAY);

                gasBtnAccess = false;
                btnGas.setBackgroundColor(Color.GRAY);

                lightBtnAccess = true;

                btnAdmin.setClickable(false);
                btnAdmin.setVisibility(View.INVISIBLE);
                break;
            case "staff":
                tvStatus.setText(staffStatus);
                doorsBtnAccess = false;
                btnDoor.setBackgroundColor(Color.GRAY);

                conditionBtnAccess = true;
                curtainsBtnAccess = true;

                gasBtnAccess = false;
                btnGas.setBackgroundColor(Color.GRAY);

                lightBtnAccess = true;

                btnAdmin.setClickable(false);
                btnAdmin.setVisibility(View.INVISIBLE);
                break;
            case "noname":
                tvStatus.setText(nonameStatus);
                doorsBtnAccess = false;
                btnDoor.setBackgroundColor(Color.GRAY);

                conditionBtnAccess = false;
                btnCondition.setBackgroundColor(Color.GRAY);

                curtainsBtnAccess = false;
                btnCurtains.setBackgroundColor(Color.GRAY);

                gasBtnAccess = false;
                btnGas.setBackgroundColor(Color.GRAY);

                lightBtnAccess = false;
                btnLight.setBackgroundColor(Color.GRAY);

                btnAdmin.setClickable(false);
                btnAdmin.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    void clearLoginData () {
        loginData = getSharedPreferences("Login data", MODE_PRIVATE);
        SharedPreferences.Editor ed = loginData.edit();
        ed.clear();
        ed.apply();
    }
}