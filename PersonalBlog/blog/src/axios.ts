import axios from 'axios';
import { useRouter } from 'vue-router';

const instance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000
});

let router: ReturnType<typeof useRouter>;
export const setRouter = (r: ReturnType<typeof useRouter>) => {
    router = r;
};

instance.interceptors.request.use(config => {
    const whiteList = ['/login', '/register'];
    // @ts-ignore
    if (!whiteList.includes(config.url)) {
        const token = localStorage.getItem('token');
        if (!token) {
            if (router) {
                router.push('/login');
            }
            return Promise.reject(new Error('未登录，请先登录'));
        }
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

export default instance;
