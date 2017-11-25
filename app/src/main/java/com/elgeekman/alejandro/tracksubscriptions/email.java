package com.elgeekman.alejandro.tracksubscriptions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class email extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        sendEmail();
        super.onStart();
    }
    @SuppressLint("LongLogTag")
    protected void sendEmail() {
        Log.i("Enviar email", "");

        String[] TO = {"alejandro99aru@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sugerencia App TrackSubscriptions");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe aquí tu sugerencia...");

        try {
            startActivity(Intent.createChooser(emailIntent, "Envía email..."));
            finish();
            Log.i("Terminado enviar email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(email.this,
                    "No hay cliente instalado.", Toast.LENGTH_SHORT).show();
        }
    }
}
