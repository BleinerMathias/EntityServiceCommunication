package br.edu.ifsp.scl.sdm.entityservicecommunication

import android.app.Service
import android.content.Intent
import android.os.IBinder

class IncrementService : Service() {

    // Carrega o valor que iremos incrementar em intent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        /* Caso o serviço seja finalizado (Eliminado pelo SO), é através desse método que saberemos se
         o serviço erá reiniciado*/

        intent?.getIntExtra("VALUE", -1)?.also {
            InterEntityCommunication.valueLiveData.postValue(it + 1)
        }
        return START_NOT_STICKY
    }

    // Método obrigatório, mas não necessário

    override fun onBind(intent: Intent): IBinder? {
        // Para serviços iniciados e não vinculados, fazemos com que ele retorne nullo
        return null
    }
}