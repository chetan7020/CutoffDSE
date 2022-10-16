package com.codiz.cutoffdse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PDFActivity extends AppCompatActivity {

    private PDFView pdfView;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);

        link = getIntent().getStringExtra("link");

        pdfView = findViewById(R.id.pdfView);

        new Retrieve().execute(link);

    }
    class Retrieve extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            CircularProgressIndicator pbLoader = findViewById(R.id.pbLoader);
            pbLoader.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromStream(inputStream).load();

            super.onPostExecute(inputStream);
        }
    }
}