package com.idlogistics.selfcheckin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.idlogistics.selfcheckin.api.ApiService;
import com.idlogistics.selfcheckin.api.RetrofitClient;
import com.idlogistics.selfcheckin.client_henkel.HenkelShipmentActivity;
import com.idlogistics.selfcheckin.driver.DriverRequestModel;
import com.idlogistics.selfcheckin.driver.DriverResponseModel;
import com.idlogistics.selfcheckin.utils.ProgressBarUtil;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IdentifyActivity extends AppCompatActivity {
    private String id_client;
    private String id_driver;
    private String CPF;
    private String CNH;
    private String DocumentType;
    private String fullName;
    private String Nationality;
    private String Transport;
    private String phone;
    private ProgressBar progressBar;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indentify);

        // Usar a classe utilitária para iniciar a barra de progresso
        progressBar = findViewById(R.id.progressBar);
        ProgressBarUtil.startProgressBar(this, progressBar, HomeActivity.class, 120000); // 120 segundos

        // Configurar Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
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

        // Retira o titulo, e deixa somente a imagem
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        Intent it = getIntent();
        CPF = it.getStringExtra("CPF");
        CNH = it.getStringExtra("CNH");
        DocumentType = it.getStringExtra("DocumentType");
        fullName = it.getStringExtra("FullName");
        Nationality = it.getStringExtra("Nationality");

        TextView edit_cnh =findViewById(R.id.edit_cnh);
        TextView edit_cpf = findViewById(R.id.edit_cpf);
        TextView edit_tipo_documento = findViewById(R.id.edit_type_document);
        TextView edit_nome = findViewById(R.id.edit_nome);
        TextView edit_nacionalidade = findViewById(R.id.edit_nacionality);

        edit_cnh.setText(CNH);
        edit_cpf.setText(CPF);
        edit_tipo_documento.setText(DocumentType);
        edit_nome.setText(fullName);
        edit_nacionalidade.setText(Nationality);

        findViewById(R.id.button_confirm).setOnClickListener(v -> {

            sendData();

            NotificationHelper.showTemporaryNotification(
                    this,
                    "Self Check-In",
                    "Dados enviados com sucesso!",
                    101
            );

            // Criar um layout customize para o Toast
            //LayoutInflater inflater = getLayoutInflater();
            //View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_text));

            // Criar e personalizar o Toast
            //Toast toast = new Toast(getApplicationContext());
            //toast.setView(layout);
            //toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 20);
            //toast.setDuration(Toast.LENGTH_SHORT);
            //toast.show();
        });

        findViewById(R.id.button_try_again ).setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), IdCaptureActivity.class);
            ProgressBarUtil.stopProgressBar(progressBar);
            startActivity(intent);
            finish();
        });
    }
    private void sendData() {

        CPF = ( CPF == null || CPF.isEmpty() ) ? "111-111-111-11" : CPF;
        CNH = ( CNH == null || CNH.isEmpty() ) ? "11111111111" : CNH;
        DocumentType = ( DocumentType == null || DocumentType.isEmpty() ) ? "LICENSE DRIVER" : DocumentType;
        fullName = ( fullName == null || fullName.isEmpty() ) ? "NOME COMPLETO DO CABRA" : fullName;

        DriverRequestModel driver = new DriverRequestModel( CPF, CNH, DocumentType, fullName );

        ApiService apiService = RetrofitClient.getApiService();

        Call<DriverResponseModel> call = apiService.storageDriver(driver);

        call.enqueue(new Callback<DriverResponseModel>() {

            public void onResponse(@NonNull Call<DriverResponseModel> call, @NonNull Response<DriverResponseModel> response) {

                if( response.isSuccessful() && response.body() != null ) {

                    DriverResponseModel responseModel = response.body();
                    String response_code = responseModel.getCode();

                    id_driver = responseModel.getCId();
                    CPF = responseModel.getCpf();
                    CNH = responseModel.getCnh();
                    fullName =  responseModel.getNome();
                    Transport =responseModel.getEmpresa();
                    phone = responseModel.getTelefone();

                    Log.e("DEBUG:", Transport );

                    // Obtendo a instância única da classe Singleton
                    GlobalData globalData = GlobalData.getInstance();

                    // Armazenando as variaves no singleton
                    globalData.setIdDriver(id_driver);
                    globalData.setCPF(CPF);
                    globalData.setCNH(CNH);
                    globalData.setDocumentType(DocumentType);
                    globalData.setFullName(fullName);
                    globalData.setNationality(Nationality);
                    globalData.setTransport(Transport);
                    globalData.setPhone(phone);

                    switch (response_code) {

                        case "200":

                            Intent intent = new Intent(getApplicationContext(), HenkelShipmentActivity.class);
                            ProgressBarUtil.stopProgressBar(progressBar);
                            startActivity(intent);
                            finish();
                            break;

                        case "9000":

                            Toast.makeText(IdentifyActivity.this, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            break;

                        default:

                            Toast.makeText(IdentifyActivity.this, ":: Erro " + response_code + " ::", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DriverResponseModel> call, @NonNull Throwable t) {

                Toast.makeText(IdentifyActivity.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
