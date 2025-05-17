document.addEventListener('DOMContentLoaded', () => {
    const tbody = document.querySelector('.tabela tbody');

    fetch('http://localhost:8080/empresas')
        .then(res => res.json())
        .then(empresas => {
            tbody.innerHTML = '';

            empresas.forEach(empresa => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
          <td>${empresa.nome}</td>
          <td>${empresa.email}</td>
          <td>${empresa.cnpj}</td>
          <td>
            <button class="btn-editar" data-id="${empresa.id}">
              <span class="icon"><i class="bi bi-pencil-square"></i></span>
            </button>
            <button class="btn-excluir" data-id="${empresa.id}">
              <span class="icon"><i class="bi bi-trash3-fill"></i></span>
            </button>
          </td>
        `;
                tbody.appendChild(tr);
            });

            tbody.querySelectorAll('.btn-editar').forEach(btn => {
                btn.addEventListener('click', () => {
                    const empresaId = btn.dataset.id;
                    localStorage.setItem("idEmpresa", empresaId);
                    window.location.href = "../editar empresa/editarempresa.html";
                });
            });
            tbody.querySelectorAll('.btn-excluir').forEach(btn => {
                btn.addEventListener('click', () => {
                    const empresaId = btn.dataset.id;
                    if (!confirm('Deseja realmente excluir esta empresa?')) return;
                    fetch(`http://localhost:8080/empresas/${empresaId}`, {
                        method: 'DELETE'
                    })
                        .then(res => {
                            if (!res.ok) throw new Error('Erro ao excluir');
                            btn.closest('tr').remove();
                            alert("Empresa excluída com sucesso!😉");
                        })
                        .catch(() => alert("Não foi possível excluir a empresa. Tente novamente, por favor.😉"));
                });
            });
        })
        .catch(err => console.error('Erro ao buscar empresas:', err));
});
