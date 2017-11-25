package com.elgeekman.alejandro.tracksubscriptions;

/**
 * Created by alejandro on 3/11/17.
 */

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.List;

public class ShowStudentDetailsActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout ;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private FirebaseAnalytics mFirebaseAnalytics;

    AppEventsLogger logger;

    ProgressDialog progressDialog;

    private AdView mAdView;
    private Button btnFullscreenAd;

    ConstraintLayout constraintLayout;
    AnimationDrawable animationDrawable ;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor ;

    public static final String PassString = "Check_App_Status";

    List<StudentDetails> list = new ArrayList<>();

    RecyclerView recyclerView;

    RecyclerView.Adapter adapter ;

    private PopupWindow mPopupWindow;

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_details);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Context mContext = getApplicationContext();
        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_custom_layout);

        mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_view);

        AdRequest adRequest1 = new AdRequest.Builder().build();


        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(ShowStudentDetailsActivity.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.interstitial));

        interstitial.loadAd(adRequest1);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        if(CheckAppIsOpenFirstTimeOrNot()){

            user.sendEmailVerification();

        }
        else {


        }

        constraintLayout = (ConstraintLayout) findViewById(R.id.main_layout1);

        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        animationDrawable.setEnterFadeDuration(2500);

        animationDrawable.setExitFadeDuration(4500);

        animationDrawable.start();

        final RelativeLayout rtl = findViewById(R.id.rtl);



        if (user != null) {


        } else {
            Intent ListSong = new Intent(getApplicationContext(), ShowStudentDetailsActivity.class);
            startActivity(ListSong);

            Toast.makeText(this, "Registrar", Toast.LENGTH_SHORT).show();
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(ShowStudentDetailsActivity.this, R.anim.rotate_around_center_point);
                fab.startAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //que hacer despues de 1.5 segundos



                        Intent ListSong = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(ListSong);
                    }
                }, 1500);


            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(ShowStudentDetailsActivity.this));

        progressDialog = new ProgressDialog(ShowStudentDetailsActivity.this);

        progressDialog.setMessage("Cargando información de la base de datos...");

        progressDialog.show();


        databaseReference = FirebaseDatabase.getInstance().getReference(MainActivity.Database_Path).child("Usuarios").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    StudentDetails studentDetails = dataSnapshot.getValue(StudentDetails.class);

                    list.add(studentDetails);




                }


                adapter = new RecyclerViewAdapter(ShowStudentDetailsActivity.this, list);

                recyclerView.setAdapter(adapter);

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

                        Toast.makeText(ShowStudentDetailsActivity.this, "Base de datos actualizada", Toast.LENGTH_SHORT).show();
                    }
                }, 1500);

                //Define your work here .

                swipeRefreshLayout.setRefreshing(false);

            }
        });




    }
    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Toast.makeText(context, intent.getStringExtra("param"),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        logSentFriendRequestEvent();
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        super.onStart();
    }

    public boolean CheckAppIsOpenFirstTimeOrNot(){

        if(sharedPreferences.getBoolean(PassString,true)){

            sharedEditor.putBoolean(PassString,false);
            sharedEditor.commit();
            sharedEditor.apply();

            // If App open for first time then return true.
            return true;
        }else {

            // If App open second time or already opened previously then return false.
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(ShowStudentDetailsActivity.this,"Settings", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_sugerencia) {
            Intent ListSong = new Intent(getApplicationContext(), email.class);
            startActivity(ListSong);
            return true;
        }
        if (id == R.id.action_perfil) {
            Intent ListSong = new Intent(getApplicationContext(), miperfil.class);
            startActivity(ListSong);
            return true;
        }
        if (id == R.id.action_estadisticas) {
            Intent ListSong = new Intent(getApplicationContext(), estadisticas.class);
            startActivity(ListSong);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    public void logSentFriendRequestEvent () {
        logger.logEvent("sentFriendRequest");
    }










}
