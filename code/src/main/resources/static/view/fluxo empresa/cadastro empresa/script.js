import {isValidName, isValidCNPJ, isValidEmail, isValidSenha} from './validações/validacoes.js'

document.addEventListener('DOMContentLoaded', async () => {
    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const nome = document.getElementById('nome').value.trim();
        const cnpj = document.getElementById('cnpj').value.trim();
        const email = document.getElementById('email').value.trim();
        const senha = document.getElementById('senha').value.trim();

        if (!isValidName(nome)) return;
        if (!isValidCNPJ(cnpj)) return;
        if (!isValidEmail(email)) return;
        if (!isValidSenha(senha)) return;

        const empresa = {
            nome: nome,
            cnpj: cnpj,
            email: email,
            senha: senha
        };

        try {
            const resposta = await fetch('http://localhost:8080/empresas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(empresa)
            });

            if (!resposta.ok) {
                alert("Não foi possível cadastrar a empresa. Tente novamente, por favor.😉");
                return;
            }

            const novaEmpresa = await resposta.json();
            localStorage.setItem('idEmpresa', novaEmpresa.id);
            alert(`Empresa ${novaEmpresa.nome} cadastrada com sucesso!🤩`);
            form.reset();
            window.location.href = "../gerenciamento vantagens/vantagem.html";
        } catch (error) {
            console.error('Erro na requisição:', error);
            alert("Erro na requisição de cadastro. Tente novamente, por favor.😉");
        }
    });
});

