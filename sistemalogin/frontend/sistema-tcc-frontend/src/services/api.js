import axios from 'axios';

// Cria uma instância do Axios com a URL base da sua API
const api = axios.create({
    baseURL: 'http://localhost:8080/api'
});

// Interceptor: Adiciona o token JWT a TODAS as requisições, se ele existir
api.interceptors.request.use(async config => {
    // Busca o token no localStorage do navegador
    const token = localStorage.getItem('authToken');

    // Se o token existir, adiciona o cabeçalho de autorização
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

export default api;
