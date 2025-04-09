package com.idlogistics.selfcheckin.client_henkel;

public class HenkelShipmentRequestModel {

    private String message;
    private String cp_shipment;
    private String id_driver;
    private String cpf;
    private String nome;
    private String transport;
    private String telefone;
    private String placa;
    private String site;
    public String getMessage() { return message; }
    public HenkelShipmentRequestModel(String cp_shipment, String id_driver, String cpf, String nome, String transport, String telefone, String placa, String site) {

        this.cp_shipment = cp_shipment;
        this.id_driver = id_driver;
        this.cpf = cpf;
        this.nome = nome;
        this.transport = transport;
        this.telefone = telefone;
        this.placa = placa;
        this.site = site;
    }
}
