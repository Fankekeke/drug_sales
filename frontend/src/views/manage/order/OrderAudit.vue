<template>
  <a-modal v-model="show" title="工单办理" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="submit" type="primary">
        确认
      </a-button>
      <a-button key="reject" @click="orderReject" type="danger">
        驳回
      </a-button>
      <a-button @click="onClose">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderAuditData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>工单编号：</b>
          {{ orderAuditData.orderCode }}
        </a-col>
        <a-col :span="8"><b>客户名称：</b>
          {{ orderAuditData.userName }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ orderAuditData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="orderAuditData.status == 0">正在对应</span>
          <span v-if="orderAuditData.status == 1">已派发</span>
          <span v-if="orderAuditData.status == 2">缴费</span>
          <span v-if="orderAuditData.status == 3">正在维修</span>
          <span v-if="orderAuditData.status == 4">维修完成</span>
          <span v-if="orderAuditData.status == 5">已退换</span>
          <span v-if="orderAuditData.status == 6">完成</span>
        </a-col>
        <a-col :span="8"><b>服务类型：</b>
          {{ orderAuditData.serverTypeName }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ orderAuditData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备注信息</span></a-col>
        <a-col :span="24">
          {{ orderAuditData.remark }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">工单图片</span></a-col>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <a-divider orientation="left">
        <span style="font-size: 12px;font-family: SimHei">工单分配</span>
      </a-divider>
      <a-row style="padding-left: 24px;padding-right: 24px;" :gutter="50">
        <a-col :span="24" style="margin-bottom: 15px">
          <a-radio-group v-model="auditData.staffId" button-style="solid">
            <a-radio-button v-for="(item, index) in staffList" :key="index" :value="item.id">
              {{ item.name }}（{{ item.workStatus > 0 ? '工作' : '空闲' }}）
            </a-radio-button>
          </a-radio-group>
        </a-col>
        <br/>
        <a-col :span="12">
          <a-form-item label='工单名称' v-bind="formItemLayout">
            <a-input v-model="auditData.orderName"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='收费价格' v-bind="formItemLayout">
            <a-input-number v-model="auditData.price" :min="0" :max="99999" style="width: 100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='对应时间' v-bind="formItemLayout">
            <a-date-picker :default-value="moment(new Date(), 'YYYY-MM-DD')" @change="onDateChange" style="width: 100%"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='维修备注' v-bind="formItemLayout">
            <a-textarea :rows="6" v-model="auditData.remark"/>
          </a-form-item>
        </a-col>
      </a-row>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
moment.locale('zh-cn')
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'OrderAudit',
  props: {
    orderAuditShow: {
      type: Boolean,
      default: false
    },
    orderAuditData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderAuditShow
      },
      set: function () {
      }
    }
  },
  watch: {
    'orderAuditShow': function (value) {
      if (value) {
        this.selectStaffByProduct(this.orderAuditData.productId)
        if (this.orderAuditData.images !== null && this.orderAuditData.images !== '') {
          this.imagesInit(this.orderAuditData.images)
        }
      }
    }
  },
  data () {
    return {
      formItemLayout,
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      auditData: {
        orderName: '',
        staffId: null,
        price: 0,
        reserveDate: '',
        remark: ''
      },
      staffList: []
    }
  },
  methods: {
    moment,
    selectStaffByProduct (productId) {
      this.$get(`/cos/staff-info/work/${productId}`).then((r) => {
        this.staffList = r.data.data
      })
    },
    onDateChange (date) {
      this.auditData.reserveDate = moment(date).format('YYYY-MM-DD')
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    orderReject () {
      this.$get(`/cos/order-info/reject/${this.orderAuditData.orderCode}`).then((r) => {
        this.$emit('success')
      })
    },
    submit () {
      if (this.auditData.staffId === null) {
        this.$message.error('请选择维修人员')
        return false
      }
      if (this.auditData.orderName === '') {
        this.$message.error('请输入工单名称')
        return false
      }
      if (this.auditData.reserveDate === '') {
        this.auditData.reserveDate = moment(new Date()).format('YYYY-MM-DD')
      }
      this.$get(`/cos/order-info/distribute`, {
        'orderName': this.auditData.orderName,
        'orderCode': this.orderAuditData.orderCode,
        'staffId': this.auditData.staffId,
        'date': this.auditData.reserveDate,
        'money': this.auditData.price,
        'remark': this.auditData.remark
      }).then((r) => {
        this.cleanData()
        this.$emit('success')
      })
    },
    onClose () {
      this.cleanData()
      this.$emit('close')
    },
    cleanData () {
      this.auditData.orderName = ''
      this.auditData.staffId = null
      this.auditData.price = 0
      this.auditData.reserveDate = ''
      this.auditData.remark = ''
    }
  }
}
</script>

<style scoped>

</style>
