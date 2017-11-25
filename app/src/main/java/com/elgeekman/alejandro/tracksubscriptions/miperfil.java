package com.elgeekman.alejandro.tracksubscriptions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class miperfil extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miperfil);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        TextView correo = findViewById(R.id.miperfil_correo);
        ImageView imagen = findViewById(R.id.imageView);
        TextView nombre = findViewById(R.id.miperfil_nombre);
        TextView servicio = findViewById(R.id.miperfil_servicio);
        TextView telf = findViewById(R.id.miperfil_numero);
        Button email_ver = findViewById(R.id.miperfil_correoverification);


        correo.setText(user.getEmail());
        Glide.with(this).load(user.getPhotoUrl()).into(imagen);
        nombre.setText(user.getDisplayName());
        servicio.setText("Datos obtenidos gracias a " + user.getProviders());
        telf.setText(user.getPhoneNumber());
        email_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification();
            }
        });




    }
}
