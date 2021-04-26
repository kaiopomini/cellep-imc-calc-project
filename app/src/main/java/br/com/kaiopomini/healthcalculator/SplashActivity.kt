package br.com.kaiopomini.healthcalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // criando a intenção (intent) de trocar de tela
            val mIntent = Intent(this, LoginActivity::class.java)

            // iniciar a proxima tela
            startActivity(mIntent)

            // finalizando a tela de splash
            finish()
        }, 2500)
    }
}