function isValidName(nome) {
    const regex = /^[a-zA-ZÀ-ÿ\s]+$/;
    if (!regex.test(nome) || nome.length < 3) {
        alert('O nome deve ter mínimo de 3 letras.😊');
        return false;
    }
    return true;
}

function isValidCPF(cpf) {
    const regex = /^[0-9]{11}$/;
    if (!regex.test(cpf)) {
        alert('O CPF deve conter exatamente 11 números.😊');
        return false;
    }
    return true;
}

function isValidEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(email)) {
        alert('O e-mail é inválido. Informe um formato como aluno@email.com.😊');
        return false;
    }
    return true;
}

function isValidRG(rg) {
    if (rg.length < 7 || !/^[A-Za-z0-9]+$/.test(rg)) {
        alert('O RG deve ter mínimo de 7 caracteres (letras e/ou números).😊');
        return false;
    }
    return true;
}

function isValidSenha(senha) {
    if (senha.length < 4) {
        alert('A senha deve ter mínimo de 4 caracteres.😊');
        return false;
    }
    return true;
}

function isValidCurso(curso) {
    const re = /^[A-Za-zÀ-ÿ\s]+$/;
    if (curso.length < 4 || !re.test(curso)) {
        alert('O nome do curso deve ter apenas letras e mínimo de 4 caracteres.😊');
        return false;
    }
    return true;
}

function isValidRuaBairro(valor, label) {
    const re = /^[A-Za-zÀ-ÿ0-9\s]+$/;
    if (valor.length < 4 || !re.test(valor)) {
        alert(`${label} deve ter mínimo de 4 caracteres (letras ou números).😊`);
        return false;
    }
    return true;
}

function isValidCEP(cep) {
    const regex = /^\d{8,}$/;
    if (!regex.test(cep)) {
        alert('O CEP deve conter apenas números e no mínimo 8 caracteres.😊');
        return false;
    }
    return true;
}

function isValidNumero(num) {
    if (!Number.isInteger(num) || num <= 0) {
        alert('O número do endereço deve ser inteiro e positivo.😊');
        return false;
    }
    return true;
}

function isValidCusto(custo) {
    if (!Number.isInteger(custo) || custo <= 0) {
        alert('O custo da vantagem deve ser inteiro e positivo.😊');
        return false;
    }
    return true;
}

function isValidFoto(foto) {
    try {
        const url = new URL(foto);
        const pathname = url.pathname.toLowerCase();
        if (!(/\.(jpe?g|png|gif|bmp|svg|webp)$/i).test(pathname)) {
            alert('A URL não termina em uma extensão de imagem válida.😊');
            return false;
        }
        return true;
    } catch (_) {
        alert('A URL da foto não é válida.😊');
        return false;
    }
}

function isValidDescricao(descricao) {
    if (descricao.length < 5) {
        alert('A descrição deve ter o mínimo de 5 caracteres.😊');
        return false;
    }
    return true;
}

function isValidCNPJ(cnpj) {
    const regex = /^[0-9]{14}$/;
    if (!regex.test(cnpj)) {
        alert('O CNPJ deve conter exatamente 14 números.😊');
        return false;
    }
    return true;
}