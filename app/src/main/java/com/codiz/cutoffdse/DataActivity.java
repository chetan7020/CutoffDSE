package com.codiz.cutoffdse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DataActivity extends AppCompatActivity {

    private Intent intent;
    private String year;
    private LinearLayout linearLayout;
    private FirebaseFirestore firebaseFirestore;
    private TextView tvInfo;
    private androidx.appcompat.widget.SearchView svSearch;
    private View view;
    private CircularProgressIndicator pbLoader;

    private void initialize() {
        tvInfo = findViewById(R.id.tvInfo);

        linearLayout = findViewById(R.id.llData);

        intent = getIntent();
        year = intent.getStringExtra("year");

        firebaseFirestore = FirebaseFirestore.getInstance();

        svSearch = findViewById(R.id.svSearch);

        pbLoader = findViewById(R.id.pbLoader);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initialize();

        tvInfo.setText(year);

        firebaseFirestore.collection(year)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String instituteCode = String.valueOf(document.get("instituteCode"));
                                String collegeName = String.valueOf(document.get("collegeName"));
                                String cap1 = String.valueOf(document.get("cap1"));
                                String cap2 = String.valueOf(document.get("cap2"));

                                createCard(instituteCode, collegeName, cap1, cap2);
                            }

                            linearLayout.setVisibility(View.VISIBLE);
                            pbLoader.setVisibility(View.GONE);

                        } else {
                            Log.d("tag", "Error getting documents: ", task.getException());
                        }
                    }
                });


        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {

                    TextView tvInstituteCode = linearLayout.getChildAt(i).findViewById(R.id.tvInstituteCode);
                    TextView tvCollegeName = linearLayout.getChildAt(i).findViewById(R.id.tvCollegeName);
                    CardView cardView = linearLayout.getChildAt(i).findViewById(R.id.cvCardDesign);

                    if (!(tvInstituteCode.getText().toString().toLowerCase().trim().contains(query.toLowerCase())
                            || tvCollegeName.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))) {

                        cardView.setVisibility(View.GONE);
                    } else {
                        cardView.setVisibility(View.VISIBLE);
                    }

                }

                return false;
            }
        });

    }

    private void createCard(String instituteCode, String collegeName, String cap1, String cap2) {
        view = getLayoutInflater().inflate(R.layout.data_card_design_layout, null, false);

        TextView tvInstituteCode = view.findViewById(R.id.tvInstituteCode);
        TextView tvCollegeName = view.findViewById(R.id.tvCollegeName);

        Button btnMore = view.findViewById(R.id.btnMore);

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("collegeInfo")
                        .document(tvInstituteCode.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                String link = document.get("link").toString();
                                openInBrowser(link);
                            }
                        });
            }
        });

        tvInstituteCode.setText(instituteCode);
        tvCollegeName.setText(collegeName);

        Button btnCap1 = view.findViewById(R.id.btnCap1);
        Button btnCap2 = view.findViewById(R.id.btnCap2);

        btnCap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataActivity.this, PDFActivity.class);
                intent.putExtra("link", cap1);
                startActivity(intent);
            }
        });

        btnCap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataActivity.this, PDFActivity.class);
                intent.putExtra("link", cap2);
                startActivity(intent);
            }
        });

        linearLayout.addView(view);
    }

    private void openInBrowser(String link) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }

}
