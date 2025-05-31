window.addEventListener('DOMContentLoaded', async () => {
  const idAluno = localStorage.getItem('idAluno');

  if (!idAluno) {
    alert('Você precisa estar logado para ver o perfil.');
    window.location.href = '../../login/login.html';
    return;
  }

  try {
    const response = await fetch(`http://localhost:8080/alunos/${idAluno}`);
    if (!response.ok) {
      alert('Houve um erro ao carregar os dados. Tente novamente, por favor. 😉');
      return;
    }

    const aluno = await response.json();

    document.getElementById('campo-nome').innerText = aluno.nome || '';
    document.getElementById('campo-email').innerText = aluno.email || '';
    document.getElementById('campo-cpf').innerText = aluno.cpf || '';
    document.getElementById('campo-rg').innerText = aluno.rg || '';
    document.getElementById('campo-curso').innerText = aluno.curso || '';
    document.getElementById('campo-instituicao').innerText = aluno.instituicao.nome || '';
    document.getElementById('campo-rua').innerText = aluno.rua || '';
    document.getElementById('campo-bairro').innerText = aluno.bairro || '';
    document.getElementById('campo-cep').innerText = aluno.cep || '';
    document.getElementById('campo-numero').innerText = aluno.numero || '';
  } catch (error) {
    alert('Não foi possível conectar-se ao servidor. Tente novamente mais tarde. 😊');
  }

  const btnAtualizar = document.getElementById('btnAtualizar');
  if (btnAtualizar) {
    btnAtualizar.addEventListener('click', () => {
      window.location.href = '../editar aluno/editaraluno.html';
    });
  }

  const btnExcluir = document.getElementById('btnExcluir');
  if (btnExcluir) {
    btnExcluir.addEventListener('click', async () => {
      const confirmar = confirm('Tem certeza que deseja excluir sua conta?😢');
      if (!confirmar) return;

      try {
        const response = await fetch(`http://localhost:8080/alunos/${idAluno}`, {
          method: 'DELETE'
        });
        if (response.ok) {
          alert('Conta excluída com sucesso!😉');
          localStorage.removeItem('idAluno');
          window.location.href = '../../login/login.html';
        } else {
          alert('Não foi possível excluir a conta. Tente novamente mais tarde.😉');
        }
      } catch (err) {
        alert('Erro ao comunicar com o servidor. Tente novamente mais tarde.');
      }
    });
  }
});
