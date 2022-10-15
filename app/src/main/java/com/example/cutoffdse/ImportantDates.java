package com.example.cutoffdse;

import static com.example.cutoffdse.R.id.svSearch;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ImportantDates extends AppCompatActivity {

    private TableLayout tlRow;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_dates);

        tlRow = findViewById(R.id.tlRow);
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("important_dates")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String activity = documentChange.getDocument().getData().get("activity").toString();
                            String start_date = documentChange.getDocument().getData().get("start_date").toString();
                            String end_date = documentChange.getDocument().getData().get("end_date").toString();
                            addRow(activity, start_date, end_date);
                        }
                    }
                });


    }

    private void addRow(String activity, String start_date, String end_date) {

        View view = getLayoutInflater().inflate(R.layout.important_date_row_layout, null, false);

        TextView tvActivity = view.findViewById(R.id.tvActivity);
        TextView tvStartDate = view.findViewById(R.id.tvStartDate);
        TextView tvEndDate = view.findViewById(R.id.tvEndDate);

        tvActivity.setText(activity.trim());
        tvStartDate.setText(start_date.trim());
        tvEndDate.setText(end_date.trim());

        tlRow.addView(view);
    }
}