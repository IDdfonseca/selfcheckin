package com.idlogistics.selfcheckin;

public class GlobalData {
    private static GlobalData instance;
    private String id_client;
    private String id_driver;
    private String CPF;
    private String CNH;
    private String DocumentType;
    private String fullName;
    private String Nationality;
    private String Phone;
    private String Plate;
    private String Transport;

    // Construtor privado para evitar instância externa
    private GlobalData() {

        // Inicialização das variáveis
        id_client = "";
        id_driver = "";
        CPF = "";
        CNH = "";
        DocumentType = "";
        Nationality = "";
        Phone = "";
        Plate = "";
        Transport = "";
    }

    // Método público para obter a instância única
    public static synchronized GlobalData getInstance() {

        if (instance == null) {

            instance = new GlobalData();
        }

        return instance;
    }

    // Métodos para acessar e modificar as variáveis
    public String getIdClient() {
        return id_client;
    }

    public void setIdClient(String id_client) {
        this.id_client = id_client;
    }

    public String getIdDriver() {
        return id_driver;
    }

    public void setIdDriver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getCNH() {
        return CNH;
    }

    public void setCNH(String CNH) {
        this.CNH = CNH;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String DocumentType) {
        this.DocumentType = DocumentType;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationality() {
        return Nationality;
    }
    public void setNationality(String Nationality) {
        this.Nationality = Nationality;
    }

    public String getPhone() {
        return Phone;
    }
    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPlate() {
        return Plate;
    }
    public void setPlate(String Plate) {
        this.Plate = Plate;
    }

    public String getTransport() {
        return Transport;
    }
    public void setTransport(String Transport) {
        this.Transport = Transport;
    }
}