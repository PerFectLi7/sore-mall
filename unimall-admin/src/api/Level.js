import request from '@/utils/request'
import Qs from 'qs'

export function listLevel(query) {
  return request({
    method: 'get',
    params: {
      _gp: 'admin.level',
      _mt: 'list',
      ...query
    }
  })
}

export function createLevel(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.level',
      _mt: 'create',
      levelDo: JSON.stringify(data)
    })
  })
}

export function updateLevel(data) {
  return request({
    method: 'post',
    data: Qs.stringify({
      _gp: 'admin.level',
      _mt: 'edit',
      levelDo: JSON.stringify(data)
    })
  })
}

export function deleteLevel(id) {
  return request({
    method: 'post',
    params: {
      _gp: 'admin.level',
      _mt: 'delete',
      id: id
    }
  })
}
