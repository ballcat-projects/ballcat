import { axios } from '@/utils/request'

export function getPage(query) {
  return axios({
    url: '/gen/datasourceconfig/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return axios({
    url: '/gen/datasourceconfig',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return axios({
    url: '/gen/datasourceconfig/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return axios({
    url: '/gen/datasourceconfig/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return axios({
    url: '/gen/datasourceconfig',
    method: 'put',
    data: obj
  })
}

export function getSelectData() {
  return axios({
    url: '/gen/datasourceconfig/select',
    method: 'get'
  })
}
