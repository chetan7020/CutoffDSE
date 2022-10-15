package com.example.cutoffdse;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class FCList extends AppCompatActivity {

    private TableLayout tlRow;
    private FirebaseFirestore firebaseFirestore;
    private SearchView svSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fclist);

        tlRow = findViewById(R.id.tlRow);
        firebaseFirestore = FirebaseFirestore.getInstance();
        svSearch = findViewById(R.id.svSearch);


        firebaseFirestore.collection("fc_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String fc_code = documentChange.getDocument().getData().get("fc_code").toString();
                            String fc_name = documentChange.getDocument().getData().get("fc_name").toString();
                            addRow(fc_code, fc_name);
                        }
                        tlRow.setVisibility(View.VISIBLE);

                        CircularProgressIndicator circularProgressIndicator = findViewById(R.id.pbLoader);
                        circularProgressIndicator.setVisibility(View.GONE);
                    }
                });

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                for(int i = 0; i < tlRow.getChildCount(); i++){

                    TextView tvCode = tlRow.getChildAt(i).findViewById(R.id.tvCode);
                    TextView tvName = tlRow.getChildAt(i).findViewById(R.id.tvName);
                    TableRow trRow = tlRow.getChildAt(i).findViewById(R.id.trRow);

                    if (! (tvCode.getText().toString().toLowerCase().trim().contains(query.toLowerCase())
                            || tvName.getText().toString().toLowerCase().trim().contains(query.toLowerCase()))){
                        trRow.setVisibility(View.GONE);
                    }else{
                        trRow.setVisibility(View.VISIBLE);
                    }

                }

                return false;
            }
        });
    }

    private void addRow(String fc_code, String fc_name) {

        View view = getLayoutInflater().inflate(R.layout.fc_lsit_row_layout, null, false);

        TextView tvCode = view.findViewById(R.id.tvCode);
        TextView tvName = view.findViewById(R.id.tvName);

        String fc_name_new = fc_name.replace("-" , "");

        tvCode.setText(fc_code.trim());
        tvName.setText(fc_name_new.trim());

        tlRow.addView(view);
    }
}