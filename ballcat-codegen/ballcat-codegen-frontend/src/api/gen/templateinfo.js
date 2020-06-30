import { axios } from '@/utils/request'

export function getObj(id) {
  return axios({
    url: '/gen/template/info/' + id,
    method: 'get'
  })
}

export function getList(groupId) {
  return axios({
    url: '/gen/template/info/list/' + groupId,
    method: 'get'
  })
}

export function putObj(obj) {
  return axios({
    url: '/gen/template/info',
    method: 'put',
    data: obj
  })
}
