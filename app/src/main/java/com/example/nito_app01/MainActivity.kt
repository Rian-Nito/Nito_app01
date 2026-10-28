package com.example.nito_app01

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private val db by lazy { Firebase.firestore }
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtCargo = findViewById<EditText>(R.id.edtCargo)
        val btnCad = findViewById<Button>(R.id.btnCadastrar)
        btnCad.setOnClickListener {
            val nome = edtNome.text.toString()
            val email = edtEmail.text.toString()
            val cargo = edtCargo.text.toString()
            cadastrarFuncionario(nome, email, cargo) {
                edtNome.text?.clear()
                edtEmail.text?.clear()
                edtCargo.text?.clear()
            }
        }
    }

    private fun cadastrarFuncionario(
        nome: String,
        email: String,
        cargo: String,
        onSucess: () -> Unit = {}
    ) {
        val dados = hashMapOf(
            "nome" to nome,
            "email" to email,
            "cargo" to cargo
        )

        db.collection("funcionarios")
            .add(dados)
            .addOnSuccessListener {
                Toast.makeText(this, "Cadastrado!", Toast.LENGTH_SHORT).show()
                onSucess()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
