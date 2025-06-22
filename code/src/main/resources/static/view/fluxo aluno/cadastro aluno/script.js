import {isValidName, isValidCPF, isValidEmail, isValidRG, isValidSenha, isValidCurso, isValidRuaBairro, isValidCEP, isValidNumero} from './validações/validacoes.js'

document.addEventListener('DOMContentLoaded', async () => {
    const select = document.getElementById('instituicao');

    fetch('http://localhost:8080/instituicoes')
        .then(res => res.json())
        .then(data => {
            data.forEach(instituicao => {
                const option = document.createElement('option');
                option.value = instituicao.id;
                option.textContent = instituicao.nome;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Erro ao carregar instituições:', err));

    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const nome = document.getElementById('nome').value.trim();
        const cpf = document.getElementById('cpf').value.trim();
        const email = document.getElementById('email').value.trim();
        const rg = document.getElementById('rg').value.trim();
        const senha = document.getElementById('senha').value.trim();
        const curso = document.getElementById('curso').value.trim();
        const rua = document.getElementById('rua').value.trim();
        const bairro = document.getElementById('bairro').value.trim();
        const cep = document.getElementById('cep').value.trim();
        const numero = parseInt(document.getElementById('numero').value, 10);

        if (!isValidName(nome)) return;
        if (!isValidCPF(cpf)) return;
        if (!isValidEmail(email)) return;
        if (!isValidRG(rg)) return;
        if (!isValidSenha(senha)) return;
        if (!isValidCurso(curso)) return;
        if (!isValidRuaBairro(rua, 'Rua')) return;
        if (!isValidRuaBairro(bairro, 'Bairro')) return;
        if (!isValidCEP(cep)) return;
        if (!isValidNumero(numero)) return;

        const aluno = {
            nome: nome,
            cpf: cpf,
            email: email,
            rg: rg,
            senha: senha,
            curso: curso,
            rua: rua,
            bairro: bairro,
            cep: cep,
            numero: numero,
            saldoMoedas: 0,
            instituicao: {
                id: Number(select.value)
            }
        };

        try {
            const resposta = await fetch('http://localhost:8080/alunos', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(aluno)
            });

            if (!resposta.ok) {
                alert("Não foi possível cadastrar o(a) aluno(a). Tente novamente, por favor.😉");
                return;
            }

            const novoAluno = await resposta.json();
            localStorage.setItem('idAluno', novoAluno.id);
            alert(`Aluno(a) ${novoAluno.nome} cadastrado(a) com sucesso!🤩`);
            form.reset();
            window.location.href = "../extrato aluno/extrato.html"; 
        } catch (error) {
            alert("Erro na requisição de cadastro. Tente novamente, por favor.😉");
        }
    });
});
