package com.example.cutoffdse;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataActivity extends AppCompatActivity {

    private Intent intent;
    private String year, cap;
    private LinearLayout linearLayout;
    private FirebaseFirestore firebaseFirestore;
    private TextView tvInfo;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initialize();

        tvInfo.setText(year + "(" + cap + ")");

        firebaseFirestore.collection(year + "_" + cap)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            String fileName = documentChange.getDocument().getData().get("fileName").toString();
                            createCard(fileName);
                        }
                    }
                });
    }

    private void createCard(String fileName) {

        View card_design = getLayoutInflater().inflate(R.layout.card_design, null, false);
        LinearLayout llCardDesign = card_design.findViewById(R.id.llCardDesign);
        TextView tvFileName = card_design.findViewById(R.id.tvFileName);

        llCardDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tvFileName.getText().toString();
                firebaseFirestore.collection(year + "_" + cap)
                        .whereEqualTo("fileName", name)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentChange documentChange : value.getDocumentChanges()) {
                                    String link = documentChange.getDocument().getData().get("link").toString();
//                                    makeToast(link);
                                    Uri Download_Uri = Uri.parse(link);

                                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                                    DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                    request.setAllowedOverRoaming(false);
                                    request.setTitle(name);
                                    request.setVisibleInDownloadsUi(true);
                                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(link));
                                    request.setMimeType(mimeType);


                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    downloadManager.enqueue(request);

                                }
                            }
                        });
            }
        });

        tvFileName.setText(fileName);
        linearLayout.addView(card_design);
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {

        tvInfo = findViewById(R.id.tvInfo);

        linearLayout = findViewById(R.id.llData);
        intent = getIntent();
        year = intent.getStringExtra("year");
        cap = intent.getStringExtra("cap");

        firebaseFirestore = FirebaseFirestore.getInstance();

    }
}
