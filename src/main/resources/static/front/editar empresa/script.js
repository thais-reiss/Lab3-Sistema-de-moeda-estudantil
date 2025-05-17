const btn = document.getElementById("btnAtualizar");
const idEmpresa = localStorage.getItem("idEmpresa");

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
        document.getElementById('senha').value = empresa.senha || "";
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

function isValidName(nome) {
    const regex = /^[a-zA-ZÀ-ÿ\s]+$/;
    if (!regex.test(nome) || nome.length < 3) {
        alert('O nome deve ter mínimo de 3 letras.😊');
        return false;
    }
    return true;
}

function isValidCNPJ(cnpj) {
    const regex = /^[0-9]{14}$/;
    if (!regex.test(cnpj)) {
        alert('O CNPJ deve conter exatamente 14 números.😊');
        return false;
    }
    return true;
}

function isValidEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(email)) {
        alert('O e-mail é inválido. Informe um formato como empresa@email.com.😊');
        return false;
    }
    return true;
}

function isValidSenha(senha) {
    if (senha.length < 4) {
        alert('A senha deve ter mínimo de 4 caracteres.😊');
        return false;
    }
    return true;
}