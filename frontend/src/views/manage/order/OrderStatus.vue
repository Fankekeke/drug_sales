<template>
  <a-modal v-model="show" title="工单办理" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="submit(4)" type="primary">
        维修完成
      </a-button>
      <a-button key="reject" @click="submit(5)" type="danger">
        已退换
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="orderStatusData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>工单编号：</b>
          {{ orderStatusData.orderCode }}
        </a-col>
        <a-col :span="8"><b>客户名称：</b>
          {{ orderStatusData.userName }}
        </a-col>
        <a-col :span="8"><b>联系方式：</b>
          {{ orderStatusData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
       <a-col :span="8"><b>工单名称：</b>
          {{ orderStatusData.orderName }}
        </a-col>
        <a-col :span="8"><b>服务费用：</b>
          {{ orderStatusData.money }}元
        </a-col>
        <a-col :span="8"><b>备注：</b>
          {{ orderStatusData.remark }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>当前状态：</b>
          <span v-if="orderStatusData.status == 0">正在对应</span>
          <span v-if="orderStatusData.status == 1">已派发</span>
          <span v-if="orderStatusData.status == 2">缴费</span>
          <span v-if="orderStatusData.status == 3">正在维修</span>
          <span v-if="orderStatusData.status == 4">维修完成</span>
          <span v-if="orderStatusData.status == 5">已退换</span>
          <span v-if="orderStatusData.status == 6">完成</span>
        </a-col>
        <a-col :span="8"><b>服务类型：</b>
          {{ orderStatusData.serverTypeName }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ orderStatusData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">备注信息</span></a-col>
        <a-col :span="24">
          {{ orderStatusData.remark }}
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
  name: 'orderStatus',
  props: {
    orderStatusShow: {
      type: Boolean,
      default: false
    },
    orderStatusData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.orderStatusShow
      },
      set: function () {
      }
    }
  },
  watch: {
    'orderStatusShow': function (value) {
      if (value) {
        this.selectStaffByProduct(this.orderStatusData.productId)
        if (this.orderStatusData.images !== null && this.orderStatusData.images !== '') {
          this.imagesInit(this.orderStatusData.images)
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
    submit (status) {
      this.$get(`/cos/order-info/set/status`, {
        orderCode: this.orderStatusData.orderCode,
        status
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
