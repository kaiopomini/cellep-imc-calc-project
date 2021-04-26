package br.com.kaiopomini.healthcalculator

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLoginEntrar.setOnClickListener {
            //Capturando os dados digitados
            val email = edtLoginEmail.text.toString().trim().toLowerCase()
            val senha = edtLoginSenha.text.toString().trim()

            //Validação dos campos
            if(email.isEmpty()){
                edtLoginEmail.error = "Campo obrigatório!"
                edtLoginEmail.requestFocus()
            }else if(senha.isEmpty()){
                edtLoginSenha.error = "Campo obrigatório!"
                edtLoginSenha.requestFocus()
            }else{
                //Verificar o email e senha

                val sharedPref = getSharedPreferences("cadastro_$email", Context.MODE_PRIVATE)
                val emailPrefs = sharedPref.getString("EMAIL", "")
                val senhaPrefs = sharedPref.getString("SENHA", "")

                if(email == emailPrefs && senha == senhaPrefs){
                    //apresentar uma mensagem de sucesso ao usuario
                    Toast.makeText(this, "Usuário logado com sucesso!", Toast.LENGTH_LONG).show()

                    //Abrindo a MainActivity
                    val mIntent = Intent(this, LoggedActivity::class.java)
                    mIntent.putExtra("INTENT_EMAIL", email)
                    mIntent.putExtra("INTENT_LOGGED", true )
                    startActivity(mIntent)
                    finish()
                }else{
                    //Apresentando uma mensagem de erro ao usuário: desafio 2 minutos
                    Toast.makeText(this, "Usuário ou senha inválidos!", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnLoginCadastrar.setOnClickListener {
            val mIntent = Intent(this, CadastroActivity::class.java)
            startActivity(mIntent)
        }

        txcLoginContinuarSemLogar.setOnClickListener {
            val mIntent = Intent(this, MainActivity::class.java)
            mIntent.putExtra("INTENT_LOGGED", false )
            startActivity(mIntent)
        }
    }
}