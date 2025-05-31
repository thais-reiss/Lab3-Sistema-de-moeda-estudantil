window.addEventListener('DOMContentLoaded', async () => {
    const idProfessor = localStorage.getItem('idProfessor');
    if (!idProfessor) {
        alert('Você precisa estar logado como professor para ver este extrato.');
        window.location.href = '../../login/login.html';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/extratos/professor/${idProfessor}`);
        if (!response.ok) {
            alert('Erro ao carregar transações. Recarregue a página e tente novamente.😉');
            return;
        }

        const transacoes = await response.json();

        let totalEnviado = 0;
        transacoes.forEach(tx => {
            totalEnviado += tx.quantidade;
        });
        const totalRecebido = 1000;

        try {
            const respProfessor = await fetch(`http://localhost:8080/professores/${idProfessor}`);
            const professor = await respProfessor.json();
            const saldoAtual = professor.saldoMoedas;

            document.getElementById('valor-recebido').innerText = `${totalRecebido} moedas`;
            document.getElementById('valor-enviado').innerText = `${totalEnviado} moedas`;
            document.getElementById('valor-saldo').innerText = `${saldoAtual} moedas`;
        } catch (error) {
            alert('Houve um erro ao carregar o saldo atual. Tente recarregar, por favor.😉')
        }

    } catch (error) {
        alert('Não foi possível conectar-se ao servidor. Tente novamente mais tarde.😉');
    }
});
