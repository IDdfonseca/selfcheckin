package com.idlogistics.selfcheckin.driver;

public class DriverResponseModel {
    private boolean success;
    private String message;
    private String code;
    private String cm_id;
    private String cm_cpf;
    private String cm_cnh;
    private String cm_nome;
    private String cm_telefone;
    private String ce_id;
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getCode() { return code; }
    public String getCId() { return cm_id; }

    public String getCpf() { return cm_cpf; }
    public String getCnh() { return cm_cnh; }
    public String getNome() { return cm_nome; }
    public String getTelefone() { return cm_telefone; }
    public String getEmpresa() { return ce_id; }
}
