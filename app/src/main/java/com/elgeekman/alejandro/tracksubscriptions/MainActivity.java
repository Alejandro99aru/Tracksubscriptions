package com.elgeekman.alejandro.tracksubscriptions;

/**
 * Created by alejandro on 3/11/17.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    Button SubmitButton ;

    Button DisplayButton;

    EditText NameEditText, PhoneNumberEditText;

    Spinner spinner;

    TextView textView, textView2, textView3,textView4 ;

    // Declaring String variable ( In which we are storing firebase server URL ).
    public static final String Firebase_Server_URL = "https://chatchat-7eccd.firebaseio.com/";

    // Declaring String variables to store name & phone number get from EditText.
    String NameHolder, NumberHolder, ColorHolder, MONEDAHEADER, FechaHolder, VisitasHolder;

    Firebase firebase;
    Button bfecha,bhora;
    EditText efecha,ehora;
    private  int dia,mes,ano,hora,minutos;
    DatabaseReference databaseReference, databaseReference1;
    private RewardedVideoAd mRewardedVideoAd;

    // Root Database Name for Firebase Database.
    public static final String Database_Path = "Student_Details_Database";

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    String[] SPINNER_DATA = {"Netflix","Spotify","Apple Music","HBO", "Google Drive","Dribble","Dropbox","Evernote","iCloud","Vodafone","Playstation Plus","Basecamp","Slack","Verizon","ING","Creative Cloud","Microsoft OneDrive","Deezer","Google Play Music","American Express","Sketch","Servicio personalizado"};
    String[] SPINNER_MONEDA_DATA = {"Euro","Dolar","Libra","Peso Mexicano"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(MainActivity.this);

        bfecha=(Button)findViewById(R.id.bfecha);
        bhora=(Button)findViewById(R.id.bhora);
        efecha=(EditText)findViewById(R.id.efecha1);
        ehora=(EditText)findViewById(R.id.ehora);
        bfecha.setOnClickListener(this);
        bhora.setOnClickListener(this);



        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(),
                        "Ad loaded.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(),
                        "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(getBaseContext(),
                        "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(),
                        "Ad closed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(getBaseContext(),
                        "Ad triggered reward.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(),
                        "Ad left application.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(),
                        "Ad failed to load.", Toast.LENGTH_SHORT).show();
            }

        });

        final EditText servicio_personalizado = findViewById(R.id.edt_personalizado);
        mAuth = FirebaseAuth.getInstance();

        firebase = new Firebase(Firebase_Server_URL);

        AdRequest adRequest1 = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.interstitial));

        interstitial.loadAd(adRequest1);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);


        SubmitButton = (Button)findViewById(R.id.submit);
        final Button remove_btn = findViewById(R.id.btn_remove1);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        NameEditText = (EditText)findViewById(R.id.name);

        spinner = findViewById(R.id.spinner);

        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);


        final Spinner spn_moneda = findViewById(R.id.spn_moneda);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        spinner.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_MONEDA_DATA);

        spn_moneda.setAdapter(adapter1);

        PhoneNumberEditText = (EditText)findViewById(R.id.phone_number);

        DisplayButton = (Button)findViewById(R.id.DisplayButton);

        //Adding setOnItemSelectedListener method on spinner.

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String SpinnerValue = (String) spinner.getSelectedItem();
                switch (SpinnerValue){
                    case "Servicio Personalizado":
                        servicio_personalizado.setVisibility(View.VISIBLE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });



        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StudentDetails studentDetails = new StudentDetails();

                String SpinnerValue = (String) spinner.getSelectedItem();
                switch(SpinnerValue){

                    case "Netflix":
                        textView.setText("Netflix");
                        textView2.setText("#F44336");
                        textView4.setText("1");

                        Toast.makeText(MainActivity.this,R.string.netflix_servicios, Toast.LENGTH_LONG).show();

                        break;

                    case "Spotify":
                        textView.setText("Spotify");
                        textView2.setText("#388E3C");
                        Toast.makeText(MainActivity.this,R.string.spotify_servicios, Toast.LENGTH_LONG).show();

                        break;

                    case "Apple Music":
                        textView.setText("Apple Music");
                        textView2.setText("#000000");
                        Toast.makeText(MainActivity.this,R.string.apple_music_servicios, Toast.LENGTH_LONG).show();

                        break;

                    case "HBO":
                        textView.setText("HBO");
                        textView2.setText("#000000");
                        Toast.makeText(MainActivity.this,R.string.hbo_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Google Drive":
                        textView.setText("Google Drive");
                        textView2.setText("#388E3C");
                        Toast.makeText(MainActivity.this,R.string.drive_servicios, Toast.LENGTH_LONG).show();

                        break;

                    case "Dribble":
                        textView.setText("Dribble");
                        textView2.setText("#D500F9");
                        Toast.makeText(MainActivity.this,R.string.dribble_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Dropbox":
                        textView.setText("Dropbox");
                        textView2.setText("#039BE5");
                        Toast.makeText(MainActivity.this,R.string.dropbox_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Evernote":
                        textView.setText("Evernote");
                        textView2.setText("#68EFAD");
                        Toast.makeText(MainActivity.this,R.string.evernote_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "iCloud":
                        textView.setText("iCloud");
                        textView2.setText("#039BE5");
                        Toast.makeText(MainActivity.this,R.string.icloud_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Vodafone":
                        textView.setText("Vodafone");
                        textView2.setText("#F44336");
                        Toast.makeText(MainActivity.this,R.string.vodafone_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Playstation Plus":
                        textView.setText("Playstation Plus");
                        textView2.setText("#01579B");
                        Toast.makeText(MainActivity.this,R.string.playstation_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Basecamp":
                        textView.setText("Basecamp");
                        textView2.setText("#AED581");
                        Toast.makeText(MainActivity.this,R.string.basecamp_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Slack":
                        textView.setText("Slack");
                        textView2.setText("#69F0AE");
                        Toast.makeText(MainActivity.this,R.string.slack_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Verizon":
                        textView.setText("Verizon");
                        textView2.setText("#F44336");
                        Toast.makeText(MainActivity.this,R.string.verizon_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "ING":
                        textView.setText("ING");
                        textView2.setText("#FFA000");
                        Toast.makeText(MainActivity.this,R.string.ing_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Creative Cloud":
                        textView.setText("Creative Cloud");
                        textView2.setText("#F44336");
                        Toast.makeText(MainActivity.this,R.string.adobe_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Microsoft OneDrive":
                        textView.setText("Microsoft OneDrive");
                        textView2.setText("#0288D1");
                        Toast.makeText(MainActivity.this,R.string.onedrive_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Deezer":
                        textView.setText("Deezer");
                        textView2.setText("#000000");
                        Toast.makeText(MainActivity.this,R.string.deezer_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Google Play Music":
                        textView.setText("Google Play Music");
                        textView2.setText("#FFA000");
                        Toast.makeText(MainActivity.this,R.string.ply_music_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "American Express":
                        textView.setText("American Express");
                        textView2.setText("#0288D1");
                        Toast.makeText(MainActivity.this,R.string.americanexpress_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Sketch":
                        textView.setText("Sketch");
                        textView2.setText("#FFA000");
                        Toast.makeText(MainActivity.this,R.string.sketch_servicios, Toast.LENGTH_LONG).show();

                        break;
                    case "Servicio Personalizado":
                        String a = String.valueOf(servicio_personalizado.getText());
                        textView.setText(a);

                        break;
                }



                String SpinnerValue1 = (String) spn_moneda.getSelectedItem();
                switch(SpinnerValue1) {

                    case "Euro":
                        textView3.setText("€");
                        break;
                    case "Dolar":
                        textView3.setText("$");
                        break;
                    case "Libra":
                        textView3.setText("£");
                        break;
                    case "Peso Mexicano":
                        textView3.setText("$");
                        break;
                }

                GetDataFromEditText();



                // Adding name into class function object.
                studentDetails.setStudentName(NameHolder);

                // Adding phone number into class function object.
                studentDetails.setStudentPhoneNumber(NumberHolder);

                studentDetails.setColor(ColorHolder);

                studentDetails.setMoneda(MONEDAHEADER);

                studentDetails.setFecha(FechaHolder);

                studentDetails.setVisitas(VisitasHolder);

                // Getting the ID from firebase database.
                String StudentRecordIDFromServer = databaseReference.push().getKey();

                FirebaseUser user = mAuth.getCurrentUser();

                databaseReference.child("Estadisticas").child(textView.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        int value_1 = Integer.parseInt(value);
                        int value_2 = value_1 + 1;
                        String value_3 = String.valueOf(value_2);
                        databaseReference.child("Estadisticas").child(textView.getText().toString()).setValue(value_3);




                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                databaseReference.child("Estadisticas").child("Totales").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        int value_1 = Integer.parseInt(value);
                        int value_2 = value_1 + 1;
                        String value_3 = String.valueOf(value_2);
                        databaseReference.child("Estadisticas").child("Totales").setValue(value_3);



                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                // Adding the both name and number values using student details class object using ID.
                databaseReference.child("Usuarios").child(user.getUid()).child(textView.getText().toString()).setValue(studentDetails);



                Intent intent = new Intent(MainActivity.this, ShowStudentDetailsActivity.class);
                startActivity(intent);

                // Showing Toast message after successfully data submit.



            }
        });

        DisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ShowStudentDetailsActivity.class);
                startActivity(intent);

            }
        });


        remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentDetails studentDetails = new StudentDetails();
                FirebaseUser user = mAuth.getCurrentUser();

                String SpinnerValue = (String) spinner.getSelectedItem();
                switch(SpinnerValue){

                    case "Netflix":

                        databaseReference.child("Usuarios").child(user.getUid()).child("Netflix").removeValue();
                        Toast.makeText(MainActivity.this,R.string.netflix_servicios_elm, Toast.LENGTH_LONG).show();

                        break;

                    case "Spotify":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Spotify").removeValue();
                        Toast.makeText(MainActivity.this,R.string.spotify_servicios_elm, Toast.LENGTH_LONG).show();

                        break;

                    case "Apple Music":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Apple Music").removeValue();
                        Toast.makeText(MainActivity.this,R.string.apple_music_servicios_elm, Toast.LENGTH_LONG).show();

                        break;

                    case "HBO":
                        databaseReference.child("Usuarios").child(user.getUid()).child("HBO").removeValue();
                        Toast.makeText(MainActivity.this,R.string.hbo_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Google Drive":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Google Drive").removeValue();
                        Toast.makeText(MainActivity.this,R.string.drive_servicios_elm, Toast.LENGTH_LONG).show();

                        break;

                    case "Dribble":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Dribble").removeValue();
                        Toast.makeText(MainActivity.this,R.string.dribble_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Dropbox":

                        databaseReference.child("Usuarios").child(user.getUid()).child("Dropbox").removeValue();
                        Toast.makeText(MainActivity.this,R.string.dropbox_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Evernote":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Evernote").removeValue();
                        Toast.makeText(MainActivity.this,R.string.evernote_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "iCloud":
                        databaseReference.child("Usuarios").child(user.getUid()).child("iCloud").removeValue();
                        Toast.makeText(MainActivity.this,R.string.icloud_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Vodafone":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Vodafone").removeValue();
                        Toast.makeText(MainActivity.this,R.string.vodafone_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Playstation Plus":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Playstation Plus").removeValue();
                        Toast.makeText(MainActivity.this,R.string.playstation_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Basecamp":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Basecamp").removeValue();
                        Toast.makeText(MainActivity.this,R.string.basecamp_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Slack":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Slack").removeValue();
                        Toast.makeText(MainActivity.this,R.string.slack_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Verizon":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Verizon").removeValue();
                        Toast.makeText(MainActivity.this,R.string.verizon_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "ING":
                        databaseReference.child("Usuarios").child(user.getUid()).child("ING").removeValue();
                        Toast.makeText(MainActivity.this,R.string.ing_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Creative Cloud":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Creative Cloud").removeValue();
                        Toast.makeText(MainActivity.this,R.string.adobe_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Microsoft OneDrive":
                        databaseReference.child("Usuarios").child("Usuarios").child(user.getUid()).child("Microsoft OneDrive").removeValue();
                        Toast.makeText(MainActivity.this,R.string.onedrive_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Deezer":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Deezer").removeValue();
                        Toast.makeText(MainActivity.this,R.string.deezer_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Google Play Music":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Google Play Music").removeValue();
                        Toast.makeText(MainActivity.this,R.string.ply_music_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "American Express":
                        databaseReference.child("Usuarios").child(user.getUid()).child("American Express").removeValue();
                        Toast.makeText(MainActivity.this,R.string.americanexpress_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Sketch":
                        databaseReference.child("Usuarios").child(user.getUid()).child("Sketch").removeValue();
                        Toast.makeText(MainActivity.this,R.string.sketch_servicios_elm, Toast.LENGTH_LONG).show();

                        break;
                    case "Servicio Personalizado":
                        String a = String.valueOf(servicio_personalizado.getText());
                        Toast.makeText(MainActivity.this,R.string.americanexpress_servicios, Toast.LENGTH_LONG).show();

                        textView.setText(a);

                        break;


                }

                Intent intent = new Intent(MainActivity.this, ShowStudentDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void GetDataFromEditText(){

        NameHolder = textView.getText().toString().trim();

        NumberHolder = PhoneNumberEditText.getText().toString().trim();

        ColorHolder = textView2.getText().toString().trim();

        MONEDAHEADER = textView3.getText().toString().trim();

        FechaHolder = efecha.getText().toString().trim();

        VisitasHolder = textView4.getText().toString().trim();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void updateUI(FirebaseUser currentUser) {
    }

    @Override
    public void onClick(View v) {
        if(v==bfecha){
            final Calendar c= Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int a = R.string.decadames;
                    efecha.setText(dayOfMonth + " de cada mes");
                }
            }
                    ,dia,mes,ano);
            datePickerDialog.show();
        }
        if (v==bhora){
            final Calendar c= Calendar.getInstance();
            hora=c.get(Calendar.HOUR_OF_DAY);
            minutos=c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    ehora.setText(hourOfDay+":"+minute);
                }
            },hora,minutos,false);
            timePickerDialog.show();
        }
    }
    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
