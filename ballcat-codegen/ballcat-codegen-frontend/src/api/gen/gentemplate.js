import { axios } from '@/utils/request'

export function getPage(query) {
  return axios({
    url: '/gen/template/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return axios({
    url: '/gen/template',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return axios({
    url: '/gen/template/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return axios({
    url: '/gen/template/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return axios({
    url: '/gen/template',
    method: 'put',
    data: obj
  })
}
