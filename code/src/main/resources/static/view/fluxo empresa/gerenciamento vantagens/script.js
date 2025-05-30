window.addEventListener('DOMContentLoaded', async () => {
    const cardContainer = document.querySelector('.card-container');
    const idEmpresa = Number(localStorage.getItem('idEmpresa'));

    try {
        const empresasRes = await fetch('http://localhost:8080/empresas');
        const empresas = await empresasRes.json();
        const empresaMap = new Map(empresas.map(e => [e.id, e.nome]));

        const vantRes = await fetch('http://localhost:8080/vantagens');
        const vantagens = await vantRes.json();

        const minhasVantagens = vantagens.filter(v => {
            if (v.empresaId != null) {
                return Number(v.empresaId) === idEmpresa;
            }
            if (v.empresa && v.empresa.id != null) {
                return Number(v.empresa.id) === idEmpresa;
            }
            return false;
        });

        cardContainer.innerHTML = '';
        if (minhasVantagens.length === 0) {
            cardContainer.innerHTML = ' <div class="empty-message"><p>Nenhuma vantagem encontrada para esta empresa. Cadastre alguma.😉</p> <img src="../../../../img/joinha.png" id="joinha"></div>';
            return;
        }

        minhasVantagens.forEach(v => {
            const empresaNome =
                v.empresa?.nome ||
                empresaMap.get(Number(v.empresaId)) ||
                '–';

            const card = document.createElement('div');
            card.classList.add('card');
            card.innerHTML = `
        <div class="img-card">
          <img src="${v.fotoUrl}" alt="Imagem de ${v.nome}" onerror="this.onerror=null; this.src='../../../../img/vantagemPadrao.png'"/>
        </div>
        <div class="info">
          <p><strong>Nome: </strong>${v.nome}</p>
          <p><strong>Empresa: </strong>${empresaNome}</p>
          <p><strong>Descrição: </strong>${v.descricao}</p>
          <p><strong>Custo: </strong>${v.custoMoedas} moedas</p>
        </div>
        <div class="acoes">
            <button class="editar" data-id="${v.id}"><i class="bi bi-pencil-square icon"></i></button>
            <button class="excluir" data-id="${v.id}"><i class="bi bi-trash3-fill icon"></i></button>
        </div>
      `;
            cardContainer.appendChild(card);
        });
        cardContainer.querySelectorAll('.editar').forEach(btn => {
            btn.addEventListener('click', () => {
                const vantagemId = btn.dataset.id;
                localStorage.setItem("idVantagem", vantagemId);
                window.location.href = "../editar vantagem/editar.html";
            });
        });
        cardContainer.querySelectorAll('.excluir').forEach(btn => {
            btn.addEventListener('click', () => {
                const vantagemId = btn.dataset.id;
                if (!confirm('Deseja realmente excluir esta vantagem?')) return;
                fetch(`http://localhost:8080/vantagens/${vantagemId}`, {
                    method: 'DELETE'
                })
                    .then(res => {
                        if (!res.ok) throw new Error('Erro ao excluir');
                        btn.closest('.card').remove();
                        alert("Vantagem excluída com sucesso!😉");
                    })
                    .catch(() => alert("Não foi possível excluir a vantagem. Tente novamente, por favor.😉"));
            });
        });

    } catch (err) {
        console.error('Erro ao carregar dados:', err);
        cardContainer.innerHTML = `<p class="erro">Não foi possível carregar as vantagens.Tente novamente, por favor.</p>`;
    }
});