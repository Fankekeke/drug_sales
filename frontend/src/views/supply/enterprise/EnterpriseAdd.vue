<template>
  <a-modal v-model="show" title="添加供应商" @cancel="onClose" :width="800">
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
        <a-col :span="8">
          <a-form-item label='功能供应商名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='单位简称或代号' v-bind="formItemLayout">
            <a-input v-decorator="[
            'abbreviation',
            { rules: [{ required: true, message: '请输入单位简称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='统一社会信用代码' v-bind="formItemLayout">
            <a-input v-decorator="[
            'creditCode'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='单位性质' v-bind="formItemLayout">
            <a-input v-decorator="[
            'nature'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='二级供应商单位性质' v-bind="formItemLayout">
            <a-input v-decorator="[
            'natureTwo'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='经营状态' v-bind="formItemLayout">
            <a-input v-decorator="[
            'status'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='法定代表人' v-bind="formItemLayout">
            <a-input v-decorator="[
            'corporateRepresentative'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='法定代表人身份证号' v-bind="formItemLayout">
            <a-input v-decorator="[
            'corporateRepresentativeId'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='法定代表人电话' v-bind="formItemLayout">
            <a-input v-decorator="[
            'corporateRepresentativePhone'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='注册资本（万元）' v-bind="formItemLayout">
            <a-input v-decorator="[
            'registeredCapital'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='注册资金币种' v-bind="formItemLayout">
            <a-input v-decorator="[
            'registeredCapitalCurrency'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='成立日期' v-bind="formItemLayout">
            <a-input v-decorator="[
            'establishmentDate'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='营业期限始期' v-bind="formItemLayout">
            <a-input v-decorator="[
            'businessBeginDate'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='营业期限止期' v-bind="formItemLayout">
            <a-input v-decorator="[
            'businessEndDate'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='注册地址' v-bind="formItemLayout">
            <a-input v-decorator="[
            'registeredAddress'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='经营范围' v-bind="formItemLayout">
            <a-input v-decorator="[
            'businessScope'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='省' v-bind="formItemLayout">
            <a-input v-decorator="[
            'province'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='市' v-bind="formItemLayout">
            <a-input v-decorator="[
            'city'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='区' v-bind="formItemLayout">
            <a-input v-decorator="[
            'district'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='英文供应商名称' v-bind="formItemLayout">
            <a-input v-decorator="[
            'enName'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label='所属行业' v-bind="formItemLayout">
            <a-input v-decorator="[
            'industry'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='单位简介' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'unitDescription'
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='供应商照片' v-bind="formItemLayout">
            <a-upload
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 1">
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

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'EnterpriseAdd',
  props: {
    enterpriseVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.enterpriseVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      formItemLayout
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
          this.$post('/cos/enterprise-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    handleChange ({file}) {
      if (file.response !== undefined) {
        console.log(file.response.code)
        console.log(file.status)
        if (file.response.code === 500 && file.status === 'done') {
          this.$message.error(file.response.msg)
        } else if (file.response.code === 0 && file.status === 'done') {
          this.$emit('success')
        }
      }
    },
    download () {
      window.location.href = 'http://127.0.0.1:9527/cos/enterprise-info/template'
    }
  }
}
</script>

<style scoped>

</style>
