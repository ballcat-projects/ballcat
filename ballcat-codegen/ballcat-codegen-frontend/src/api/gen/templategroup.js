import { axios } from '@/utils/request'

export function getPage(query) {
  return axios({
    url: '/gen/templategroup/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return axios({
    url: '/gen/templategroup',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return axios({
    url: '/gen/templategroup/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return axios({
    url: '/gen/templategroup/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return axios({
    url: '/gen/templategroup',
    method: 'put',
    data: obj
  })
}
