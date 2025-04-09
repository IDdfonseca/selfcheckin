package com.idlogistics.selfcheckin.api;

import com.idlogistics.selfcheckin.TransportRequestModel;
import com.idlogistics.selfcheckin.client_henkel.HenkelShipmentRequestDeleteModel;
import com.idlogistics.selfcheckin.client_henkel.HenkelShipmentRequestModel;
import com.idlogistics.selfcheckin.client_henkel.HenkelShipmentResponseModel;
import com.idlogistics.selfcheckin.driver.DriverRequestModel;
import com.idlogistics.selfcheckin.driver.DriverResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/get_driver")
    Call<DriverResponseModel> storageDriver(@Body DriverRequestModel driver);
    @Headers("Content-Type: application/json")
    @POST("v1/get_shipment")
    Call<HenkelShipmentRequestModel> getShipment(@Body HenkelShipmentRequestModel shipment);
    @Headers("Content-Type: application/json")
    @GET("v1/list_shipment_by_cpf")
    Call<List<HenkelShipmentResponseModel>> listShipments(@Query("cpf") String cpf );
    @Headers("Content-Type: application/json")
    @POST("v1/delete_shipment")
    Call<HenkelShipmentRequestDeleteModel> deleteShipment(@Body HenkelShipmentRequestDeleteModel shipment);
    @GET("v1/list_transport")
    Call<List<TransportRequestModel>> listTransport();

}
