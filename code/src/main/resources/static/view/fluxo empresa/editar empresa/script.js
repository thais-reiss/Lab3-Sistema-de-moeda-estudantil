const btn = document.getElementById("btnAtualizar");
const idEmpresa = localStorage.getItem("idEmpresa");

import {isValidName, isValidCNPJ, isValidEmail, isValidSenha} from './validações/validacoes.js'

window.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch(`http://localhost:8080/empresas/${idEmpresa}`);

        if (!response.ok) {
            alert("Sinto muito. Houve um erro ao carregar os dados da empresa. Tente novamente.😉");
            return;
        }
        const empresa = await response.json();

        document.getElementById('nome').value = empresa.nome || "";
        document.getElementById('cnpj').value = empresa.cnpj || "";
        document.getElementById('email').value = empresa.email || "";
        //document.getElementById('senha').value = empresa.senha || ""; Não retorna senha
    } catch (error) {
        alert("Sinto muito. Houve um erro ao carregar os dados da empresa. Tente novamente.😉");
    }
});

const form = document.querySelector('form');
form.addEventListener('submit', async (ev) => {
    ev.preventDefault();

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
        const resposta = await fetch(`http://localhost:8080/empresas/${idEmpresa}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(empresa)
        });

        if (!resposta.ok) {
            alert("Não foi possível atualizar a empresa. Tente novamente, por favor.😉");
            return;
        }

        const empresaAtualizada = await resposta.json();
        alert(`Empresa ${empresaAtualizada.nome} atualizada com sucesso!🤩`);
        form.reset();
        window.location.href = "../consulta empresas/consultaempresas.html";
    } catch (error) {
        alert("Erro na requisição de atualização. Tente novamente, por favor.😉");
    }
});

