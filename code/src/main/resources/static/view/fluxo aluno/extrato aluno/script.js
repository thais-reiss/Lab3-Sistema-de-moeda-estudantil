window.addEventListener('DOMContentLoaded', async () => {
    const idAluno = localStorage.getItem('idAluno');
    if (!idAluno) {
        alert('Você precisa estar logado como aluno para acessar este extrato.');
        window.location.href = '../../login/login.html';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/extratos/aluno/${idAluno}`);
        if (!response.ok) {
            alert('Erro ao carregar suas transações. Recarregue a página e tente novamente.😉');
            return;
        }

        const transacoes = await response.json();

        let totalRecebido = 0;
        transacoes.forEach(tx => {
            totalRecebido += tx.quantidade;
        });

        const totalGasto = 0; //mudar na próxima sprint

        try {
            const respAluno = await fetch(`http://localhost:8080/alunos/${idAluno}`);
            const aluno = await respAluno.json();
            const saldoAtual = aluno.saldoMoedas;

            document.getElementById('valor-recebido').innerText = `${totalRecebido} moedas`;
            document.getElementById('valor-gasto').innerText = `${totalGasto} moedas`;
            document.getElementById('valor-saldo').innerText = `${saldoAtual} moedas`;
        } catch (error) {
            alert('Houve um erro ao carregar o saldo atual. Tente recarregar, por favor.😉')
        }
    } catch (error) {
        alert('Não foi possível conectar-se ao servidor. Tente novamente mais tarde.');
    }
});