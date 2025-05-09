import axios, { AxiosInstance,AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
// @ts-ignore
import cookie from "js-cookie";

const router = useRouter();

// 防止多个请求同时刷新 token，使用一个标志和等待队列
let isRefreshing = false
let refreshSubscribers: Function[] = []

function onRefreshed(newToken: string) {
    refreshSubscribers.forEach(cb => cb(newToken))
    refreshSubscribers = []
}

function addRefreshSubscriber(cb: Function) {
    refreshSubscribers.push(cb)
}

// 创建 axios 实例
const axiosInstance: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8081',
    timeout: 10000
})

// 请求拦截器
axiosInstance.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        } else {
            router.push("/login")
        }
        return config
    },
    error => Promise.reject(error)
)

// 响应拦截器
axiosInstance.interceptors.response.use(
    (response: AxiosResponse) => {
        if (response.data?.code !== "200") {
            ElMessage.error(response.data?.message || '请求失败')
            return Promise.reject(response.data)
        }
        return response
    },
    async (error) => {
        const originalRequest = error.config
        const status = error.response?.status

        if (status === 401 && !originalRequest._retry) {
            originalRequest._retry = true

            const refreshToken = cookie.get("refreshToken");
            if (!refreshToken) {
                router.push('/login')
                return Promise.reject(error)
            }

            if (!isRefreshing) {
                isRefreshing = true
                try {
                    const res = await axios.post('http://localhost:8081/refreshToken', null, {
                        params: { refreshToken }
                    })

                    const newToken = res.data.token
                    localStorage.setItem('token', newToken)
                    isRefreshing = false
                    onRefreshed(newToken)
                } catch (err) {
                    isRefreshing = false
                    localStorage.removeItem('token')
                    localStorage.removeItem('refreshToken')
                    router.push('/login')
                    return Promise.reject(err)
                }
            }

            return new Promise(resolve => {
                addRefreshSubscriber((newToken: string) => {
                    originalRequest.headers.Authorization = `Bearer ${newToken}`
                    resolve(axiosInstance(originalRequest))
                })
            })
        }

        // 其他错误处理
        if (status === 403) ElMessage.error('没有权限访问')
        else if (status === 401) ElMessage.error('登录过期，请重新登录')
        else if (status === 500) ElMessage.error('服务器错误')
        // else ElMessage.error(error.response?.data?.message || '请求出错')

        return Promise.reject(error)
    }
)

export default axiosInstance
