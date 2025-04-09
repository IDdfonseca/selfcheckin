package com.idlogistics.selfcheckin.scandit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/*
 * A fragment that displays an AlertDialog with the recognized document.
 */
public class AlertDialogFragment extends DialogFragment {
    private static final String KEY_TITLE_RES = "title_res";
    private static final String KEY_MESSAGE = "message";

    private Callbacks callbacks;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getHost() instanceof Callbacks) {
            callbacks = (Callbacks) getHost();
        } else {
            throw new ClassCastException("Parent fragment doesn't implement Callbacks!");
        }
    }

    public static AlertDialogFragment newInstance(@StringRes int title, String message) {
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_TITLE_RES, title);
        arguments.putString(KEY_MESSAGE, message);

        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @StringRes int titleRes = getArguments().getInt(KEY_TITLE_RES);
        String message = getArguments().getString(KEY_MESSAGE);

        return new AlertDialog.Builder(requireContext())
                .setTitle(titleRes)
                .setMessage(message)
//                .setPositiveButton(R.string.result_button_resume, null)
                .create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        callbacks.onAlertDismissed();
    }

    public interface Callbacks {
        void onAlertDismissed();
    }
}
