document.addEventListener('DOMContentLoaded', () => {
    const cardContainer = document.querySelector('.card-container');
    const empresasUrl = 'http://localhost:8080/empresas';
    const vantagensUrl = 'http://localhost:8080/vantagens';

    const fetchEmpresas = fetch(empresasUrl)
        .then(res => {
            if (!res.ok) throw new Error('Falha ao carregar empresas');
            return res.json();
        })
        .then(empresas => {
            const map = new Map();
            empresas.forEach(e => map.set(e.id, e.nome));
            return map;
        });

    const fetchVantagens = fetch(vantagensUrl)
        .then(res => {
            if (!res.ok) throw new Error('Falha ao carregar vantagens');
            return res.json();
        });

    Promise.all([fetchEmpresas, fetchVantagens])
        .then(([empresaMap, vantagens]) => {
            cardContainer.innerHTML = '';

            vantagens.forEach(v => {
                const empresaIdNum = v.empresaId != null
                    ? Number(v.empresaId)
                    : v.empresa?.id;
                const empresaNome = v.empresa?.nome
                    || empresaMap.get(empresaIdNum)
                    || '–';

                const card = document.createElement('div');
                card.classList.add('card');
                card.innerHTML = `
        <div class="img-card">
          <img src="${v.fotoUrl}" alt="Imagem de ${v.nome}" />
        </div>
        <div class="info">
          <p><strong>Nome: </strong>${v.nome}</p>
          <p><strong>Empresa: </strong>${empresaNome}</p>
          <p><strong>Descrição: </strong>${v.descricao}</p>
          <p><strong>Custo: </strong>${v.custoMoedas} moedas</p>
        </div>
      `;
                cardContainer.appendChild(card);
            });
        })
        .catch(err => {
            console.error(err);
            cardContainer.innerHTML = `<p class="erro">Não foi possível carregar as vantagens.</p>`;
        });
});
