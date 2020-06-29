import { axios } from '@/utils/request'

export function getPage(query) {
  return axios({
    url: '/gen/template/group/page',
    method: 'get',
    params: query
  })
}

export function addObj(obj) {
  return axios({
    url: '/gen/template/group',
    method: 'post',
    data: obj
  })
}

export function getObj(id) {
  return axios({
    url: '/gen/template/group/' + id,
    method: 'get'
  })
}

export function delObj(id) {
  return axios({
    url: '/gen/template/group/' + id,
    method: 'delete'
  })
}

export function putObj(obj) {
  return axios({
    url: '/gen/template/group',
    method: 'put',
    data: obj
  })
}

export function getSelectData() {
  return axios({
    url: '/gen/template/group/select',
    method: 'get'
  })
}
