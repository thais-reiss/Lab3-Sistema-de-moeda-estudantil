function fecharmodal() {
    document.getElementById('modal-envio').style.display = 'none';
}

function abrirmodal() {
    document.getElementById('modal-envio').style.display = 'block';
}

window.onclick = function (event) {
    const modal = document.getElementById('modal-envio');
    if (event.target == modal) {
        fecharmodal();
    }
}

window.addEventListener('DOMContentLoaded', async () => {
    const idProfessor = localStorage.getItem('idProfessor');

    try {
        const respProfessor = await fetch(`http://localhost:8080/professores/${idProfessor}`);
        if (!respProfessor.ok) {
            alert('Erro ao carregar dados do professor. Faça login novamente.');
            window.location.href = '../../login/login.html';
            return;
        }
        const professor = await respProfessor.json();

        const instituicaoId = professor.instituicao.id;

        const respAlunos = await fetch('http://localhost:8080/alunos');
        if (!respAlunos.ok) {
            alert('Erro ao carregar lista de alunos. Tente novamente recarregar, por favor.😉');
            return;
        }
        const listaAlunos = await respAlunos.json();

        const alunosDaMesmaInstituicao = listaAlunos.filter(aluno => {
            return aluno.instituicao && aluno.instituicao.id === instituicaoId;
        });

        const tbody = document.getElementById('corpo-tabela');
        tbody.innerHTML = '';

        if (alunosDaMesmaInstituicao.length === 0) {
            const trVazio = document.createElement('tr');
            trVazio.innerHTML = `
        <td colspan="5" style="text-align:center; padding: 1rem;">
          Nenhum aluno encontrado para a sua instituição.
        </td>
      `;
            tbody.appendChild(trVazio);
        } else {
            alunosDaMesmaInstituicao.forEach(aluno => {
                const tr = document.createElement('tr');

                tr.innerHTML = `
          <td>${aluno.nome || ''}</td>
          <td>${aluno.email || ''}</td>
          <td>${aluno.curso || ''}</td>
          <td>${aluno.instituicao?.nome || ''}</td>
          <td>
            <button class="btn-enviar-moedas" data-id-aluno="${aluno.id}">
              <span class="icon"><i class="bi bi-arrow-up-circle"></i></span>
            </button>
          </td>
        `;

                tbody.appendChild(tr);
            });
        }

        document.querySelectorAll('.btn-enviar-moedas').forEach(botao => {
            botao.addEventListener('click', event => {
                const idDoAlunoClicado = event.currentTarget.getAttribute('data-id-aluno');
                localStorage.setItem('idAlunoSelecionado', idDoAlunoClicado);
                abrirmodal();
            });
        });

        const btnConfirmarEnvio = document.getElementById('confirmar-envio');
        if (btnConfirmarEnvio) {
            btnConfirmarEnvio.addEventListener('click', async () => {
                const valorMoedas = parseInt(document.getElementById('valor-moedas').value);
                const motivo = document.getElementById('motivo-envio').value;
                const idAlunoSelecionado = localStorage.getItem('idAlunoSelecionado');

                if (!idAlunoSelecionado) {
                    alert('Nenhum aluno selecionado para o envio de moedas.');
                    fecharmodal();
                    return;
                }
                if (isNaN(valorMoedas) || valorMoedas < 1) {
                    alert('Informe um valor de moedas válido (no mínimo 1).');
                    return;
                }
                if (!motivo.trim()) {
                    alert('Informe o motivo do envio.');
                    return;
                }
                const idRemetente = idProfessor; 
                const idDestinatario = idAlunoSelecionado;

                try {
                    const responseEnvio = await fetch('http://localhost:8080/transacoes/enviar-moedas', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            professor: { id: Number(idRemetente) },
                            aluno: { id: Number(idDestinatario) },
                            quantidade: valorMoedas,
                            motivo: motivo,
                            data: new Date().toISOString()
                        })
                    });

                    if (responseEnvio.ok) {
                        alert('Moedas enviadas com sucesso para aluno(a)!😉');
                        document.getElementById('valor-moedas').value = '';
                        document.getElementById('motivo-envio').value = '';
                        localStorage.removeItem('idAlunoSelecionado');
                        fecharmodal();
                    } else {
                        const msgErro = await responseEnvio.text();
                        alert('Sinto muito. Houve um erro ao enviar moedas: ' + msgErro);

                    }
                } catch (err) {
                    alert('Erro ao conectar-se ao servidor. Tente novamente mais tarde.');
                }
            });
        }

    } catch (err) {
        alert('Ocorreu um erro ao inicializar a página. Recarregue e tente novamente.');
    }
});