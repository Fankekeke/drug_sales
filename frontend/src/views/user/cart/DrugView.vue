<template>
  <a-drawer
    title="药品详情"
    :maskClosable="false"
    width=1250
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="show"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <div style="font-size: 13px;font-family: SimHei" v-if="drugInfo !== null">
      <a-card :bordered="false" style="height: 400px">
        <div id="areas" style="width: 100%;height: 350px;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
      </a-card>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">药品信息</span></a-col>
        <a-col :span="8"><b>药品名称：</b>
          {{ drugInfo.name }}
        </a-col>
        <a-col :span="8"><b>药品编号：</b>
          {{ drugInfo.code }}
        </a-col>
        <a-col :span="8"><b>所属品牌：</b>
          {{ drugInfo.brand }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>所属分类：</b>
          <span v-if="drugInfo.category == 1">可卡因</span>
          <span v-if="drugInfo.category == 2">维生素制剂</span>
          <span v-if="drugInfo.category == 3">鱼肝油</span>
          <span v-if="drugInfo.category == 4">药物饮料</span>
          <span v-if="drugInfo.category == 5">膳食纤维</span>
        </a-col>
        <a-col :span="8"><b>药品类别：</b>
          <span v-if="drugInfo.classification == 1">中药材</span>
          <span v-if="drugInfo.classification == 2">中药饮片</span>
          <span v-if="drugInfo.classification == 3">中西成药</span>
          <span v-if="drugInfo.classification == 4">化学原料药</span>
          <span v-if="drugInfo.classification == 5">抗生素</span>
          <span v-if="drugInfo.classification == 6">生化药品</span>
          <span v-if="drugInfo.classification == 7">放射性药品</span>
          <span v-if="drugInfo.classification == 8">血清</span>
          <span v-if="drugInfo.classification == 9">诊断药品</span>
        </a-col>
        <a-col :span="8"><b>通用名：</b>
          {{ drugInfo.commonName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>剂型：</b>
          {{ drugInfo.dosageForm }}
        </a-col>
        <a-col :span="16"><b>用法：</b>
          {{ drugInfo.usages }}
        </a-col>
        <br/>
        <br/>
        <a-col :span="24"><b>适用症状：</b>
          {{ drugInfo.applicableSymptoms }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>适用疾病：</b>
          {{ drugInfo.applicableDisease }}
        </a-col>
        <br/>
        <br/>
        <a-col :span="8"><b>包装清单：</b>
          {{ drugInfo.packingList }}
        </a-col>
        <a-col :span="16"><b>使用剂量：</b>
          {{ drugInfo.dosageUse }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>有效期：</b>
          {{ drugInfo.validityPeriod }} 月
        </a-col>
        <a-col :span="8"><b>批准文号：</b>
          {{ drugInfo.approvalNumber }}
        </a-col>
        <a-col :span="8"><b>生产企业：</b>
          {{ drugInfo.manufacturer }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>单价：</b>
          {{ drugInfo.unitPrice }} 元
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ drugInfo.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">药品图片</span></a-col>
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
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">购买数量</span></a-col>
        <a-col :span="6">
          <a-input-number id="inputNumber" v-model="value" :min="1" :max="drugData.reserve" style="width: 100%"/>
        </a-col>
      </a-row>

    </div>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">添加购物车</a-button>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import baiduMap from '@/utils/map/baiduMap'
import {mapState} from 'vuex'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'drugView',
  props: {
    drugShow: {
      type: Boolean,
      default: false
    },
    drugData: {
      type: Object
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.drugShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      drugInfo: null,
      pharmacyInfo: null,
      value: 1
    }
  },
  watch: {
    drugShow: function (value) {
      if (value) {
        this.selectDrugDetail()
      }
    }
  },
  methods: {
    local (pharmacy) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(pharmacy.longitude, pharmacy.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    selectDrugDetail () {
      this.$get(`/cos/drug-info/selectDrugDetail`, {drugId: this.drugData.drugId, pharmacyId: this.drugData.pharmacyId}).then((r) => {
        this.drugInfo = r.data.drug
        this.pharmacyInfo = r.data.pharmacy
        if (this.drugInfo.images !== null && this.drugInfo.images !== '') {
          this.imagesInit(this.drugInfo.images)
        }
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.local(this.pharmacyInfo)
          }, 500)
        }, 200)
      })
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
    onClose () {
      this.$emit('close')
    },
    handleSubmit () {
      this.drugData.total = this.value
      this.$emit('success', this.drugData)
    }
  }
}
</script>

<style scoped>

</style>
