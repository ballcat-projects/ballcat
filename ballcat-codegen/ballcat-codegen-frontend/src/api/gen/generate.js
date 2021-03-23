import { axios } from '@/utils/request'

export function getTableInfoPage(dsName, query) {
  return axios({
    url: '/table-info/page',
    method: 'get',
    params: query,
    headers: { dsName: dsName }
  })
}

export function preview(dsName, genConfig) {
  return axios({
    url: '/preview',
    method: 'post',
    data: genConfig,
    headers: { dsName: dsName },
  })
}

export function generate(dsName, genConfig) {
  return axios({
    url: '/generate',
    method: 'post',
    data: genConfig,
    headers: { dsName: dsName },
    responseType: 'blob'
  })
}
