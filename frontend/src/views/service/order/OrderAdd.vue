<template>
  <a-modal v-model="show" title="新增公告" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-col :span="24">
          <a-form-item label='售后内容' v-bind="formItemLayout">
            <a-textarea :rows="5" v-decorator="[
            'remark',
             { rules: [{ required: true, message: '请输入售后内容!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        </a-col>
        <a-col :span="12">
          <a-form-item label='服务类型' v-bind="formItemLayout">
            <a-select v-decorator="[
              'serverType',
              { rules: [{ required: true, message: '请输入服务类型!' }] }
              ]">
              <a-select-option v-for="(item, index) in serviceSortList" :value="item.id" :key="index">{{ item.name }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
         <a-col :span="12">
          <a-form-item label='售后产品' v-bind="formItemLayout">
            <a-select v-decorator="[
              'productId',
              { rules: [{ required: true, message: '请输入服务类型!' }] }
              ]">
              <a-select-option v-for="(item, index) in productList" :value="item.value" :key="index">{{ item.label }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='图册' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 8">
                <a-icon type="plus" />
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'OrderAdd',
  props: {
    orderAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      serviceSortList: [],
      productList: []
    }
  },
  mounted () {
    this.getServiceSort()
    this.getProduct()
  },
  methods: {
    getServiceSort () {
      this.$get(`/cos/service-sort/list`).then((r) => {
        this.serviceSortList = r.data.data
      })
    },
    getProduct () {
      this.$get(`/cos/product-info/list`).then((r) => {
        this.productList = r.data.data
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          values.userId = this.currentUser.userId
          this.loading = true
          this.$post('/cos/order-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
