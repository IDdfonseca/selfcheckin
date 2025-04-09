package com.idlogistics.selfcheckin.utils;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

public class PlacaMaskUtil {
    public static void applyMask(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating || s == null) {
                    return;
                }

                String unmaskedText = s.toString().replaceAll("[^A-Za-z0-9]", "").toUpperCase(); // Remove caracteres inválidos
                StringBuilder formattedText = new StringBuilder();
                int index = 0;

                // Determinar a máscara: Mercosul (AAA-0A00) ou Antiga (AAA-0000)
                String mask = "AAA-0000"; // Padrão antigo

                // Muda o teclado conforme o usuário vai digitando
                if(unmaskedText.length() <= 2 ){

                    editText.setInputType(InputType.TYPE_CLASS_TEXT);

                } else if(unmaskedText.length() == 3){

                    editText.setInputType(InputType.TYPE_CLASS_PHONE);

                }else if(unmaskedText.length() == 4){

                    editText.setInputType(InputType.TYPE_CLASS_TEXT);

                } else{

                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                }

                if (unmaskedText.length() > 4 && Character.isLetter(unmaskedText.charAt(4))) {
                    mask = "AAA-0A00"; // Muda para padrão Mercosul se o 5º caractere for uma letra
                }

                for (char m : mask.toCharArray()) {
                    if (index >= unmaskedText.length()) {
                        break;
                    }

                    char currentChar = unmaskedText.charAt(index);

                    if (m == 'A') {  // Apenas letras
                        if (!Character.isLetter(currentChar)) {
                            continue; // Ignora números
                        }
                        formattedText.append(currentChar);
                        index++;
                    } else if (m == '0') {  // Apenas números
                        if (!Character.isDigit(currentChar)) {
                            continue; // Ignora letras
                        }
                        formattedText.append(currentChar);
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
        });
    }

    // Função para remover a máscara e retornar apenas os caracteres alfanuméricos
    public static String removeMask(String text) {
        // Remove todos os caracteres não alfanuméricos
        return text.replaceAll("[^A-Za-z0-9]", "");
    }
}
