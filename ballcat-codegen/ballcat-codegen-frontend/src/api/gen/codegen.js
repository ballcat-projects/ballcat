import { axios } from '@/utils/request'

export function codeGen(dsName, genConfig) {
  return axios({
    url: '/generator/code',
    method: 'post',
    data: genConfig,
    headers: { dsName: dsName },
    responseType: 'blob'
  })
}
