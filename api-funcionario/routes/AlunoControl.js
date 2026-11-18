const express = require('express');
const router = express.Router();
// Importa o Model
const Aluno = require('../models/Aluno');

// Buscar todos (GET /alunos)
router.get('/', async (req, res) => {
    const lista = await Aluno.findAll({
        order: [['id_aluno', 'DESC']]
    });
    res.status(200).json(lista);
});

// Cadastrar (POST /alunos)
router.post('/', async (req, res) => {
    const { nome, email, matricula, curso } = req.body;
    await Aluno.create({ nome, email, matricula, curso });
    res.status(200).json({ message: 'Aluno cadastrado com sucesso' });
});

// Buscar por id (GET /alunos/:id)
router.get('/:id', async (req, res) => {
    const item = await Aluno.findByPk(req.params.id);
    res.status(200).json(item);
});

// Deletar (DELETE /alunos/:id)
router.delete('/:id', async (req, res) => {
    await Aluno.destroy({
        where: { id_aluno: req.params.id }
    });
    res.status(200).json({ message: 'Aluno excluído com sucesso' });
});

// Atualizar (PUT /alunos/:id)
router.put('/:id', async (req, res) => {
    const { nome, email, matricula, curso } = req.body;
    await Aluno.update(
        { nome, email, matricula, curso },
        { where: { id_aluno: req.params.id } }
    );
    res.status(200).json({ message: 'Aluno atualizado com sucesso' });
});

module.exports = router;
