import { axios } from '@/utils/request'

export function getPage(query) {
  return axios({
    url: '/gen/datasource-config/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return axios({
    url: '/gen/datasource-config',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return axios({
    url: '/gen/datasource-config/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return axios({
    url: '/gen/datasource-config/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return axios({
    url: '/gen/datasource-config',
    method: 'put',
    data: obj
  })
}

export function getSelectData() {
  return axios({
    url: '/gen/datasource-config/select',
    method: 'get'
  })
}
