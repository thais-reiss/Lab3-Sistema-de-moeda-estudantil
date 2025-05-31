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

        if (!isValidNome(nome)) return;
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

function isValidNome(nome) {
    const regex = /^[a-zA-ZÀ-ÿ\s]+$/;
    if (!regex.test(nome) || nome.length < 3) {
        alert('O nome deve ter mínimo de 3 letras.😊');
        return false;
    }
    return true;
}

function isValidCusto(custo) {
    if (!Number.isInteger(custo) || custo <= 0) {
        alert('O custo da vantagem deve ser inteiro e positivo.😊');
        return false;
    }
    return true;
}

function isValidFoto(foto) {
    try {
        const url = new URL(foto);
        const pathname = url.pathname.toLowerCase();
        if (!(/\.(jpe?g|png|gif|bmp|svg|webp)$/i).test(pathname)) {
            alert('A URL não termina em uma extensão de imagem válida.😊');
            return false;
        }
        return true;
    } catch (_) {
        alert('A URL da foto não é válida.😊');
        return false;
    }
}

function isValidDescricao(descricao) {
    if (descricao.length < 5) {
        alert('A descrição deve ter o mínimo de 5 caracteres.😊');
        return false;
    }
    return true;
}