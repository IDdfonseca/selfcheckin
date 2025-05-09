package com.idlogistics.selfcheckin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.idl_logo_744_154) ;
        imageView.setLayoutParams(new Toolbar.LayoutParams(
                350,
                100,
                Gravity.CENTER)

        ); // Centraliza a imagem na Toolbar

        // Adicionar a imagem na Toolbar
        toolbar.addView(imageView);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Encontre o botão no layout
        Button btnClientNivea = findViewById(R.id.btn_client_nivea);

        btnClientNivea.setOnClickListener(v -> {

            NotificationHelper.showTemporaryNotification(
                this,
                "Olá!",
                "Clicou no Botão da Nivea",
                101
            );
        });

        //Encontra o botão no layout
        Button btnClientHenkel = findViewById(R.id.btn_client_henkel);

        btnClientHenkel .setOnClickListener(v -> {

            GlobalData globalData = GlobalData.getInstance();
            globalData.setIdClient("HENOUT");
            String client = globalData.getIdClient();

            // Ação ao clicar no botão
            Intent intent = new Intent( HomeActivity.this, IdentifyActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
