package com.idlogistics.selfcheckin.client_henkel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.idlogistics.selfcheckin.NotificationHelper;
import com.idlogistics.selfcheckin.R;
import com.idlogistics.selfcheckin.api.ApiService;
import com.idlogistics.selfcheckin.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HenkelShipmentListShipmentAdapter extends RecyclerView.Adapter<HenkelShipmentListShipmentAdapter.ViewHolder> {
    private Context context;
    private List<String> items;
    public HenkelShipmentListShipmentAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String label_item = " SHIPMENT : ";
        String item = items.get(position);
        holder.itemText.setText(String.format("%s%s", label_item, item));

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Inflar o layout personalizado
                View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_delete_alert_dialog, null);

                // Configurar o AlertDialog com o layout personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);

                AlertDialog dialog = builder.create();

                // Configurar os botões do diálogo
                dialogView.findViewById(R.id.dialog_positive_button).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // Implementar a lógica de deletar do banco de dados
                        deleteItemFromDatabase(item, position, new ResponseCallback() {

                            @Override
                            public void onResponse(boolean responseAction, int position) {
                                if (responseAction) {
                                    items.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, items.size());
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.dialog_negative_button).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public interface ResponseCallback {
        void onResponse(boolean responseAction, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemIcon;
        public TextView itemText;
        public ImageView deleteIcon;

        public ViewHolder(View view) {

            super(view);
            itemIcon = view.findViewById(R.id.item_icon);
            itemText = view.findViewById(R.id.item_text);
            deleteIcon = view.findViewById(R.id.delete_icon);
        }
    }

    private void deleteItemFromDatabase(String item, int position, ResponseCallback callback) {
        HenkelShipmentRequestDeleteModel ship = new HenkelShipmentRequestDeleteModel(item);
        ApiService apiService = RetrofitClient.getApiService();
        Call<HenkelShipmentRequestDeleteModel> call = apiService.deleteShipment(ship);

        call.enqueue(new Callback<HenkelShipmentRequestDeleteModel>() {

            @Override
            public void onResponse(@NonNull Call<HenkelShipmentRequestDeleteModel> call, @NonNull Response<HenkelShipmentRequestDeleteModel> response) {

                boolean response_action = false;

                if (response.isSuccessful() && response.body() != null) {

                    HenkelShipmentRequestDeleteModel responseModel = response.body();
                    String response_message = responseModel.getMessage();
                    String response_code = responseModel.getCode();

                    switch (response_code) {

                        case "200":

                            response_action = true;

                            NotificationHelper.showTemporaryNotification(
                                context,
                                "Self Check-In",
                                responseModel.getMessage(),
                                101
                            );

                            break;

                        case "9000":

                            response_action = false;
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                            break;

                        default:

                            response_action = false;
                            Toast.makeText(context, ":: Erro " + response_code + " ::", Toast.LENGTH_SHORT).show();
                            break;
                    }

                } else {

                    response_action = false;
                    String errorMessage = "Erro ao deletar o registro. Tente novamente.";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                }

                callback.onResponse(response_action, position);
            }

            @Override
            public void onFailure(@NonNull Call<HenkelShipmentRequestDeleteModel> call, @NonNull Throwable t) {

                callback.onResponse(false, position); // Chama o callback com false em caso de falha
            }
        });
    }
}