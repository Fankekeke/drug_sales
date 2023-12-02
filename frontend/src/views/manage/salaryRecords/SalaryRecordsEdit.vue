<template>
  <a-modal v-model="show" @cancel="onClose" :width="900">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical" style="font-size: 13px;font-family: SimHei">
      <a-row :gutter="15">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">选择员工</span></a-col>
        <a-col :span="10">
          <a-form-item v-bind="formItemLayout" v-if="staffData != null">
            <a-input v-model="staffData.staffName" disabled/>
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
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'salaryRecordsEdit',
  props: {
    salaryRecordsEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.salaryRecordsEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      staffList: [],
      staffData: null,
      staffCode: '',
      gain: 0
    }
  },
  methods: {
    getStaffInfo (staffCode) {
      this.$get(`/cos/staff-info/${staffCode}`).then((r) => {
        this.staffData = r.data.data
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    setFormValues ({...salaryRecords}) {
      this.rowId = salaryRecords.id
      this.getStaffInfo(salaryRecords.staffCode)
      let fields = ['basicWage', 'postAllowance', 'performanceBonus', 'overtimePay', 'holidayCosts', 'pension', 'unemployment', 'medicalInsurance', 'tax', 'housingFund', 'payroll', 'remark']
      let obj = {}
      Object.keys(salaryRecords).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = salaryRecords[key]
        }
      })
      this.form.setFieldsValue(obj)
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
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        if (!err) {
          this.loading = true
          this.$put('/cos/salary-records', {
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
