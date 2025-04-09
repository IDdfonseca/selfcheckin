package com.idlogistics.selfcheckin.client_henkel;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idlogistics.selfcheckin.GlobalData;
import com.idlogistics.selfcheckin.HomeActivity;
import com.idlogistics.selfcheckin.R;
import com.idlogistics.selfcheckin.api.ApiService;
import com.idlogistics.selfcheckin.api.RetrofitClient;
import com.idlogistics.selfcheckin.utils.ProgressBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HenkelShipmentFinishActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_henkel_finish);

        progressBar = findViewById(R.id.progressBar);

        // Usar a classe utilitária para iniciar a barra de progresso
        ProgressBarUtil.startProgressBar(this, progressBar, HomeActivity.class, 10000); // 10 segundos

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.logo_henkel_256_256);
        imageView.setLayoutParams(new Toolbar.LayoutParams(
                450,
                100,
                Gravity.CENTER)

        ); // Centraliza a imagem na Toolbar

        // Configurar Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Adicionar a imagem na Toolbar
        toolbar.addView(imageView);

        // Retira o label de titulo
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
    }
    private void displayShipments(List<HenkelShipmentResponseModel> shipments) {

        RecyclerView recyclerView = findViewById(R.id.recycler_view_shipment_finish);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> shipment_items = new ArrayList<>();

        HenkelShipmentListShipmentReadOnlyAdapter adapter = new HenkelShipmentListShipmentReadOnlyAdapter(shipment_items, this);
        recyclerView.setAdapter(adapter);

        for (HenkelShipmentResponseModel shipment : shipments) {

            shipment_items.add(shipment.getShipment());
        }
    }
}
