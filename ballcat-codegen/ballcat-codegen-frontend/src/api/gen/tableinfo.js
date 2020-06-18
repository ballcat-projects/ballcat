import { axios } from '@/utils/request'

export function getPage(dsName, query) {
  return axios({
    url: '/tableinfo/page',
    method: 'get',
    params: query,
    headers: { dsName: dsName }
  })
}
