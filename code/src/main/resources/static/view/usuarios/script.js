document.addEventListener('DOMContentLoaded', () => {
    const btnAluno = document.getElementById('cadastroAluno');
    const btnEmpresa = document.getElementById('cadastroEmpresa');

    btnAluno.addEventListener('click', () => {
        window.location.href = '../fluxo aluno/cadastro aluno/cadastroaluno.html';
    });

    btnEmpresa.addEventListener('click', () => {
        window.location.href = '../fluxo empresa/cadastro empresa/cadastroempresa.html';
    });
});
