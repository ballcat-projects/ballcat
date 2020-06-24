import { axios } from '@/utils/request'

export function getList(templateGroupId) {
  return axios({
    url: '/gen/template/directory-entry/list/' + templateGroupId,
    method: 'get'
  })
}

export function addObj(obj) {
  return axios({
    url: '/gen/template/directory-entry',
    method: 'post',
    data: obj
  })
}

export function delObj(id, mode) {
  return axios({
    url: '/gen/template/directory-entry/' + id,
    method: 'delete',
    params: { mode: mode }
  })
}

export function putObj(obj) {
  return axios({
    url: '/gen/template/directory-entry',
    method: 'put',
    data: obj
  })
}

export function move(entryId, targetEntryId, horizontalMove) {
  return axios({
    url: '/gen/template/directory-entry/' + entryId + '/position',
    method: 'patch',
    params: { targetEntryId: targetEntryId, horizontalMove: horizontalMove }
  })
}

export function rename(entryId, name) {
  return axios({
    url: '/gen/template/directory-entry/' + entryId + '/name',
    method: 'patch',
    params: { name: name }
  })
}
