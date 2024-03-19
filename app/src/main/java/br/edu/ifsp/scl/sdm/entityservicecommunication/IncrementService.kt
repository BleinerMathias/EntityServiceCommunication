package br.edu.ifsp.scl.sdm.entityservicecommunication

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message

class IncrementService : Service() {

    private inner class IncrementHandler(looper:Looper):Handler(looper){
        override fun handleMessage(msg:Message) {
            super.handleMessage(msg)
            msg.data.getInt("VALUE").also{
                InterEntityCommunication.valueLiveData.postValue(it+1)
            }
            stopSelf() // Serviço auto se elimina
        }
    }


    // Carrega o valor que iremos incrementar em intent
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        /* Caso o serviço seja finalizado (Eliminado pelo SO), é através desse método que saberemos se
         o serviço erá reiniciado*/

        intent?.getIntExtra("VALUE", -1)?.also {value ->
            HandlerThread("IncrementHandlerThread").apply {
                start()
                IncrementHandler(looper).apply{
                    obtainMessage().apply{
                        data.putInt("VALUE", value)
                        sendMessage(this)
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    // Método obrigatório, mas não necessário

    override fun onBind(intent: Intent): IBinder? {
        // Para serviços iniciados e não vinculados, fazemos com que ele retorne nullo
        return null
    }
}