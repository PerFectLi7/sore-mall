<template>
  <div class="app-container">
    <el-card class="box-card">
      <h3>商品修改</h3>
      <el-form ref="dataForm" :rules="rules" :model="product" label-width="150px">
        <el-form-item label="商品名称" prop="title">
          <el-input v-model="product.title" />
        </el-form-item>
        <el-form-item label="等价积分" prop="points">
          <el-input v-model="product.points">
            <template slot="append">点</template>
          </el-input>
        </el-form-item>
        <el-form-item label="剩余库存" prop="stock">
          <el-input v-model="product.stock" :disabled="disabled"/>
        </el-form-item>

        <el-form-item label="是否在售" prop="status">
          <el-radio-group v-model="product.status">
            <el-radio :label="1">在售</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="商品图片">
          <el-upload
            :action="uploadPath"
            :headers="headers"
            :limit="5"
            :file-list="imgsFileList"
            :on-exceed="uploadOverrun"
            :on-success="handleimgsUrl"
            :on-remove="handleRemove"
            multiple
            accept=".jpg, .jpeg, .png, .gif"
            list-type="picture-card"
          >
            <i class="el-icon-plus" />
          </el-upload>
        </el-form-item>

        <el-form-item label="商品简介" prop="description">
          <el-input v-model="product.description" />
        </el-form-item>

        <el-form-item label="商品详细介绍" prop="detail">
          <editor :init="editorInit" v-model="product.detail" />
        </el-form-item>

      </el-form>
    </el-card>

    <div class="op-container">
      <el-button @click="handleCancel">取消</el-button>
      <el-button v-if="!product.id" type="primary" @click="handleCreate">保存商品</el-button>
      <el-button v-if="product.id" type="primary" @click="handleEdit">更新商品</el-button>
    </div>
  </div>
</template>

<script>
import { detailProduct, editProduct, createProduct } from '@/api/product'
import { uploadPath, createStorage } from '@/api/storage'
import Editor from '@tinymce/tinymce-vue'
import { MessageBox } from 'element-ui'
import { getToken } from '@/utils/auth'

export default {
  name: 'ProductUpsert',
  components: { Editor },
  disabled: false,
  data() {
    return {
      uploadPath,
      imgsFileList: [],
      product: {
        imgList: [],
        type: 0
      },
      disabled: false,
      rules: {
        status: [
          { required: true, message: '请选择商品状态', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '商品名称不能为空', trigger: 'blur' }
        ],
        points: [
          { required: true, message: '商品积分不能为空', trigger: 'blur' }
        ],
        detail: [{ required: true, message: '请填写商品详情', trigger: 'blur' }]
      },
      editorInit: {
        language: 'zh_CN',
        convert_urls: false,
        plugins: ['advlist anchor autolink autosave code codesample colorpicker colorpicker contextmenu directionality emoticons fullscreen hr image imagetools importcss insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars wordcount'],
        toolbar: ['searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample', 'hr bullist numlist link image charmap preview anchor pagebreak insertdatetime media table emoticons forecolor backcolor fullscreen'],
        images_upload_handler: function(blobInfo, success, failure) {
          const formData = new FormData()
          formData.append('file', blobInfo.blob())
          createStorage(formData).then(res => {
            success(res.data.url)
          }).catch(() => {
            failure('上传失败，请重新上传')
          })
        }
      }

    }
  },
  computed: {
    headers() {
      return {
        ADMINTOKEN: getToken()
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init: function() {
      const productId = this.$route.query.id
      if (productId) {
        this.disabled = true
        detailProduct(productId).then(response => {
          // 深拷贝
          this.product = JSON.parse(JSON.stringify(response.data.data))
          this.imgsFileList = []
          for (var i = 0; i < this.product.imgList.length; i++) {
            this.imgsFileList.push({
              url: this.product.imgList[i]
            })
          }
        })
      }
    },
    handleCancel: function() {
      this.$router.push({ path: '/product/list' })
    },
    handleCreate: function() {
      this.handleUpsert('create')
    },
    handleEdit: function() {
      this.handleUpsert('edit')
    },
    /**
     * 执行更新添加操作，传入操作action字符串: 'create', 'edit'
     */
    handleUpsert(action) {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          if (this.product.imgList.length === 0) {
            this.$notify.error({
              title: '失败',
              message: '请上传至少一张图片'
            })
          } else {
            const finalProduct = {
              ...this.product
            }
            let method = createProduct
            let successWords = '创建成功'
            if (action === 'edit') {
              method = editProduct
              successWords = '编辑成功'
            }
            method(finalProduct)
              .then(response => {
                this.$notify.success({
                  title: '成功',
                  message: successWords
                })
                this.$router.push({ path: '/product/list' })
              })
              .catch(response => {
                MessageBox.alert('业务错误：' + response.data.errmsg, '警告', {
                  confirmButtonText: '确定',
                  type: 'error'
                })
              })
          }
        } else {
          this.$notify.error({
            title: '失败',
            message: '请完善以上表单'
          })
        }
      })
    },
    /** ******************* 图片上传相关 *********************/
    uploadOverrun: function() {
      this.$message({
        type: 'error',
        message: '上传文件个数超出限制!最多上传5张图片!'
      })
    },
    handleimgsUrl(response, file, fileList) {
      if (response.errno === 200) {
        this.product.imgList.push(response.url)
        this.product.img = this.product.imgList[0]
      }
    },
    handleRemove: function(file, fileList) {
      for (var i = 0; i < this.product.imgList.length; i++) {
        // 这里存在两种情况
        // 1. 如果所删除图片是刚刚上传的图片，那么图片地址是file.response.url
        //    此时的file.url虽然存在，但是是本机地址，而不是远程地址。
        // 2. 如果所删除图片是后台返回的已有图片，那么图片地址是file.url
        var url
        if (file.response === undefined) {
          url = file.url
        } else {
          url = file.response.url
        }

        if (this.product.imgList[i] === url) {
          this.product.imgList.splice(i, 1)
        }
      }
      if (this.product.imgList.length > 0) {
        this.product.img = this.product.imgList[0]
      }
    }
  }
}
</script>

<style>
.el-card {
  margin-bottom: 10px;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.input-new-keyword {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}

.avatar {
  width: 145px;
  height: 145px;
  display: block;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
