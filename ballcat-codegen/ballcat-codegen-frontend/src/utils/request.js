import axios from 'axios'
import { notification } from 'ant-design-vue'

const axiosInstance = axios.create({
  baseURL: process.env.VUE_APP_API_URL_PREFIX,
  timeout: 60000 // 请求超时时间
})

const err = error => {
  if (error.response) {
    const {
      response: { status, data }
    } = error
    if (status === 403) {
      notification.error({
        message: 'Forbidden',
        description: data.message
      })
    }
    if (status === 401 && !(data.result && data.result.isLogin)) {
      notification.error({
        message: 'Unauthorized',
        description: 'Authorization verification failed'
      })
    }
  }
  return Promise.reject(error)
}

// request interceptor
axiosInstance.interceptors.request.use(config => {
  return config
}, err)

// response interceptor
axiosInstance.interceptors.response.use(response => {
  return response.data
}, err)

export { axiosInstance as axios }
