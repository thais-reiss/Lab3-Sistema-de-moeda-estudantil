const btn = document.getElementById("btnAtualizar");
const idVantagem = localStorage.getItem("idVantagem");
const idEmpresa = Number(localStorage.getItem('idEmpresa'));
//const select = document.getElementById('empresa');

window.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch(`http://localhost:8080/vantagens/${idVantagem}`);

        if (!response.ok) {
            alert("Sinto muito. Houve um erro ao carregar os dados da vantagem. Tente novamente.😉");
            return;
        }
        const vantagem = await response.json();

        /*const respEmpresa = await fetch('http://localhost:8080/empresas');
        if (!respEmpresa.ok) throw new Error('Falha ao carregar empresas.');
        const empresas = await respEmpresa.json();

        empresas.forEach(empresa => {
            const option = document.createElement('option');
            option.value = empresa.id;
            option.textContent = empresa.nome;
            if (empresa.id === vantagem.empresaId) {
                option.selected = true;
            }
            select.appendChild(option);
        });*/

        document.getElementById('nome').value = vantagem.nome || "";
        document.getElementById('custo').value = vantagem.custoMoedas || "";
        document.getElementById('foto').value = vantagem.fotoUrl || "";
        document.getElementById('descricao').value = vantagem.descricao || "";
    } catch (error) {
        alert("Sinto muito. Houve um erro ao carregar os dados da vantagem. Tente novamente.😉");
    }
});

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
        const resposta = await fetch(`http://localhost:8080/vantagens/${idVantagem}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(vantagem)
        });

        if (!resposta.ok) {
            alert("Não foi possível atualizar a vantagem. Tente novamente, por favor.😉");
            return;
        }

        const vantagemAtualizada = await resposta.json();
        alert(`Vantagem ${vantagemAtualizada.nome} atualizada com sucesso!🤩`);
        form.reset();
        window.location.href = "../gerenciamento vantagens/vantagem.html";
    } catch (error) {
        alert("Erro na requisição de atualização. Tente novamente, por favor.😉");
    }
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