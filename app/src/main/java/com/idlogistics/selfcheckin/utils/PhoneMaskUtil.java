package com.idlogistics.selfcheckin.utils;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneMaskUtil implements TextWatcher {

    private final EditText editText;
    private boolean isUpdating;
    private String oldText = "";

    public PhoneMaskUtil(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (isUpdating || s == null) {

            return;
        }

        editText.setInputType(InputType.TYPE_CLASS_PHONE);

        String unmaskedText = s.toString().replaceAll("[^0-9]", ""); // Remove caracteres não numéricos
        StringBuilder formattedText = new StringBuilder();
        int index = 0;

        String mask = "(##) #####-####";

        for (char m : mask.toCharArray()) {

            if (index >= unmaskedText.length()) {

                break;
            }

            if (m == '#') {

                formattedText.append(unmaskedText.charAt(index));
                index++;

            } else {

                formattedText.append(m);
            }
        }

        isUpdating = true;
        editText.setText(formattedText.toString());
        editText.setSelection(formattedText.length());
        isUpdating = false;
    }

    @Override
    public void afterTextChanged(Editable s) {}

    public static void applyMask(EditText editText) {

        editText.addTextChangedListener(new PhoneMaskUtil(editText));
    }

    // Função para remover a máscara e retornar apenas os caracteres alfanuméricos
    public static String removeMask(String text) {

        // Remove todos os caracteres não alfanuméricos
        return text.replaceAll("[^A-Za-z0-9]", "");
    }
}
