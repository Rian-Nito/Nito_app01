# Documentação — Criação e Implementação da Nova Tabela (`aluno`)

Este documento descreve todo o processo realizado para seguir a **Aula 15 (Retrofit + API Node.js + MySQL)** e expandir a base de dados `escola` com uma nova tabela (`aluno`), implementando o CRUD completo no backend e o consumo via Retrofit no aplicativo Android (`Nito_App01`).

---

## 1. Banco de Dados MySQL (`escola`)

No banco de dados `escola`, além da tabela `funcionario` da Aula 15, foi criada a tabela `aluno` para armazenar os registros de estudantes da escola.

### Script SQL (`api-funcionario/escola.sql`)

```sql
CREATE DATABASE IF NOT EXISTS escola;
USE escola;

-- Tabela original (Aula 15)
CREATE TABLE IF NOT EXISTS funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    senha VARCHAR(60) NOT NULL,
    cargo VARCHAR(60) NOT NULL
);

-- Nova tabela criada na database escola
CREATE TABLE IF NOT EXISTS aluno (
    id_aluno INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL,
    matricula VARCHAR(30) NOT NULL,
    curso VARCHAR(60) NOT NULL
);

INSERT INTO aluno (nome, email, matricula, curso) VALUES
('Lucas Pereira', 'lucas@escola.com', '2026001', 'Desenvolvimento de Sistemas'),
('Mariana Costa', 'mariana@escola.com', '2026002', 'Redes de Computadores'),
('Rafael Oliveira', 'rafael@escola.com', '2026003', 'Ciência da Computação');
```

### Estrutura dos Campos da Tabela `aluno`
| Coluna | Tipo | Restrições | Descrição |
| :--- | :--- | :--- | :--- |
| `id_aluno` | `INT` | `AUTO_INCREMENT`, `PRIMARY KEY` | Identificador único do aluno |
| `nome` | `VARCHAR(100)` | `NOT NULL` | Nome completo do aluno |
| `email` | `VARCHAR(120)` | `NOT NULL` | E-mail institucional ou pessoal |
| `matricula` | `VARCHAR(30)` | `NOT NULL` | Número de matrícula |
| `curso` | `VARCHAR(60)` | `NOT NULL` | Curso matriculado |

---

## 2. Backend Node.js (`api-funcionario`)

A API segue o padrão **CommonJS** (`require` / `module.exports`) com **Express**, **CORS**, **MySQL2** e **Sequelize**.

### 2.1. Model `models/Aluno.js`
Mapeia a tabela `aluno` no Sequelize mantendo `freezeTableName: true` (para não pluralizar o nome da tabela) e `timestamps: false` (pois a tabela não utiliza `createdAt`/`updatedAt`).

### 2.2. Controller / Rotas `routes/AlunoControl.js`
Implementa os 5 endpoints do CRUD:
- **GET `/alunos`**: Lista todos os alunos ordenados por `id_aluno DESC`.
- **GET `/alunos/:id`**: Busca um aluno específico pela chave primária (`findByPk`).
- **POST `/alunos`**: Cadastra um novo aluno recebendo `{ nome, email, matricula, curso }` no corpo da requisição.
- **PUT `/alunos/:id`**: Atualiza os dados de um aluno existente pelo `id_aluno`.
- **DELETE `/alunos/:id`**: Remove um aluno pelo `id_aluno`.

### 2.3. Registro no Servidor (`server.js`)
O roteador de alunos foi importado e montado na rota `/alunos`:
```javascript
const rotasAluno = require('./routes/AlunoControl');
app.use('/alunos', rotasAluno);
```

### 2.4. Como executar o servidor
```powershell
cd api-funcionario
npm install
node --watch server.js
```
O servidor ficará disponível em `http://localhost:3000` (rotas `/funcionarios` e `/alunos`).

---

## 3. Aplicativo Android com Retrofit (`Nito_App01`)

### 3.1. Permissões e Dependências
- **`AndroidManifest.xml`**: Permissão `android.permission.INTERNET`, `android:usesCleartextTraffic="true"` (para requisições HTTP em desenvolvimento) e registro da `AlunoActivity`.
- **`app/build.gradle.kts`**: Adicionadas as dependências do Retrofit (`2.11.0`), Converter Gson (`2.11.0`), OkHttp Logging Interceptor (`4.12.0`) e Kotlin Coroutines Android (`1.9.0`).

### 3.2. Modelo `Aluno.kt`
```kotlin
data class Aluno(
    val id_aluno: Int? = null,
    var nome: String = "",
    var email: String = "",
    var matricula: String = "",
    var curso: String = ""
)
```

### 3.3. Interface `ApiService.kt`
Adicionados os métodos `suspend` para os endpoints de `/alunos`:
- `@GET("alunos") suspend fun listarAlunos(): List<Aluno>`
- `@GET("alunos/{id}") suspend fun obterAluno(@Path("id") id: Int): Aluno`
- `@POST("alunos") suspend fun criarAluno(@Body a: Aluno): Map<String, String>`
- `@PUT("alunos/{id}") suspend fun atualizarAluno(@Path("id") id: Int, @Body a: Aluno): Map<String, String>`
- `@DELETE("alunos/{id}") suspend fun excluirAluno(@Path("id") id: Int): Map<String, String>`

### 3.4. Cliente Retrofit (`RetrofitClient.kt`)
Configurado com `BASE_URL = "http://10.0.2.2:3000/"` (endereço padrão do emulador Android para acessar o `localhost` da máquina hospedeira), `HttpLoggingInterceptor` e `GsonConverterFactory`.

### 3.5. Interface e Lógica (`activity_aluno.xml` e `AlunoActivity.kt`)
- Layout com campos de entrada (`edtIdAluno`, `edtNomeAluno`, `edtEmailAluno`, `edtMatricula`, `edtCurso`), botões de ação (`Salvar`, `Atualizar`, `Excluir`, `Listar Tudo`, `Funcionários`) e `ListView` (`listaAlunos`).
- `AlunoActivity` utiliza `CoroutineScope(Dispatchers.Main)` com `withContext(Dispatchers.IO)` para realizar as chamadas de rede sem bloquear a UI.
- Na `MainActivity`, o botão **Gerenciar Alunos** (`btnIrAlunos`) abre a `AlunoActivity`.

---

## 4. Como Testar

1. **Banco de Dados**: Execute o script `api-funcionario/escola.sql` no MySQL Workbench ou MySQL CLI.
2. **Backend**: Inicie a API com `node --watch server.js` dentro de `api-funcionario`.
3. **Navegador / API**: Acesse `http://localhost:3000/funcionarios` e `http://localhost:3000/alunos` para confirmar o retorno JSON.
4. **Android**: Execute o app no emulador Android. Teste o CRUD de **Funcionários** na tela inicial e clique em **Gerenciar Alunos** para testar o CRUD completo da nova tabela **Alunos**.
