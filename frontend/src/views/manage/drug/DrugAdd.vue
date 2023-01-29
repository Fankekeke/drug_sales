<template>
  <a-modal v-model="show" title="新增药品" @cancel="onClose" :width="1000">
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
        <a-col :span="6">
          <a-form-item label='药品名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='品牌' v-bind="formItemLayout">
            <a-input v-decorator="[
            'brand',
            { rules: [{ required: true, message: '请输入品牌!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='所属分类' v-bind="formItemLayout">
            <a-select v-decorator="[
              'category',
              { rules: [{ required: true, message: '请输入所属分类!' }] }
              ]">
              <a-select-option value="1">可卡因</a-select-option>
              <a-select-option value="2">维生素制剂</a-select-option>
              <a-select-option value="3">鱼肝油</a-select-option>
              <a-select-option value="4">药物饮料</a-select-option>
              <a-select-option value="5">膳食纤维</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='药品类别' v-bind="formItemLayout">
            <a-select v-decorator="[
              'classification',
              { rules: [{ required: true, message: '请输入药品类别!' }] }
              ]">
              <a-select-option value="1">中药材</a-select-option>
              <a-select-option value="2">中药饮片</a-select-option>
              <a-select-option value="3">中西成药</a-select-option>
              <a-select-option value="4">化学原料药</a-select-option>
              <a-select-option value="5">抗生素</a-select-option>
              <a-select-option value="6">生化药品</a-select-option>
              <a-select-option value="7">放射性药品</a-select-option>
              <a-select-option value="8">血清</a-select-option>
              <a-select-option value="9">诊断药品</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='通用名' v-bind="formItemLayout">
            <a-input v-decorator="[
            'commonName'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="6">
          <a-form-item label='剂型' v-bind="formItemLayout">
            <a-input v-decorator="[
            'dosageForm'
            ]"/>
          </a-form-item>
        </a-col>
         <a-col :span="6">
          <a-form-item label='用法' v-bind="formItemLayout">
            <a-input v-decorator="[
            'usages'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='适用症状' v-bind="formItemLayout">
            <a-input v-decorator="[
            'applicableSymptoms'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='适用疾病' v-bind="formItemLayout">
            <a-input v-decorator="[
            'applicableDisease'
            ]"/>
          </a-form-item
          >
        </a-col>
        <a-col :span="6">
          <a-form-item label='包装清单' v-bind="formItemLayout">
            <a-input v-decorator="[
            'packingList'
            ]"/>
          </a-form-item
          >
        </a-col>
        <a-col :span="6">
          <a-form-item label='使用剂量' v-bind="formItemLayout">
            <a-input v-decorator="[
            'dosageUse'
            ]"/>
          </a-form-item
          >
        </a-col>
        <a-col :span="6">
          <a-form-item label='有效期' v-bind="formItemLayout">
            <a-input-number style="width: 100%" :min="1" :step="1" v-decorator="[
            'validityPeriod'
            ]"/>
          </a-form-item
          >
        </a-col>
        <a-col :span="6">
          <a-form-item label='批准文号' v-bind="formItemLayout">
            <a-input v-decorator="[
            'approvalNumber'
            ]"/>
          </a-form-item
          >
        </a-col>
        <a-col :span="6">
          <a-form-item label='生产企业' v-bind="formItemLayout">
            <a-input v-decorator="[
            'manufacturer'
            ]"/>
          </a-form-item
          >
        </a-col>
        <a-col :span="6">
          <a-form-item label='单价' v-bind="formItemLayout">
            <a-input-number style="width: 100%" :min="1" :step="0.1" v-decorator="[
            'unitPrice'
            ]"/>
          </a-form-item
          >
        </a-col>
        <a-col :span="24">
          <a-form-item label='药品图片' v-bind="formItemLayout">
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
  name: 'drugAdd',
  props: {
    drugAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.drugAddVisiable
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
      previewImage: ''
    }
  },
  methods: {
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
          this.loading = true
          this.$post('/cos/drug-info', {
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
