package com.elgeekman.alejandro.tracksubscriptions;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.elgeekman.alejandro.tracksubscriptions.MainActivity.Database_Path;

public class estadisticas extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout ;


    private FirebaseAuth mAuth;
    TextView num_netflix, por_netflix, num_data_netflix,por_data_netflix;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    RecyclerView recyclerView;

    RecyclerView.Adapter adapter ;


    List<StadisticasDetails> list1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_view);


        databaseReference = FirebaseDatabase.getInstance().getReference(MainActivity.Database_Path).child("Estadisticas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    StadisticasDetails stadisticasDetails = dataSnapshot.getValue(StadisticasDetails.class);

                    list1.add(stadisticasDetails);




                }




                progressDialog.dismiss();


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                databaseReference.getDatabase();

                progressDialog.setMessage("Revisando base de datos...");

                progressDialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //que hacer despues de 1.5 segundos

                        progressDialog.dismiss();

                        Toast.makeText(estadisticas.this, "Base de datos actualizada", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);

                //Define your work here .

                swipeRefreshLayout.setRefreshing(false);

            }
        });


    }

    @Override
    protected void onStart() {
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        databaseReference.child("Estadisticas").child("Totales").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        netflix();
        spotify();

        super.onStart();
    }

    private void netflix(){
        databaseReference.child("Estadisticas").child("Netflix").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                int value_1 = Integer.parseInt(value);

                num_data_netflix.setText(value);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private void spotify(){
        databaseReference.child("Estadisticas").child("Spotify").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                int value_1 = Integer.parseInt(value);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private void Apple_Music(){
        databaseReference.child("Estadisticas").child("Apple Music").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                int value_1 = Integer.parseInt(value);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
