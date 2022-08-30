package com.example.cutoffdse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView cv1819, cv1920, cv2021, cv2122, cv2223, cvSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        cv1819.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method1819();
            }
        });

        cv1920.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method1920();
            }
        });

        cv2021.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method2021();
            }
        });

        cv2122.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method2122();
            }
        });

        cv2223.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method2223();
            }
        });

        cvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodSettings();
            }
        });

    }

    private void methodSettings() {
        makeToast("Settings");
    }

    private void method2223() {
        makeToast("Coming Soon");
    }

    private void method2122() {
        createDialog("2021-22") ;
    }

    private void method1920() {
        makeToast("Yet to Build - 2019-20"); ;
    }

    private void method2021() {
        makeToast("Yet to Build - 2020-21"); ;
    }

    private void method1819() {
        makeToast("Yet to Build - 2018-19");
    }

    private void createDialog(String year) {
        final int[] checkedItem = {-1};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Cap Round");

        final String[] strings = new String[]{"Cap-1", "Cap-2"};

        builder.setSingleChoiceItems(strings, checkedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                checkedItem[0] = which;
                dialog.dismiss();

                Intent intent = new Intent(MainActivity.this , DataActivity.class) ;
                intent.putExtra("year" , year) ;
                intent.putExtra("cap" , strings[which]) ;
                startActivity(intent) ;
            }
        });

        AlertDialog customAlertDialog = builder.create();
        customAlertDialog.show();
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {

        cv1819 = findViewById(R.id.cv1819);
        cv1920 = findViewById(R.id.cv1920);
        cv2021 = findViewById(R.id.cv2021);
        cv2122 = findViewById(R.id.cv2122);
        cv2223 = findViewById(R.id.cv2223);
        cvSettings = findViewById(R.id.cvSettings);

    }
}