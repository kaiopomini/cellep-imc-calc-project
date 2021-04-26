package br.com.kaiopomini.healthcalculator

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_logged.*
import java.util.logging.LoggingMXBean


class LoggedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged)

        val email = intent.getStringExtra("INTENT_EMAIL")
        val sharedPrefs = getSharedPreferences("cadastro_$email", Context.MODE_PRIVATE)
        val nome = sharedPrefs.getString("NOME", "")
        val peso = sharedPrefs.getString("PESOS", "0")
        val altura = sharedPrefs.getString("ALTURAS", "0")
        val data = sharedPrefs.getString("DATAS", "")


        val listView = findViewById<ListView>(R.id.lvwLoggedLista)

        listView.adapter = MyCustomAdapter(this, peso!!, altura!!, data!!)

        val listaPeso = peso!!.split(';')
        val listaAltura = altura!!.split(';')
        val listaData = data!!.split(';')


        val imc = calcImc(listaPeso.last().toDouble(), listaAltura.last().toDouble())
        txvLoggedBemVindo.text = "Olá $nome!"
        txvLoggedImc.text = "Seu IMC é ${imc.format(2)}"
        txvLoggedMessage.text = textoImc(imc)
        txvLoggedData.text = listaData.last()


        btnLoggedCalcularNovo.setOnClickListener {
            val mIntent = Intent(this, MainActivity::class.java)
            mIntent.putExtra("INTENT_EMAIL", email)
            mIntent.putExtra("INTENT_LOGGED", true )
            startActivity(mIntent)
        }

        txcLoggedLogout.setOnClickListener {
            val mIntent = Intent(this, LoginActivity::class.java)
            startActivity(mIntent)
            finish()
        }
    }

    fun calcImc(peso: Double, altura: Double) : Double {
        //peso / altura * altura
        return peso / (altura * altura)
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)

    fun textoImc(imc : Double) : String {

        return if (imc < 18.5) {
            "ABAIXO DO PESO"
        } else if (imc < 25) {
            "PESO NORMAL"
        } else if (imc < 30) {
            "SOBREPESO"
        } else if (imc < 35) {
            "ODESIDADE GRAU I"
        } else if (imc < 40) {
            "OBESIDADE GRAU II"
        } else {
            "OBESIDADE GRAU III"
        }
    }

    private class MyCustomAdapter(context: Context, peso: String, altura: String, data: String): BaseAdapter() {

        private val mContext: Context
        private val mPeso: String
        private val mAltura: String
        private val mData: String

        init {
            mContext = context
            mPeso = peso
            mAltura = altura
            mData = data
        }

        override fun getCount(): Int {
            val listaPeso = mPeso.split(';')
            return listaPeso.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "Teste string"
        }
        //responsible for rendering each row
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val listaPeso = mPeso.split(';')
            val listaAltura = mAltura.split(';')
            val listaData = mData.split(';')

            val layoutInflater = LayoutInflater.from(mContext)
            val itemHistorico = layoutInflater.inflate(R.layout.item_historico, parent, false)
            val dataTextView = itemHistorico.findViewById<TextView>(R.id.data)
            dataTextView.text = "${listaData[position]}"
            val pesoTextView = itemHistorico.findViewById<TextView>(R.id.peso)
            pesoTextView.text = "${listaPeso[position]}Kg"
            val alturaTextView = itemHistorico.findViewById<TextView>(R.id.altura)
            alturaTextView.text = "${listaAltura[position]}m"
            val imc = listaPeso[position].toDouble() / (listaAltura[position].toDouble() * listaAltura[position].toDouble())
            val imcFormated = String.format("%.2f", imc)
            val imcTextView = itemHistorico.findViewById<TextView>(R.id.imc)
            imcTextView.text = "IMC: $imcFormated"
            return itemHistorico
//            val textView = TextView(mContext)
//            textView.text = "Here is my row"
//            return textView
        }
    }
}

