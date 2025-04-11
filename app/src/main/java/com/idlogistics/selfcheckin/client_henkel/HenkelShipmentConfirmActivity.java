package com.idlogistics.selfcheckin.client_henkel;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idlogistics.selfcheckin.GlobalData;
import com.idlogistics.selfcheckin.HomeActivity;
import com.idlogistics.selfcheckin.NotificationHelper;
import com.idlogistics.selfcheckin.R;
import com.idlogistics.selfcheckin.api.ApiService;
import com.idlogistics.selfcheckin.api.RetrofitClient;
import com.idlogistics.selfcheckin.utils.PhoneMaskUtil;
import com.idlogistics.selfcheckin.utils.PlacaMaskUtil;
import com.idlogistics.selfcheckin.utils.ProgressBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HenkelShipmentConfirmActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_henkel_confirm );

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.logo_henkel_256_256) ;
        imageView.setLayoutParams(new Toolbar.LayoutParams(
                230,
                80,
                Gravity.CENTER)

        ); // Centraliza a imagem na Toolbar

        // Usar a classe utilitária para iniciar a barra de progresso
        progressBar = findViewById(R.id.progressBar);
        ProgressBarUtil.startProgressBar(this, progressBar, HomeActivity.class, 120000); // 120 segundos

        // Configurar Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configura o botão de voltar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Adicionar a imagem na Toolbar
        toolbar.addView(imageView);

        // Retira a label de titulo
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Obtendo a instância única da classe Singleton
        GlobalData globalData = GlobalData.getInstance();

        String CPF = globalData.getCPF();
        String name = globalData.getFullName();
        String plate = globalData.getPlate();
        String phone = globalData.getPhone();

        EditText editText_CPF = findViewById(R.id.driver_confirm_cpf);
        EditText editText_Name = findViewById(R.id.driver_confirm_name);
        EditText editText_Phone = findViewById(R.id.driver_confirm_phone);
        EditText editText_Plate = findViewById(R.id.driver_confirm_plate);

        //Aplica a mascara
        PlacaMaskUtil.applyMask(editText_Plate);
        PhoneMaskUtil.applyMask((EditText) editText_Phone);

        editText_CPF.setText(CPF);
        editText_Name.setText(name);
        editText_Phone.setText(phone);
        editText_Plate.setText(plate);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<HenkelShipmentResponseModel>> call = apiService.listShipments(CPF);

        call.enqueue(new Callback<List<HenkelShipmentResponseModel>>() {

            @Override
            public void onResponse(@NonNull Call<List<HenkelShipmentResponseModel>> call, @NonNull Response<List<HenkelShipmentResponseModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<HenkelShipmentResponseModel> shipments = response.body();
                    displayShipments(shipments);

                } else {

                    Log.e("Retrofit", "Erro: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HenkelShipmentResponseModel>> call, @NonNull Throwable t) {

                Log.e("Retrofit", "Falha na requisição", t);
            }
        });

        Button btnBackContinue = findViewById(R.id.button_back);
        Button btnConfirm = findViewById(R.id.button_finish);

        btnBackContinue.setOnClickListener( v ->{

            super.onBackPressed();
        });

        btnConfirm.setOnClickListener( v ->{

            // Inflar o layout personalizado
            View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_confirm_alert_dialog, null);

            // Configurar o AlertDialog com o layout personalizado
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();

            // Configurar os botões do diálogo
            dialogView.findViewById(R.id.dialog_positive_button).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Parar a contagem da barra de progresso
                    ProgressBarUtil.stopProgressBar(progressBar);

                    // Ação quando "Sim" for clicado
                    Intent itcon = new Intent( getApplicationContext(), HenkelShipmentFinishActivity.class );
                    itcon.putExtra("CPF", R.id.driver_confirm_cpf);
                    itcon.putExtra("name", R.id.driver_confirm_name);
                    itcon.putExtra("plate", R.id.driver_confirm_plate);
                    itcon.putExtra("phone", R.id.driver_confirm_phone);
                    startActivity(itcon);
                    dialog.dismiss();
                    finishAffinity();
                }
            });

            dialogView.findViewById(R.id.dialog_negative_button).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    NotificationHelper.showTemporaryNotification(
                            getApplicationContext(),
                            "Self Check-In",
                            "Operação cancelada!",
                            101
                    );

                    dialog.dismiss();
                }
            });

            dialog.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //onBackPressed();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayShipments(List<HenkelShipmentResponseModel> shipments) {

        RecyclerView recyclerView = findViewById(R.id.recycler_view_shipment_confirm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> shipment_items = new ArrayList<>();

        HenkelShipmentListShipmentAdapter adapter = new HenkelShipmentListShipmentAdapter(shipment_items, this);
        recyclerView.setAdapter(adapter);

        //container = findViewById(R.id.container);

        for (HenkelShipmentResponseModel shipment : shipments) {

            shipment_items.add(shipment.getShipment());
        }
    }
}