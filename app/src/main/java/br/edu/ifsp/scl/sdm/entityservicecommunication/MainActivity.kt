package br.edu.ifsp.scl.sdm.entityservicecommunication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.sdm.entityservicecommunication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var incrementServiceIntent : Intent
    private var counter = 0

    private val incrementBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            intent.getIntExtra("VALUE", -1)?.also {value->
                counter= value
                Toast.makeText(this@MainActivity, "You clicked $counter times", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        incrementServiceIntent = Intent(this, IncrementService::class.java)

        with (amb) {
            mainTb.apply {
                getString(R.string.app_name).also { setTitle(it) }
                setSupportActionBar(this)
            }
            incrementBt.setOnClickListener {

                // Serviço iniciado
                startService(incrementServiceIntent.apply {
                    putExtra("VALUE", counter)
                })
            }
        }

    }

    override fun onStart() {
        super.onStart()
        registerReceiver(incrementBroadcastReceiver, IntentFilter("INCREMENT_VALUE_ACTION"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(incrementBroadcastReceiver)
    }

    // Método para destrutir o serviço
    override fun onDestroy() {
        super.onDestroy()
        stopService(incrementServiceIntent)
    }


}