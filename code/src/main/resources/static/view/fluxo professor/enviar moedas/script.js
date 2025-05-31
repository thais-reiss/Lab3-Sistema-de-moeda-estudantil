function fecharmodal() {
    document.getElementById('modal-envio').style.display = 'none';
}

function abrirmodal() {
    document.getElementById('modal-envio').style.display = 'block';
}

window.onclick = function(event) {
    const modal = document.getElementById('modal-envio');
    if (event.target == modal) {
        fecharmodal();
    }
}