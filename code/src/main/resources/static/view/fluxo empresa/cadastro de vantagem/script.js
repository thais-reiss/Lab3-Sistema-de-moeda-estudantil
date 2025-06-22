import {isValidName, isValidCusto, isValidFoto, isValidDescricao} from './validações/validacoes.js'

document.addEventListener('DOMContentLoaded', async () => {
    const idEmpresa = Number(localStorage.getItem('idEmpresa'));

    /*const select = document.getElementById('empresa');
    fetch('http://localhost:8080/empresas')
        .then(res => res.json())
        .then(data => {
            data.forEach(empresa => {
                const option = document.createElement('option');
                option.value = empresa.id;
                option.textContent = empresa.nome;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Erro ao carregar empresas:', err)); */

    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const nome = document.getElementById('nome').value.trim();
        const custo = parseInt(document.getElementById('custo').value, 10);
        const foto = document.getElementById('foto').value.trim();
        const descricao = document.getElementById('descricao').value.trim();

        if (!isValidName(nome)) return;
        if (!isValidCusto(custo)) return;
        if (!isValidFoto(foto)) return;
        if (!isValidDescricao(descricao)) return;

        const vantagem = {
            nome: nome,
            descricao: descricao,
            fotoUrl: foto,
            custoMoedas: custo,
            empresaId: idEmpresa
        };

        try {
            const resposta = await fetch('http://localhost:8080/vantagens', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(vantagem)
            });

            if (!resposta.ok) {
                alert("Não foi possível cadastrar a vantagem. Tente novamente, por favor.😉");
                return;
            }

            const novaVantagem = await resposta.json();
            alert(`Vantagem ${novaVantagem.nome} cadastrada com sucesso!🤩`);
            form.reset();
            window.location.href = "../gerenciamento vantagens/vantagem.html";
        } catch (error) {
            console.error('Erro na requisição:', error);
            alert("Erro na requisição de cadastro. Tente novamente, por favor.😉");
        }
    });
});
