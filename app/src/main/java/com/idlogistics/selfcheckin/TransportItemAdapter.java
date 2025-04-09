package com.idlogistics.selfcheckin;

public class TransportItemAdapter {
    private final String id;
    private final String name;

    public TransportItemAdapter(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return name;
    }

    @Override
    public String toString() {
        return name; // é isso que será mostrado no AutoComplete
    }
}
