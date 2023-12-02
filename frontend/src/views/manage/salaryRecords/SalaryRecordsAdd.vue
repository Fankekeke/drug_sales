<template>
  <a-modal v-model="show" @cancel="onClose" :width="900">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical" style="font-size: 13px;font-family: SimHei">
      <a-row :gutter="15">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">选择员工</span></a-col>
        <a-col :span="10">
          <a-form-item v-bind="formItemLayout">
            <a-select style="width: 100%" v-model="staffCode" @change="staffChange" option-label-prop="label">
              <a-select-option v-for="(item, index) in staffList" :key="index" :value="item.staffCode" :label="item.staffName">
                <a-row>
                  <a-col :span="4">
                    <a-avatar style="margin-right: 20px" shape="square" :size="40" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + item.avatar" />
                  </a-col>
                  <a-col :span="20">
                    <a-row>
                      <a-col><span>{{item.staffName}}</span></a-col>
                      <a-col style="font-size: 10px">
                        <span v-if="item.staffType === 1">售货员</span>
                        <span v-if="item.staffType === 2">理货员</span>
                        <span v-if="item.staffType === 3">收银员</span>
                        <span v-if="item.staffType === 4">分拣员</span>
                        <span v-if="item.staffType === 5">杂工</span>
                      </a-col>
                    </a-row>
                  </a-col>
                </a-row>
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-row v-if="staffData != null">
        <a-col :span="8"><b>员工姓名：</b>
          <a-popover>
            <template slot="content">
              <a-avatar v-if="staffData.avatar !== null" shape="square" :size="132" icon="user" :src="'http://127.0.0.1:9527/imagesWeb/' + staffData.avatar" />
              <a-avatar v-else shape="square" :size="132" icon="user" />
            </template>
            <a>{{ staffData.staffName !== null ? staffData.staffName : '- -' }}</a>
          </a-popover>
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          <a-tooltip>
            <template slot="title">
              {{ staffData.email }}
            </template>
            {{ staffData.email.slice(0, 8) }} ...
          </a-tooltip>
        </a-col>
        <a-col :span="8"><b>性别：</b>
          <span v-if="staffData.staffSex === 1">男</span>
          <span v-if="staffData.staffSex === 2">女</span>
        </a-col>
      </a-row>
      <br/>
      <a-row v-if="staffData != null">
        <a-col :span="8"><b>员工职务：</b>
          <span v-if="staffData.staffType === 1">售货员</span>
          <span v-if="staffData.staffType === 2">理货员</span>
          <span v-if="staffData.staffType === 3">收银员</span>
          <span v-if="staffData.staffType === 4">分拣员</span>
          <span v-if="staffData.staffType === 5">杂工</span>
        </a-col>
        <a-col :span="8"><b>出生日期：</b>
          {{ staffData.birthDate }}
        </a-col>
        <a-col :span="24"></a-col>
      </a-row>
      <br/>
      <a-row :gutter="20">
        <a-col :span="8">
          <a-form-item label="基本工资">
            <a-input-number style="width: 100%" v-decorator="[
              'basicWage', { rules: [{ required: true, message: '请填写基本工资!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="岗位津贴">
            <a-input-number style="width: 100%" v-decorator="[
              'postAllowance', { rules: [{ required: true, message: '请填写岗位津贴!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="绩效奖金">
            <a-input-number style="width: 100%" v-decorator="[
              'performanceBonus', { rules: [{ required: true, message: '请填写绩效奖金!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="加班费">
            <a-input-number style="width: 100%" v-decorator="[
              'overtimePay', { rules: [{ required: true, message: '请填写加班费!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="过节费">
            <a-input-number style="width: 100%" v-decorator="[
              'holidayCosts', { rules: [{ required: true, message: '请填写过节费!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="养老保险">
            <a-input-number style="width: 100%" v-decorator="[
              'pension', { rules: [{ required: true, message: '请填写养老保险!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="失业保险">
            <a-input-number style="width: 100%" v-decorator="[
              'unemployment', { rules: [{ required: true, message: '请填写失业保险!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="医疗保险">
            <a-input-number style="width: 100%" v-decorator="[
              'medicalInsurance', { rules: [{ required: true, message: '请填写医疗保险!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="税金">
            <a-input-number style="width: 100%" v-decorator="[
              'tax', { rules: [{ required: true, message: '请填写税金!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="8">
          <a-form-item label="住房公积金">
            <a-input-number style="width: 100%" v-decorator="[
              'housingFund', { rules: [{ required: true, message: '请填写住房公积金!' }] }
              ]"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24" style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备注</span></a-col>
        <a-col :span="24">
          <a-form-item>
            <a-textarea placeholder="Basic usage" :rows="4" v-decorator="['remark']"/>
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
  name: 'salaryRecordsAdd',
  props: {
    salaryRecordsAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.salaryRecordsAddVisiable
      },
      set: function () {
      }
    }
  },
  watch: {
    staffData: function (val) {
      if (val) {
        this.getGainByStaffCode(this.staffData.staffCode)
      }
    }
  },
  mounted () {
    this.getStaffList()
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      staffList: [],
      staffData: null,
      gain: 0,
      staffCode: '',
      payroll: 0
    }
  },
  methods: {
    staffChange (value) {
      this.staffList.forEach(item => {
        if (item.staffCode === value) {
          this.staffData = item
        }
      })
    },
    getGainByStaffCode (staffCode) {
      this.$get(`/cos/salary-gain/gain/${staffCode}`).then((r) => {
        this.gain = r.data.data
        this.form.setFieldsValue({'basicWage': this.gain})
      })
    },
    getStaffList () {
      this.$get('/cos/staff-info/list').then((r) => {
        this.staffList = r.data.data
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
      if (this.staffData == null) {
        this.$message.error('请选择员工！')
        return false
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          values.staffCode = this.staffCode
          this.$post('/cos/salary-records', {
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
