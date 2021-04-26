package br.com.kaiopomini.healthcalculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.text.SimpleDateFormat
import java.util.*


class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        btnCadastroCadastrar.setOnClickListener {
            val nome = edtCadastroNome.text.toString().trim()
            val email = edtCadastroEmail.text.toString().trim().toLowerCase()
            val senha = edtCadastroSenha.text.toString().trim()
            val peso = edtCadastroPeso.text.toString().trim()
            val altura = edtCadastroAltura.text.toString().trim()
            val data = getCurrentDateTime()
            val dataString = data.toString("dd/MM/yyyy")


            if(nome.isEmpty() || peso.isEmpty() || altura.isEmpty() || email.isEmpty() || senha.isEmpty() ){
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show()
            }else{


                val sharedPrefs = getSharedPreferences("cadastro_$email", Context.MODE_PRIVATE)

                val editPrefs = sharedPrefs.edit()


                editPrefs.putString("NOME", nome)
                editPrefs.putString("PESOS", peso)
                editPrefs.putString("ALTURAS", altura)
                editPrefs.putString("DATAS", dataString)
                editPrefs.putString("EMAIL", email)
                editPrefs.putString("SENHA", senha)

                editPrefs.apply()

                Toast.makeText(this, "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show()


                val mIntent = Intent(this, LoggedActivity::class.java)


                mIntent.putExtra("INTENT_EMAIL", email )
                mIntent.putExtra("INTENT_LOGGED", true )

                startActivity(mIntent)


                finishAffinity()
            }
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