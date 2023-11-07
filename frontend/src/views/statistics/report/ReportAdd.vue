<template>
  <a-modal v-model="show" title="新增报表" @cancel="onClose" :width="800">
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
          <a-form-item label='报表标题' v-bind="formItemLayout">
            <a-input v-decorator="[
            'title',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='药店'>
            <a-select @change="pharmacyCheck" v-decorator="[
              'pharmacyId',
              ]">
              <a-select-option :value="item.id" v-for="(item, index) in pharmacyList" :key="index">{{
                  item.name
                }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='药品'>
            <a-select style="width: 100%" v-decorator="[
              'drugId',
              ]">
              <a-select-option v-for="(item, index) in drugList" :key="index" :value="item.drugId">{{
                  item.drugName
                }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='用户'>
            <a-select style="width: 100%" v-decorator="[
              'userId',
              ]">
              <a-select-option v-for="(item, index) in userList" :key="index" :value="item.id">{{
                  item.name
                }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='统计日期'>
            <a-range-picker @change="onChange" style="width: 100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-decorator="[
            'remark',
             { rules: [{ required: true, message: '请输入内容!' }] }
            ]"/>
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
  name: 'reportAdd',
  props: {
    reportAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.reportAddVisiable
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
      pharmacyInfo: null,
      drugList: [],
      pharmacyList: [],
      userList: [],
      dateList: []
    }
  },
  mounted () {
    this.getUser()
    this.getPharmacy()
  },
  methods: {
    onChange (date, dateString) {
      this.dateList = dateString
    },
    pharmacyCheck (value) {
      if (value) {
        this.pharmacyList.forEach(e => {
          if (e.id === value) {
            this.getDrug(e.id)
            this.pharmacyInfo = e
          }
        })
      }
    },
    getDrug (pharmacyId) {
      this.$get(`/cos/pharmacy-inventory/detail/pharmacy/${pharmacyId}`).then((r) => {
        this.drugList = r.data.data
      })
    },
    getPharmacy () {
      this.$get('/cos/pharmacy-info/list').then((r) => {
        this.pharmacyList = r.data.data
      })
    },
    getUser () {
      this.$get('/cos/user-info/list').then((r) => {
        this.userList = r.data.data
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
      this.dateList = []
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          if (this.dateList.length !== 0) {
            values.startDate = this.dateList[0]
            values.endDate = this.dateList[1]
          }
          this.loading = true
          this.$post('/cos/report-info', {
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
