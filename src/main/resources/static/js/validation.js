function validateEmail() {
    const email = document.getElementById('email').value;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;  // Regex para validação de email
    if (!emailRegex.test(email)) {
        alert('Por favor, insira um email válido.');
        return false;
    }
    return true;
}
