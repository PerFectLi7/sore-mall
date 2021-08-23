<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button v-permission="['level:level:create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="tabledatas"
      size="small"
      element-loading-text="正在查询中。。。"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="等级ID" prop="id" >
          <template slot-scope="scope">
              <span>{{scope.row.id}}</span>
          </template>
      </el-table-column>
      <el-table-column align="center" label="名称" prop="name" >
          <template slot-scope="scope">
              <span v-if="scope.row.show">
                  <el-input size="mini" placeholder="请输入内容" v-model="scope.row.name"></el-input>
              </span>
              <span v-else>{{scope.row.name}}</span>
          </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="350" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <div v-show="!scope.row.show">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row,scope.$index)">修改</el-button>
          <el-button v-permission="['product:product:delete']" type="danger" style="margin:5px" size="mini" @click="handleDelete(scope.row,scope.$index)">删除</el-button>
          </div>
          <div v-show="scope.row.show">
          <el-button  type="primary" size="mini" @click="handleCane(scope.row,scope.$index)">取消</el-button>
          <el-button v-permission="['product:product:edit']" type="primary" size="mini" @click="handleSave(scope.row,scope.$index)">保存</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>
  </div>
</template>

<script>
import Vue from 'vue'
import { listLevel, createLevel, updateLevel, deleteLevel} from '@/api/level'
import BackToTop from '@/components/BackToTop'
import { MessageBox } from 'element-ui'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'BoxList',
  components: { BackToTop, Pagination },
  data() {
    return {
      tabledatas: [],
      olderRow:undefined,
      action:undefined,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
      },
      selectedIds: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listLevel(this.listQuery)
        .then(response => {
          this.tabledatas = response.data.data.items
          this.tabledatas.map(i => {
            i.show = false
            return i
          })
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
          this.tabledatas = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
                let list = {
                    id: "",
                    name: "",
                    show: true
                }
                this.tabledatas.push(list)
                this.action = "create"
    },
    handleCane(row, index) {
              if(!row.id){
                this.tabledatas.splice(index, 1)
              }else{
                row.show = row.show ? false : true
                Vue.set(this.tabledatas, index, this.olderRow)
            }
    },
    handleSave(row,index){
            if (row.name) {
              let method = ""
              let successWords = ''
            if (this.action === 'edit') {
              method = updateLevel
              successWords = '编辑成功'
            } else if (this.action === 'create') {
              method = createLevel
              successWords = '创建成功'
            }
            method(row)
              .then(response => {
                this.$notify.success({
                  title: '成功',
                  message: successWords
                })
                row = JSON.parse(JSON.stringify(response.data.data))
                row.show = false
                Vue.set(this.tabledatas, index, row)
              })
              .catch(response => {
                MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
                  confirmButtonText: '确定',
                  type: 'error'
                })
                this.handleCancel(row,index)
              })
            } else {
                this.$notify.error({
                  title: '失败',
                  message: '等级名称不能为空'
                })
            }
    },
    handleEdit(row, index) {
      this.olderRow = JSON.parse(JSON.stringify(row)); 
      row.show = row.show ? false : true
      Vue.set(this.tabledatas, index, row)
      this.action = 'edit'
    },
    handleInfo(id) {
      this.$router.push({ path: '/box/bplist', query: { id: id }})
    },
    handleDelete(row, index) {
        this.$confirm('此操作将永久删除奖箱--' + row.title + '--, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteLevel(row.id)
            .then(response => {
              this.$notify.success({
                title: '成功',
                message: '删除成功'
              })
              this.tabledatas.splice(index, 1)
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }).catch(() => {
          return false
        })
    }
  }
}
</script>
