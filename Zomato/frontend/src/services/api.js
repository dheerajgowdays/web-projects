import axios from "axios";
import { useAuthStore } from '../store/AuthStore';
import { use } from "react";

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localost:8080/api/v1',
    headers: {'Content-Type':'application/json'},
    timeout: 10000,
})

api.interceptors.request.use((config) =>{
    const token = useAuthStore.getState().accessToken;
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
})

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const original = error.config;

        if (error.response?.status === 401 && !original._retry){
            original._retry = true;
            try{
                const refreshToken = useAuthStore.getState().refreshToken;
                const res = await axios.post(
                    `${import.meta.env.VITE_API_URL}/auth/refresh`,
                    {refreshToken}
                );
                const {accessToken} = res.data.data;
                useAuthStore.getState().setAccessToken(accessToken);
                return api(original);
            }
            catch{
                useAuthStore.getState().logout();
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    });

export default api;