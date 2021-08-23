<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <!--<el-input v-model="listQuery.userId" clearable class="filter-item" style="width: 200px;" placeholder="请输入用户ID"/>-->
      <el-input
        v-model="listQuery.orderNo"
        clearable
        class="filter-item"
        style="width: 200px;"
        placeholder="请输入订单编号"
      />
      <el-select
        v-model="listQuery.status"
        style="width: 200px"
        class="filter-item"
        placeholder="请选择订单状态"
      >
        <el-option v-for="(key, value) in statusMap" :key="key" :label="key" :value="value" />
      </el-select>
      <el-button
        v-permission="['operation:order:list']"
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >查找</el-button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <br>
      <el-date-picker
        v-model="downData.gmtStart"
        type="datetime"
        style="width: 200px"
        class="filter-item"
        default-time="00:00:00"
        placeholder="选择开始日期"
      />
      至
      <el-date-picker
        v-model="downData.gmtEnd"
        type="datetime"
        style="width: 200px"
        class="filter-item"
        default-time="00:00:00"
        placeholder="选择结束日期"
      />
      <el-select
        v-model="downData.status"
        style="width: 200px"
        class="filter-item"
        placeholder="待出库"
      >
        <el-option v-for="(key, value) in statusMap" :key="key" :label="key" :value="value" />
      </el-select>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="downExcelBtn">导出</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="downExcelStatisticsBtn">汇总</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      :cell-class-name="tableCellClassName"
      size="small"
      element-loading-text="正在查询中。。。"
      border
      fit
      highlight-current-row
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="right" class="table-expand">
            <el-form-item label="联系人名">
              <span>{{ props.row.consignee }}</span>
            </el-form-item>
          </el-form>
          <el-form label-position="right" class="table-expand">
            <el-form-item label="联系电话">
              <span>{{ props.row.phone }}</span>
            </el-form-item>
          </el-form>
          <el-form label-position="right" class="table-expand">
            <el-form-item label="商品名称">
              <span>{{ props.row.productDO.title}}</span>
            </el-form-item>
          </el-form>
          <el-form label-position="right" class="table-expand">
            <el-form-item label="商品积分">
              <span>{{ props.row.productDO.points}}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" width="210" label="订单编号" prop="orderNo" />

      <el-table-column align="center" width="80" label="用户ID" prop="userId" />

      <el-table-column align="center" width="80" label="用户昵称" prop="userNick" />

      <el-table-column align="center" width="80" label="商品名称" prop="productName" />
      
      <el-table-column align="center" width="120" label="订单状态" prop="status">
        <template slot-scope="scope">
          <el-tag :type="statusColor(scope.row.status)">{{ scope.row.status | orderStatusFilter }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" width="100" label="订单积分" prop="actualPrice">
        <template slot-scope="scope"> {{ scope.row.points }}</template>
      </el-table-column>

      <!--<el-table-column align="center" label="支付金额" prop="actualPrice"/>-->

      <el-table-column align="center" width="140" label="创建时间" prop="gmtCreate">
        <template slot-scope="scope">{{ scope.row.gmtCreate | formatTime }}</template>
      </el-table-column>

      <el-table-column align="center" width="140" label="物流渠道" prop="shipCode">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status <= 20 || scope.row.status >= 80" type="info">未发货</el-tag>
          <el-tag v-else>{{ scope.row.shipCode | shipCodeFilter }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" width="140" label="发货时间" prop="gmtCreate">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status <= 20 || scope.row.status >= 80" type="info">未发货</el-tag>
          <el-tag v-else>{{ scope.row.gmtShip | formatTime }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" width="200" label="物流单号" prop="shipNo"/>

      <el-table-column align="center" label="操作" width="300" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-permission="['operation:order:detail']"
            type="primary"
            size="mini"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <el-button
            v-permission="['operation:order:detail']"
            v-if="scope.row.status===20"
            type="primary"
            size="mini"
            @click="downOrderExcelBtn(scope.row)"
          >配送单</el-button>
          <el-button
            v-permission="['operation:order:ship']"
            v-if="scope.row.status===20"
            type="primary"
            size="mini"
            @click="handleShip(scope.row)"
          >发货</el-button>
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

    <!-- 订单详情对话框 -->
    <el-dialog :visible.sync="orderDialogVisible" title="订单详情" width="800">
      <el-form :data="orderDetail" label-position="left">
        <el-form-item label="订单编号">
          <span>{{ orderDetail.orderNo }}</span>
        </el-form-item>
        <el-form-item label="用户Id">
          <span>{{ orderDetail.userId }}</span>
        </el-form-item>
        <el-form-item label="订单状态">
          <template>
            <el-tag>{{ orderDetail.status | orderStatusFilter }}</el-tag>
          </template>
        </el-form-item>
        <el-form-item label="收货信息">
          <span>（收货人）{{ orderDetail.consignee }}</span>
          <span>（手机号）{{ orderDetail.phone }}</span>
          <span>（地址）{{ orderDetail.province }}{{ orderDetail.city }}{{ orderDetail.county }}{{ orderDetail.address }}{{ orderDetail.address }}</span>
        </el-form-item>
        <el-form-item label="商品信息">
          <el-table :data="orderDetail.skuList" size="small" border fit highlight-current-row>
            <el-table-column align="center" label="名称" prop="spuTitle" />
            <el-table-column align="center" label="商品数量" prop="num" />
            <el-table-column align="center" label="单位" prop="unit" />
            <el-table-column align="center" label="总积分（单价*数量）">
              <template slot-scope="scope">
                <span>{{ scope.row.price * scope.row.num }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 发货对话框 -->
    <el-dialog :visible.sync="shipDialogVisible" title="发货">
      <el-form
        ref="shipForm"
        :model="shipForm"
        :rules="shipRules"
        status-icon
        label-position="left"
        label-width="100px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="快递公司" prop="shipCode">
          <el-select v-model="shipForm.shipCode" placeholder="请选择快递公司">
            <el-option v-for="(key, value) in shipCodeMap" :key="key" :label="key" :value="value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="快递编号" prop="shipNo">
          <el-input :disabled="shipForm.shipCode === 'NONE'" v-model="shipForm.shipNo" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="shipDialogVisible = false">取消</el-button>
        <el-button :disabled="shipSubmiting" type="primary" @click="confirmShip">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listOrder, shipOrder, refundOrder, detailOrder, getExcelInfo, editAdminMono, getExcelStatistics } from '@/api/order'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import checkPermission from '@/utils/permission' // 权限判断函数

const statusMap = {
  20: '待出库',
  30: '待收货',
  40: '已完成',
}

const shipCodeMap = {
  'NONE': '无需物流公司',
  'SF': '顺丰速运',
  'HTKY': '百世快递',
  'ZTO': '中通快递',
  'STO': '申通快递',
  'YTO': '圆通速递',
  'YD': '韵达速递',
  'YZPY': '邮政快递包裹',
  'EMS': 'EMS',
  'HHTT': '天天快递',
  'JD': '京东快递',
  'UC': '优速快递',
  'DBL': '德邦快递',
  'ZJS': '宅急送',
  'TNT': 'TNT快递'
}


export default {
  name: 'Order',
  components: { Pagination },
  filters: {
    orderStatusFilter(status) {
      return statusMap[status]
    },
    shipCodeFilter(shipCode) {
      if (shipCode) {
        return shipCodeMap[shipCode]
      }
      return '无需物流公司'
    }
  },
  data() {
    return {
      statusMap,
      shipCodeMap,
      excelData: {
        barcode: undefined,
        name: undefined,
        specifications: undefined,
        unit: undefined,
        num: undefined,
        address: undefined
      },
      excelDataList: [],
      downData: {
        status: '',
        gmtStart: undefined,
        gmtEnd: undefined
      },
      list: undefined,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        id: undefined,
        name: undefined
      },
      orderDialogVisible: false,
      orderDetail: {},
      refundForm: {
        orderNo: undefined,
        type: 0
      },
      refundDialogVisible: false,
      refundSubmiting: false,
      adminMonoDialogVisible: false,
      downloadLoading: false,
      shipForm: {
        orderNo: undefined,
        shipCode: 'NONE',
        shipNo: undefined
      },
      shipDialogVisible: false,
      shipSubmiting: false,
      shipRules: {
        shipCode: [
          { required: true, message: '请选择物流类型', trigger: 'blur' }
        ],
        orderNo: [
          { required: true, message: '请使用非IE浏览器重试', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    checkPermission,
    getList() {
      this.listLoading = true
      listOrder(this.listQuery)
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
    handleDetail(row) {
      detailOrder(row.id).then(response => {
        this.orderDetail = response.data.data
      })
      this.orderDialogVisible = true
    },
    handleShip(row) {
      this.shipDialogVisible = true
      this.shipForm.orderNo = row.orderNo
      this.shipForm.shipCode = ''
      this.shipForm.shipNo = ''
    },
    confirmShip() {
      this.$refs['shipForm'].validate(valid => {
        if (valid) {
          if (this.shipForm.shipCode !== 'NONE' && !this.shipForm.shipNo) {
            this.$notify.error({
              title: '失败',
              message: '请填写运单号'
            })
          } else {
            this.shipSubmiting = true
            shipOrder(this.shipForm)
              .then(response => {
                this.shipSubmiting = false
                this.shipDialogVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '确认发货成功！'
                })
                this.getList()
              })
              .catch(response => {
                this.shipSubmiting = false
                this.$notify.error({
                  title: '失败',
                  message: response.data.errmsg
                })
              })
          }
        }
      })
    },
    // 订单表格中下载
    downOrderExcelBtn(row) {
      detailOrder(row.id).then(response => {
        var temp = response.data.data
        const obj = {}
        obj.address = temp.province + temp.city + temp.county + temp.address
        obj.orderNo = temp.orderNo
        obj.phone = temp.phone
        obj.mono = temp.mono
        obj.adminMono = temp.adminMono
        obj.area = temp.province + temp.city + temp.county
        obj.consignee = temp.consignee
        for (var j = 0; j < temp.skuList.length; j++) {
          var sku = temp.skuList[j]
          obj.unit = sku.unit
          obj.num = sku.num
          obj.specifications = sku.title
          obj.barcode = sku.barCode
          obj.name = sku.spuTitle
          var copy = Object.assign({}, obj)
          this.excelDataList.push(copy)
        }
        this.handleDownload(this.excelDataList)
        this.excelDataList = []
      })
    },
    // 选择条件下载
    downExcelBtn() {
      this.downloadLoading = true
      const dataInfo = Object.assign({}, {
        status: this.downData.status
      })

      if (this.downData.gmtStart) {
        dataInfo['gmtStart'] = this.downData.gmtStart.getTime()
      }
      if (this.downData.gmtEnd) {
        dataInfo['gmtEnd'] = this.downData.gmtEnd.getTime()
      }
      getExcelInfo(dataInfo).then(response => {
        if (response.data.data == null) {
          this.$notify.error({
            title: '失败',
            message: '没有信息可以打印'
          })
        }

        var data = response.data.data
        for (var i = 0; i < data.length; i++) {
          var temp = data[i]
          const obj = {}
          obj.address = temp.province + temp.city + temp.county + temp.address
          obj.orderNo = temp.orderNo
          obj.phone = temp.phone
          obj.mono = temp.mono
          obj.adminMono = temp.adminMono
          obj.area = temp.province + temp.city + temp.county
          obj.consignee = temp.consignee
          for (var j = 0; j < temp.skuList.length; j++) {
            var sku = temp.skuList[j]
            obj.unit = sku.unit
            obj.num = sku.num
            obj.specifications = sku.title
            obj.barcode = sku.barCode
            obj.name = sku.spuTitle
            var copy = Object.assign({}, obj)
            this.excelDataList.push(copy)
          }
        }
        this.handleDownload(this.excelDataList)
        this.excelDataList = []
        this.downloadLoading = false
      })
        .catch(response => {
          this.downloadLoading = false
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    statusColor(status) {
      switch (status) {
        case 10:
        case 80:
        case 90:
          return 'info'
        case 30:
        case 40:
        case 50:
          return 'success'
        case 20:
          return 'warning'
        case 60:
        case 70:
          return 'danger'
      }
      return 'primary'
    },
    handleDownload(data) {
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = [
          '订单编号',
          '商品名称',
          '规格',
          '单位',
          '配送数量',
          '配送地址',
          '收货人',
          '联系方式',
          '区域划分',
          '客服备注',
          '用户备注'
        ]
        const filterVal = [
          'orderNo',
          'name',
          'specifications',
          'unit',
          'num',
          'address',
          'consignee',
          'phone',
          'area',
          'mono',
          'adminMono'
        ]
        excel.export_json_to_excel2(tHeader, data, filterVal, '订单信息')
      })
    },

    // 订单汇总
    downExcelStatisticsBtn() {
      const param = {
      }
      if (this.downData.gmtStart) {
        param['gmtStart'] = this.downData.gmtStart.getTime()
      }
      if (this.downData.gmtEnd) {
        param['gmtEnd'] = this.downData.gmtEnd.getTime()
      }
      getExcelStatistics(param).then(response => {
        if (response.data.data == null || response.data.data.length === 0) {
          this.$notify.error({
            title: '失败',
            message: '没有信息可以打印'
          })
        } else {
          this.handleStatisticsDownload(response.data.data)
        }
      })
        .catch(response => {
          this.downloadLoading = false
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },

    handleStatisticsDownload(data) {
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = [
          '商品名称',
          'spuId',
          '规格',
          'skuId',
          '数量'
        ]
        const filterVal = [
          'spuTitle',
          'spuId',
          'skuTitle',
          'skuId',
          'num'
        ]
        excel.export_json_to_excel2(tHeader, data, filterVal, '统计信息')
      })
    },

    tableCellClassName({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 1) {
        if (row.adminMonoLevel) {
          if (row.adminMonoLevel === 1) {
            return 'success-row'
          } else if (row.adminMonoLevel === 2) {
            return 'warning-row'
          } else if (row.adminMonoLevel === 3) {
            return 'danger-row'
          }
        }
        return ''
      }
    }
  }
}
</script>
<style>
  .el-table .danger-row {
    background: rgb(201, 143, 143);
  }

  .el-table .warning-row {
    background: rgb(197, 175, 142);
  }

  .el-table .success-row {
    background: rgb(166, 202, 149);
  }
</style>
