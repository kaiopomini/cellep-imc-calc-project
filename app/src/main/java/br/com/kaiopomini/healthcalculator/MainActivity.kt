package br.com.kaiopomini.healthcalculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // adds
        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)



        val logged = intent.getBooleanExtra("INTENT_LOGGED", false)
        val email = intent.getStringExtra("INTENT_EMAIL")





        btnMainCalcular.setOnClickListener {
            val peso = edtMainPeso.text.toString()
            val altura = edtMainAltura.text.toString()
            if (peso.isNotEmpty() || altura.isNotEmpty()) {
                val imc = calcImc(peso.toDouble(), altura.toDouble())
                txvMainImc.text = "Seu IMC é ${imc.format(2)}"
                txvMainMessage.text = textoImc(imc)
            } else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
            }

        }

        btnMainAddHistorico.setOnClickListener {
            if (logged) {
                val peso = edtMainPeso.text.toString()
                val altura = edtMainAltura.text.toString()
                if (peso.isNotEmpty() || altura.isNotEmpty()) {
                    val sharedPrefs = getSharedPreferences("cadastro_$email", Context.MODE_PRIVATE)

                    var listaPeso = sharedPrefs.getString("PESOS","")
                    var listaAltura = sharedPrefs.getString("ALTURAS", "")
                    var listaData = sharedPrefs.getString("DATAS", "")



                    val editPrefs = sharedPrefs.edit()

                    val data = getCurrentDateTime()
                    val dataString = data.toString("dd/MM/yyyy")

                    listaAltura+=";$altura"
                    listaPeso+=";$peso"
                    listaData+=";$dataString"

                    editPrefs.putString("PESOS", listaPeso)
                    editPrefs.putString("ALTURAS", listaAltura)
                    editPrefs.putString("DATAS",  listaData)

                    editPrefs.apply()

                    Toast.makeText(this, "Adicionado ao histórico.", Toast.LENGTH_LONG).show()

                    val mIntent = Intent(this, LoggedActivity::class.java)

                    mIntent.putExtra("INTENT_EMAIL", email )

                    startActivity(mIntent)
                    finishAffinity()
                } else{
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
                }

            } else {

                val mIntent = Intent(this, CadastroActivity::class.java)
                startActivity(mIntent)
            }
        }
    }


     fun calcImc(peso: Double, altura: Double) : Double {
        //peso / altura * altura
        val resultado = peso / (altura * altura)
        return resultado
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    fun textoImc(imc : Double) : String {

        return if (imc < 18.5) {
            "ABAIXO DO PESO - Nesse ponto, o corpo magro deixa de ser saudável e o organismo fica mais vulnerável a infecções."
        } else if (imc < 25) {
            "PESO NORMAL - Seu peso está adequado à altura. É importante manter a educação alimentar e a atividade física. "
        } else if (imc < 30) {
            "SOBREPESO - Esta faixa indica que você está com predisposição à obesidade; dependendo do seu histórico familiar e pessoal, pode haver um quadro de pré-diabetes e hipertensão."
        } else if (imc < 35) {
            "ODESIDADE GRAU I - O risco de desenvolver diabetes, para quem está nesta faixa de peso, é de 20%, e o de hipertensão ultrapassa 25%. Procure orientação médica."
        } else if (imc < 40) {
            "OBESIDADE GRAU II - O risco de desenvolver diabetes chega a 40%. Procure orientação médica."
        } else {
            "OBESIDADE GRAU III - O risco de desenvolver doenças associadas ao excesso de peso, como diabetes, reumatismos, câncer, apneia do sono, hipertensão e outros chega até 90%. Procure orientação médica imediatamente."
        }

    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}