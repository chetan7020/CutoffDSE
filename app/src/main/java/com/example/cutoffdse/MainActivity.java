package com.example.cutoffdse;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private CardView cvImportantDates, cvListOfFC, cv2122, cv2223, cvCollege;
    private FirebaseFirestore firebaseFirestore;

    private void initialize() {

        firebaseFirestore = FirebaseFirestore.getInstance();

        cvImportantDates = findViewById(R.id.cvImportantDates);
        cvListOfFC = findViewById(R.id.cvListOfFC);
        cv2122 = findViewById(R.id.cv2122);
        cv2223 = findViewById(R.id.cv2223);
        cvCollege = findViewById(R.id.cvCollege);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        cvImportantDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodImportantDates();
            }
        });

        cvListOfFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodListOfFC();
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

        cvCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                methodCollege();
            }
        });

    }

    private void methodCollege() {
        startActivity(new Intent(MainActivity.this , CollegeInfoActivity.class));
    }

    private void method2223() {

        firebaseFirestore.collection("2022-23")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (String.valueOf(value.getDocuments().stream().count()).equals("0")) {
                            makeToast("Coming Soon");
                        } else {
                            Intent intent = new Intent(MainActivity.this, DataActivity.class);
                            intent.putExtra("year", "2022-23");
                            startActivity(intent);
                        }
                    }
                });
    }

    private void method2122() {
        Intent intent = new Intent(MainActivity.this, DataActivity.class);
        intent.putExtra("year", "2021-22");
        startActivity(intent);
    }

    private void methodListOfFC() {
        startActivity(new Intent(MainActivity.this, FCList.class));
    }

    private void methodImportantDates() {
        startActivity(new Intent(MainActivity.this, ImportantDates.class));
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}