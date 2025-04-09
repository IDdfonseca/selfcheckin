package com.idlogistics.selfcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Alterando a aparência para tela FULL SCREEN
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Redirecionando para HomeActivity após 3 segundos
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent( MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Finaliza a activity atual

            }

        }, 1000); // 3000ms = 3 segundos

    }
}