import request from '@/utils/request'
import Qs from 'qs'

export function listBox(query) {
  return request({
    method: 'get',
    params: {
      _gp: 'admin.box',
      _mt: 'list',
      ...query
    }
  })
}

export function boxProductByBid(query) {
  return request({
    method: 'get',
    params: {
      _gp: 'admin.box',
      _mt: 'detail',
      ...query
    }
  })
}

export function createBox(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.box',
      _mt: 'create',
      BoxDo: JSON.stringify(data)
    })
  })
}

export function editBox(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.box',
      _mt: 'edit',
      BoxDo: JSON.stringify(data)
    })
  })
}

export function deleteBox(id) {
  return request({
    method: 'post',
    params: {
      _gp: 'admin.box',
      _mt: 'delete',
      id: id
    }
  })
}
