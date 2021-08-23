<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.boxId"
        style="width: 200px"
        class="filter-item"
        clearable
        placeholder="请选择抽奖箱"
      >
        <el-option v-for="(key,index) in boxs" :key="index" :label="key.name" :value="key.id" />
      </el-select>
      <el-select
        v-model="listQuery.levelId"
        style="width: 200px"
        class="filter-item"
        clearable
        placeholder="请选择奖品等级"
      >
        <el-option v-for="(key,index) in levels" :key="index" :label="key.name" :value="key.id" />
      </el-select>
      <el-select
        v-model="listQuery.productId"
        style="width: 200px"
        class="filter-item"
        clearable
        placeholder="请选择商品"
      >
        <el-option v-for="(key,index) in products" :key="index" :label="key.title" :value="key.id" />
      </el-select>
      <el-button v-permission="['box:box:list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button v-permission="['product:product:create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
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
    
      <el-table-column align="center" label="ID" prop="id" >
        <input />
          <template slot-scope="scope">
              <span>{{scope.row.id}}</span>
          </template>
      </el-table-column>
      <el-table-column align="center" min-width="100" label="奖箱名称">
          <template slot-scope="scope">
              <span v-if="scope.row.show">
                  <el-select v-model="scope.row.boxId" filterable placeholder="请选择">
                    <el-option 
                      v-for="item in boxs"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                      >
                    </el-option>
                  </el-select>
              </span>
              <span v-else>{{scope.row.boxName}}</span>
          </template>
      </el-table-column>
      <el-table-column align="center" min-width="100" label="商品名称">
          <template slot-scope="scope">
              <span v-if="scope.row.show">
                  <el-select v-model="scope.row.productId" filterable placeholder="请选择">
                    <el-option 
                      v-for="item in products"
                      :key="item.id"
                      :label="item.title"
                      :value="item.id"
                      >
                    </el-option>
                  </el-select>
              </span>
              <span v-else>{{scope.row.productName}}</span>
          </template>
      </el-table-column>
      <el-table-column align="center" min-width="100" label="奖品等级名称">
          <template slot-scope="scope">
              <span v-if="scope.row.show">
                  <el-select v-model="scope.row.levelId" filterable placeholder="请选择">
                    <el-option
                      v-for="item in levels"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
              </span>
              <span v-else>{{scope.row.levelName}}</span>
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
    <div v-show="isActive">
      <img :src="IMG" width="50" height="50">
    </div>

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
import { updateBoxProduct, createBoxProduct, deleteBoxProduct, listBoxProduct} from '@/api/BoxProduct'
import BackToTop from '@/components/BackToTop'
import { MessageBox } from 'element-ui'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'BoxProductList',
  components: { BackToTop, Pagination },
  data() {
    return {
      tabledatas: [],
      olderRow:undefined,
      action:undefined,
      total: 0,
      isActive:false,
      IMG:undefined,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        id: undefined,
        productId:undefined,
        levelId:undefined
      },
      boxsDetail: '',
      products:[],
      levels:[],
      boxs:[],
      BoxProductDTO:{},
      selectedIds: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listBoxProduct(this.listQuery)
        .then(response => {
          this.tabledatas = response.data.data.boxProductDTOPage.items
          this.products = response.data.data.productSimpleDTOS
          this.levels = response.data.data.levelDOS
          this.boxs = response.data.data.boxDOS
          this.tabledatas.map(i => {
            i.show = false
            return i
          })
          this.total = response.data.data.boxProductDTOPage.total
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
                    boxId: "",
                    boxName: "",
                    productId: "",
                    productName:"",
                    levelId:"",
                    levelName:"",
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
              let method = ""
              let successWords = ''
            if (this.action === 'edit') {
              method = updateBoxProduct
              successWords = '编辑成功'
            } else if (this.action === 'create') {
              method = createBoxProduct
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

    },
    handleEdit(row, index) {
      this.olderRow = JSON.parse(JSON.stringify(row)); 
      row.show = row.show ? false : true
      Vue.set(this.tabledatas, index, row)
      this.action = 'edit'
    },
    handleDelete(row, index) {
        this.$confirm('此操作将永久删除奖箱--' + row.title + '--, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteBoxProduct(row.id)
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
