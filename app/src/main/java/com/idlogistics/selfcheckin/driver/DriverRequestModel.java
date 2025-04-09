package com.idlogistics.selfcheckin.driver;

public class DriverRequestModel {
    private String cpf;
    private String cnh;
    private String tipo;
    private String nome;

    public DriverRequestModel(String cpf, String cnh, String tipo, String nome  ) {

        this.cpf = cpf;
        this.cnh = cnh;
        this.tipo = tipo;
        this.nome = nome;
    }
}
