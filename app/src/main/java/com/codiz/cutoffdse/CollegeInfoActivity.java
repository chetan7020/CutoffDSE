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
import android.widget.Toast;

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

public class CollegeInfoActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private FirebaseFirestore firebaseFirestore;
    private androidx.appcompat.widget.SearchView svSearch;
    private View view;

    private void initialize() {
        linearLayout = findViewById(R.id.llData);

        firebaseFirestore = FirebaseFirestore.getInstance();

        svSearch = findViewById(R.id.svSearch);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initialize();

        firebaseFirestore.collection("collegeInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String instituteCode = String.valueOf(document.get("instituteCode"));
                                String collegeName = String.valueOf(document.get("collegeName"));
                                String link = String.valueOf(document.get("link"));

                                createCard(instituteCode, collegeName, link);
                            }

                            linearLayout.setVisibility(View.VISIBLE);
                            CircularProgressIndicator pbLoader = findViewById(R.id.pbLoader);
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

    private void createCard(String instituteCode, String collegeName, String link) {
        view = getLayoutInflater().inflate(R.layout.college_info_card_design_layout, null, false);

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

        linearLayout.addView(view);
    }

    private void openInBrowser(String link) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }


    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}