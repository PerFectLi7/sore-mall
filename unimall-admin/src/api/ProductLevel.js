import request from '@/utils/request'
import Qs from 'qs'

export function listUnimalProductLevel(query) {
  return request({
    method: 'get',
    params: {
      _gp: 'admin.adminproductlevel',
      _mt: 'list',
      ...query
    }
  })
}

export function createUnimalProductLevel(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.adminproductlevel',
      _mt: 'create',
      ...data
    })
  })
}

export function updateUnimalProductLevel(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.adminproductlevel',
      _mt: 'edit',
      ...data
    })
  })
}

export function deleteUnimalProductLevel(id) {
  return request({
    method: 'post',
    params: {
      _gp: 'admin.adminproductlevel',
      _mt: 'delete',
      id: id
    }
  })
}
