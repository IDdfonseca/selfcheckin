package com.idlogistics.selfcheckin.client_henkel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.idlogistics.selfcheckin.GlobalData;
import com.idlogistics.selfcheckin.HomeActivity;
import com.idlogistics.selfcheckin.NotificationHelper;
import com.idlogistics.selfcheckin.R;
import com.idlogistics.selfcheckin.TransportItemAdapter;
import com.idlogistics.selfcheckin.TransportRequestModel;
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

public class HenkelShipmentActivity extends AppCompatActivity {
    private String site = null;
    private ProgressBar progressBar;
    private String id_transport;
    private MaterialAutoCompleteTextView spinnerTransport;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(  R.layout.activity_henkel );

        // Configurar Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Usar a classe utilitária para iniciar a barra de progresso
        progressBar = findViewById(R.id.progressBar);
        ProgressBarUtil.startProgressBar(this, progressBar, HomeActivity.class, 120000); // 120 segundos

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.logo_henkel_256_256) ;
        imageView.setLayoutParams(new Toolbar.LayoutParams(
                230,
                80,
                Gravity.CENTER)

        ); // Centraliza a imagem na Toolbar

        // Adicionar a imagem na Toolbar
        toolbar.addView(imageView);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Obtendo a instância única da classe Singleton
        GlobalData globalData = GlobalData.getInstance();

        String site = globalData.getIdClient();
        String id_driver = globalData.getIdDriver();
        String CPF = globalData.getCPF();
        String CNH = globalData.getCNH();
        String fullName = globalData.getFullName();
        String Phone = globalData.getPhone();
        String Transport = globalData.getTransport();

        // Captura os campos da activity
        EditText editText_CPF = findViewById(R.id.driver_cpf);
        EditText editText_CNH = findViewById(R.id.driver_cnh);
        EditText editText_Name = findViewById(R.id.driver_nome);
        EditText editText_placa = findViewById(R.id.driver_edit_plate);
        EditText editText_telefone = findViewById(R.id.driver_edit_telefone);
        EditText editText_shipment = findViewById(R.id.driver_edit_shipment);

        //Aplica a mascara
        PlacaMaskUtil.applyMask(editText_placa);
        PhoneMaskUtil.applyMask((EditText) editText_telefone);

        spinnerTransport = findViewById(R.id.driver_transport);
        fetchTransport(Transport);

        //Set nos campos os valores, obtidos anteriormente
        editText_CPF.setText(CPF);
        editText_CNH.setText(CNH);
        editText_Name.setText(fullName);
        editText_telefone.setText(Phone);

        // Validação dos campos
        findViewById( R.id.button_shipment).setOnClickListener(v -> {

            if (editText_telefone.getText().toString().trim().isEmpty()) {

                editText_telefone.requestFocus(1);
                editText_telefone.setError("Preencha este campo!");

            } else if(editText_placa.getText().toString().trim().isEmpty()) {

                editText_placa.requestFocus(0);
                editText_placa.setError("Preencha este Campo!");

            } else if (editText_shipment.getText().toString().trim().isEmpty()) {

                editText_shipment.requestFocus(1);
                editText_shipment.setError("Preencha este campo!");

            } else {

                globalData.setPlate( editText_placa.getText().toString() );

                // Trata os dados antes de enviar para a API
                String field_shipment = editText_shipment.getText().toString();
                String valTelefone = PhoneMaskUtil.removeMask(editText_telefone.getText().toString());
                String placa = PlacaMaskUtil.removeMask(editText_placa.getText().toString());

                sendData( field_shipment, id_driver, CPF, fullName, id_transport, valTelefone, placa, site);

                editText_shipment.setText("");
            }
        });

        findViewById( R.id.button_finish).setOnClickListener(v -> {

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
                    Intent it = new Intent(getApplicationContext(), HenkelShipmentConfirmActivity.class);
                    it.putExtra("CPF", CPF);
                    it.putExtra("name", fullName);
                    it.putExtra("plate", editText_placa.getText().toString());
                    it.putExtra("phone", editText_telefone.getText().toString());
                    startActivity(it);
                    dialog.dismiss();
                }
            });

            dialogView.findViewById(R.id.dialog_negative_button).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Ação quando "Não" for clicado
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

    private void fetchTransport(String TransportSelected) {

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<TransportRequestModel>> call = apiService.listTransport();

        call.enqueue(new Callback<List<TransportRequestModel>>() {

            @Override
            public void onResponse(@NonNull Call<List<TransportRequestModel>> call, @NonNull Response<List<TransportRequestModel>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<TransportRequestModel> transportList = response.body();
                    List<TransportItemAdapter> transportItems = new ArrayList<>();

                    for (TransportRequestModel model : transportList) {

                        transportItems.add(new TransportItemAdapter(String.valueOf(model.getIdTransport()), model.getNome()));
                    }

                    ArrayAdapter<TransportItemAdapter> adapter = new ArrayAdapter<>(
                        HenkelShipmentActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        transportItems
                    );

                    spinnerTransport.setAdapter(adapter);

                    // Seleciona item com base no ID
                    for (TransportItemAdapter item : transportItems) {

                        if (item.getId().equals(TransportSelected)) {

                            spinnerTransport.setText(item.getNome(), false);
                            break;
                        }
                    }

                    // Captura do ID selecionado quando o usuário toca
                    spinnerTransport.setOnItemClickListener((parent, view, position, id) -> {

                        TransportItemAdapter selectedItem = (TransportItemAdapter) parent.getItemAtPosition(position);
                        id_transport = selectedItem.getId();
                        //Log.d("TRANSPORTE_ID", "Selecionado ID: " + idSelecionado);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TransportRequestModel>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            View view = getCurrentFocus();

            if (view instanceof EditText) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        return super.dispatchTouchEvent(event);
    }

    private void sendData(String shipment, String id_driver, String cpf, String nome, String Transport, String telefone, String placa, String site  ) {

        HenkelShipmentRequestModel ship = new HenkelShipmentRequestModel( shipment, id_driver, cpf, nome, Transport, telefone, placa, site );

        ApiService apiService = RetrofitClient.getApiService();
        Call<HenkelShipmentRequestModel> call = apiService.getShipment(ship);

        call.enqueue(new Callback<HenkelShipmentRequestModel>() {

            public void onResponse(@NonNull Call<HenkelShipmentRequestModel> call, @NonNull Response<HenkelShipmentRequestModel> response) {

                if( response.isSuccessful() && response.body() != null ) {

                    HenkelShipmentRequestModel responseModel = response.body();
                    String response_message = responseModel.getMessage();

                    NotificationHelper.showTemporaryNotification(
                            getApplicationContext(),
                            "Self Check-In",
                            response_message,
                            101
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Call<HenkelShipmentRequestModel> call, @NonNull Throwable t) {

                NotificationHelper.showTemporaryNotification(
                        getApplicationContext(),
                        "Self Check-In",
                        "Falha: " + t.getMessage(),
                        101
                );
            }
        });
    }
}
