document.addEventListener('DOMContentLoaded', () => {
    const tbody = document.querySelector('.tabela tbody');

    fetch('http://localhost:8080/alunos')
        .then(res => res.json())
        .then(alunos => {
            tbody.innerHTML = '';

            alunos.forEach(aluno => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
          <td>${aluno.nome}</td>
          <td>${aluno.email}</td>
          <td>${aluno.curso}</td>
          <td>${aluno.instituicao.nome}</td>
          <td>
            <button class="btn-editar" data-id="${aluno.id}">
              <span class="icon"><i class="bi bi-pencil-square"></i></span>
            </button>
            <button class="btn-excluir" data-id="${aluno.id}">
              <span class="icon"><i class="bi bi-trash3-fill"></i></span>
            </button>
          </td>
        `;
                tbody.appendChild(tr);
            });

            tbody.querySelectorAll('.btn-editar').forEach(btn => {
                btn.addEventListener('click', () => {
                    const alunoId = btn.dataset.id;
                    localStorage.setItem("idAluno", alunoId);
                    window.location.href = "../editar aluno/editaraluno.html";
                });
            });
            tbody.querySelectorAll('.btn-excluir').forEach(btn => {
                btn.addEventListener('click', () => {
                    const alunoId = btn.dataset.id;
                    if (!confirm('Deseja realmente excluir este(a) aluno(a)?')) return;
                    fetch(`http://localhost:8080/alunos/${alunoId}`, {
                        method: 'DELETE'
                    })
                        .then(res => {
                            if (!res.ok) throw new Error('Erro ao excluir');
                            btn.closest('tr').remove();
                            alert("Aluno(a) excluído(a) com sucesso!😉");
                        })
                        .catch(() => alert("Não foi possível excluir o(a) aluno(a). Tente novamente, por favor.😉"));
                });
            });
        })
        .catch(err => console.error('Erro ao buscar alunos:', err));
});
