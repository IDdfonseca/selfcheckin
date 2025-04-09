package com.idlogistics.selfcheckin;

import androidx.annotation.NonNull;

public class TransportRequestModel {

    private String ce_razao;
    private String ce_id;
    public String getIdTransport() {
        return ce_id;
    }
    public String getNome() {
        return ce_razao;
    }

    @NonNull
    @Override
    public String toString() {
        return ce_razao;
    }
}
