const express = require('express');
const cors = require('cors');
const mysql = require('mysql2/promise');
const { sequelize } = require('./db');
const Funcionario = require('./models/Funcionario');
const Aluno = require('./models/Aluno');
const rotasFuncionario = require('./routes/FuncionarioControl');
const rotasAluno = require('./routes/AlunoControl');

const app = express();
app.use(cors());
app.use(express.json());

app.get('/', (req, res) => res.send('API Escola (Sequelize + CommonJS) - OK'));
app.use('/funcionarios', rotasFuncionario);
app.use('/alunos', rotasAluno);

const PORT = 3000;

(async () => {
    try {
        // Garante que a database 'escola' exista antes de conectar com o Sequelize
        const conn = await mysql.createConnection({
            host: 'localhost',
            port: 3306,
            user: 'root',
            password: 'root'
        });
        await conn.query('CREATE DATABASE IF NOT EXISTS escola;');
        await conn.end();

        await sequelize.authenticate();
        await sequelize.sync();

        if (await Funcionario.count() === 0) {
            await Funcionario.bulkCreate([
                { nome: 'Ana Silva', email: 'ana@teste.com', senha: '123', cargo: 'Atendente' },
                { nome: 'Bruno Souza', email: 'bruno@teste.com', senha: '123', cargo: 'Gerente' },
                { nome: 'Carla Nunes', email: 'carla@teste.com', senha: 'abc', cargo: 'Caixa' }
            ]);
        }

        if (await Aluno.count() === 0) {
            await Aluno.bulkCreate([
                { nome: 'Lucas Pereira', email: 'lucas@escola.com', matricula: '2026001', curso: 'Desenvolvimento de Sistemas' },
                { nome: 'Mariana Costa', email: 'mariana@escola.com', matricula: '2026002', curso: 'Redes de Computadores' },
                { nome: 'Rafael Oliveira', email: 'rafael@escola.com', matricula: '2026003', curso: 'Ciência da Computação' }
            ]);
        }

        app.listen(PORT, () => console.log(`Servidor: http://localhost:${PORT}`));
    } catch (err) {
        console.error('Erro ao conectar no banco:', err);
        process.exit(1);
    }
})();
