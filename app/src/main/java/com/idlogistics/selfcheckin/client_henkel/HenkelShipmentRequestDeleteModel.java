package com.idlogistics.selfcheckin.client_henkel;

public class HenkelShipmentRequestDeleteModel {
    private String code;
    private String message;
    private String shipment;
    public String getMessage() { return message; }
    public String getCode() { return code; }
    public HenkelShipmentRequestDeleteModel(String shipment) { this.shipment = shipment; }
}