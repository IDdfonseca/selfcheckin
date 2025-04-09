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
import androidx.recyclerview.widget.RecyclerView;

import com.idlogistics.selfcheckin.R;
import com.idlogistics.selfcheckin.api.ApiService;
import com.idlogistics.selfcheckin.api.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HenkelShipmentListShipmentReadOnlyAdapter extends RecyclerView.Adapter<HenkelShipmentListShipmentReadOnlyAdapter.ViewHolder> {

    private Context context;
    private List<String> items;
    public HenkelShipmentListShipmentReadOnlyAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }
    @NonNull
    public HenkelShipmentListShipmentReadOnlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shipment_read_only, parent, false);
        return new HenkelShipmentListShipmentReadOnlyAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HenkelShipmentListShipmentReadOnlyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String label_item = " SHIPMENT : ";
        String item = items.get(position);
        holder.itemText.setText(String.format("%s%s", label_item, item));
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
            deleteIcon = view.findViewById(R.id.item_ok);
        }
    }
    private void deleteItemFromDatabase(String item, int position, HenkelShipmentListShipmentAdapter.ResponseCallback callback) {
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
                            Toast.makeText(context, responseModel.getMessage(), Toast.LENGTH_SHORT).show();
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
