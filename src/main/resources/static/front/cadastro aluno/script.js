document.addEventListener('DOMContentLoaded', async () => {
    const select = document.getElementById('instituicao');

    fetch('http://localhost:8080/instituicoes')
        .then(res => res.json())
        .then(data => {
            data.forEach(instituicao => {
                const option = document.createElement('option');
                option.value = instituicao.id;       
                option.textContent = instituicao.nome;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Erro ao carregar instituições:', err));

    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const aluno = {
            nome:      document.getElementById('nome').value,
            cpf:       document.getElementById('cpf').value,
            email:     document.getElementById('email').value,
            rg:        document.getElementById('rg').value,
            senha:     document.getElementById('senha').value,
            curso:     document.getElementById('curso').value,
            rua:       document.getElementById('rua').value,
            bairro:    document.getElementById('bairro').value,
            cep:       document.getElementById('cep').value,
            numero:    parseInt(document.getElementById('numero').value, 10),
            saldoMoedas: 0,
            instituicao: {
                id: Number(select.value)
            }
        };
        //console.log("------ Dados do aluno: ", aluno);

        try {
            const resposta = await fetch('http://localhost:8080/alunos', {
                method:  'POST',
                headers: { 'Content-Type': 'application/json' },
                body:    JSON.stringify(aluno)
            });

            if (!resposta.ok) {
                alert("Não foi possível cadastrar o aluno. Tente novamente, por favor. 😉");
                return;
            }

            const novoAluno = await resposta.json();
            alert(`Aluno ${novoAluno.nome} cadastrado com sucesso! 🤩`);
            form.reset();
        } catch (error) {
            console.error('Erro na requisição:', error);
            alert("Erro na requisição de cadastro. Tente novamente, por favor 😉");
        }
    });
});
