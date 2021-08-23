import request from '@/utils/request'
import Qs from 'qs'

export function listBoxProduct(query) {
  return request({
    method: 'get',
    params: {
      _gp: 'admin.boxproduct',
      _mt: 'list',
      ...query
    }
  })
}

export function createBoxProduct(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.boxproduct',
      _mt: 'create',
      BoxProductDTO: JSON.stringify(data)
    })
  })
}

export function updateBoxProduct(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.boxproduct',
      _mt: 'edit',
      BoxProductDTO: JSON.stringify(data)
    })
  })
}

export function deleteBoxProduct(id) {
  return request({
    method: 'post',
    params: {
      _gp: 'admin.boxproduct',
      _mt: 'delete',
      id: id
    }
  })
}
