package com.example.nito_app01

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class TelaLista : AppCompatActivity() {

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_lista)

        val listView = findViewById<ListView>(R.id.listViewFuncionarios)
        val nomes = mutableListOf<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nomes)
        listView.adapter = adapter

        db.collection("funcionarios")
            .get()
            .addOnSuccessListener { result ->
                nomes.clear()
                for (doc in result) {
                    doc.getString("nome")?.let { nomes.add(it)}
                }
                adapter.notifyDataSetChanged()
                if (nomes.isEmpty()){
                    Toast.makeText(this, "Nenhum funcionário cadastrado.",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,"Erro ao listar: ${e.message}",Toast.LENGTH_LONG).show()
            }
    }
}