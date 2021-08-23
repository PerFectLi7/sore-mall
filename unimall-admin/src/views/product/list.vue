<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.title" clearable class="filter-item" style="width: 200px;" placeholder="输入商品名称" />
      <el-select v-model="listQuery.status" clearable style="width: 200px" class="filter-item" placeholder="选择商品状态" >
        <el-option v-for="(item,index) in goodsStatusMap" :key="index" :label="item.name" :value="item.value" />
      </el-select>
      <el-button v-permission="['product:product:list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button v-permission="['product:product:create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <el-button v-permission="['product:product:batchdelete']" :disabled="selectedIds.length === 0" class="filter-item" type="danger" icon="el-icon-delete" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      size="small"
      element-loading-text="正在查询中。。。"
      border
      fit
      highlight-current-row
      @selection-change="handleSelectionChange"
    >

      <el-table-column align="center" label="商品ID" prop="id" />

      <el-table-column align="center" min-width="100" label="名称" prop="title" />

      <el-table-column align="center" label="积分" prop="points" />

      <el-table-column align="center" property="img" label="图片">
        <template slot-scope="scope">
          <img :src="scope.row.img" width="40" >
        </template>
      </el-table-column>

      <el-table-column align="center" label="商品详细介绍" prop="detail">
        <template slot-scope="scope">
          <el-dialog :visible.sync="detailDialogVisible" title="商品详情">
            <div v-html="goodsDetail" />
          </el-dialog>
          <el-button type="primary" size="mini" @click="showDetail(scope.row)">查看</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="销量" prop="sales" />

      <el-table-column align="center" label="是否在售" prop="status">
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.status == 1 ? 'success' : 'info' "
          >{{ scope.row.status == 1 ? '在售' : '未售' }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        max-width="300"
        min-width="300"
        label="描述"
        prop="description"
      />

      <el-table-column align="center" label="操作" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['product:product:edit']" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-permission="['product:product:edit']" v-if=" scope.row.status == 1" type="warning" size="mini" @click="freezeOrActivationBtn(scope.row,'freeze')">下架</el-button>
          <el-button v-permission="['product:product:edit']" v-else type="success" size="mini" @click="freezeOrActivationBtn(scope.row,'activation')">上架</el-button>
          <el-button v-permission="['product:product:delete']" type="danger" style="margin:5px" size="mini" @click="handleDelete(scope.row)">删除</el-button>
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
import { listProduct, deleteProduct, detailProduct, freezeOrActivtion, batchDeleteProduct } from '@/api/product'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'ProductList',
  components: { BackToTop, Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      goodsStatusMap: [{ value: 0, name: '下架' }, { value: 1, name: '正常' }],
      listQuery: {
        page: 1,
        limit: 20,
        title: undefined,
        status: undefined
      },
      goodsDetail: '',
      detailDialogVisible: false,
      selectedIds: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listProduct(this.listQuery)
        .then(response => {
          this.list = response.data.data.items
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$router.push({ path: '/product/upsert' })
    },
    handleUpdate(row) {
      this.$router.push({ path: '/product/upsert', query: { id: row.id }})
    },
    showDetail(row) {
      if (!this.detailDialogVisible) {
        detailProduct(row.id)
          .then(response => {
            this.goodsDetail = response.data.data.detail.replace(/<img/g, "<img style='max-width:100%;height:auto;line-height:0px'")
            this.detailDialogVisible = true
          })
      }
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除商品--' + row.title + '--, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProduct(row.id)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除成功'
            })
            const index = this.list.indexOf(row)
            this.list.splice(index, 1)
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
    },
    freezeOrActivationBtn(row, title) {
      var status = 0
      var sign = ''
      if (title === 'activation') {
        status = 1
        sign = '激活'
      } else if (title === 'freeze') {
        status = 0
        sign = '冻结'
      } else {
        return false
      }

      this.$confirm('此操作将' + sign + '商品--' + row.title + '--, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).catch(() => {
        return false
      }).then(() => {
        freezeOrActivtion(row.id, status).then(response => {
          this.$notify.success({
            title: '成功',
            message: sign + '成功'
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1, response.data.data)
        }).catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
      })
    },
    handleSelectionChange(e) {
      const temp = []
      e.forEach(item => {
        temp.push(item.id)
      })
      this.selectedIds = temp
    },
    handleBatchDelete(e) {
      // 执行批量删除
      this.$confirm('此操作将永久删除选中的' + this.selectedIds.length + '件商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteProduct(this.selectedIds)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除成功'
            })
            this.getList()
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
