package com.example.nito_app01

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class AlunoActivity : AppCompatActivity() {

    private val ui = CoroutineScope(Dispatchers.Main)
    private var itens = mutableListOf<String>()
    private lateinit var adaptador: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)

        val edtId: EditText = findViewById(R.id.edtIdAluno)
        val edtNome: EditText = findViewById(R.id.edtNomeAluno)
        val edtEmail: EditText = findViewById(R.id.edtEmailAluno)
        val edtMatricula: EditText = findViewById(R.id.edtMatricula)
        val edtCurso: EditText = findViewById(R.id.edtCurso)
        val btnSalvar: Button = findViewById(R.id.btnSalvarAluno)
        val btnAtualizar: Button = findViewById(R.id.btnAtualizarAluno)
        val btnExcluir: Button = findViewById(R.id.btnExcluirAluno)
        val btnListar: Button = findViewById(R.id.btnListarAluno)
        val btnVoltar: Button = findViewById(R.id.btnVoltarFuncionarios)
        val lista: ListView = findViewById(R.id.listaAlunos)

        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, itens)
        lista.adapter = adaptador

        btnSalvar.setOnClickListener {
            val a = Aluno(
                nome = edtNome.text.toString(),
                email = edtEmail.text.toString(),
                matricula = edtMatricula.text.toString(),
                curso = edtCurso.text.toString()
            )
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.criarAluno(a) }
                    toast("Aluno cadastrado com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtMatricula, edtCurso)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao salvar: ${e.message}")
                }
            }
        }

        btnAtualizar.setOnClickListener {
            val id = edtId.text.toString().toIntOrNull()
            if (id == null) {
                toast("Informe um ID para atualizar")
                return@setOnClickListener
            }
            val a = Aluno(
                nome = edtNome.text.toString(),
                email = edtEmail.text.toString(),
                matricula = edtMatricula.text.toString(),
                curso = edtCurso.text.toString()
            )
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.atualizarAluno(id, a) }
                    toast("Aluno atualizado com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtMatricula, edtCurso)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao atualizar: ${e.message}")
                }
            }
        }

        btnExcluir.setOnClickListener {
            val id = edtId.text.toString().toIntOrNull()
            if (id == null) {
                toast("Informe um ID para excluir")
                return@setOnClickListener
            }
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.excluirAluno(id) }
                    toast("Aluno excluído com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtMatricula, edtCurso)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao excluir: ${e.message}")
                }
            }
        }

        btnListar.setOnClickListener {
            listar()
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        listar()
    }

    private fun listar() {
        ui.launch {
            try {
                val dados = withContext(Dispatchers.IO) { RetrofitClient.api.listarAlunos() }
                itens.clear()
                dados.forEach { a ->
                    itens.add("#${a.id_aluno} - ${a.nome} | ${a.matricula} | ${a.curso}")
                }
                adaptador.notifyDataSetChanged()
            } catch (e: Exception) {
                toast("Erro ao listar: ${e.message}")
            }
        }
    }

    private fun limpar(vararg campos: EditText) {
        campos.forEach { it.setText("") }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
