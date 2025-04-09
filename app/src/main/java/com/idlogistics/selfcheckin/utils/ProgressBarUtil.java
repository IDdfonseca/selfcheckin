package com.idlogistics.selfcheckin.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class ProgressBarUtil {
    private static CountDownTimer countDownTimer;
    public static void startProgressBar(final Context context, final ProgressBar progressBar, final Class<?> targetActivity, int duration) {

        // Configurar a animação
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        animation.setDuration(duration); // Duração em milissegundos
        animation.start();

        // Configurar o CountDownTimer
        countDownTimer = new CountDownTimer(duration, 100) { // Intervalo de 100 milissegundos

            @Override
            public void onTick(long millisUntilFinished) {
                // Atualizar a barra de progresso
                progressBar.setProgress((int) ((duration - millisUntilFinished) / (duration / 100)));
            }

            @Override
            public void onFinish() {
                // Certifique-se de que a barra de progresso está completa
                progressBar.setProgress(100);

                // Redirecionar para a Activity alvo
                Intent intent = new Intent(context, targetActivity);
                context.startActivity(intent);
                if (context instanceof Activity) {
                    ((Activity) context).finish(); // Fechar a Activity atual se o contexto for uma Activity
                }
            }
        }.start();
    }

    public static void stopProgressBar(final ProgressBar progressBar) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        progressBar.setProgress(0); // Resetar a barra de progresso
    }
}
