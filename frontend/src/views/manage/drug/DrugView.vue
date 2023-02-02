<template>
  <a-modal v-model="show" title="药品详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="drugData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">药品信息</span></a-col>
        <a-col :span="8"><b>药品名称：</b>
          {{ drugData.name }}
        </a-col>
        <a-col :span="8"><b>药品编号：</b>
          {{ drugData.code }}
        </a-col>
        <a-col :span="8"><b>所属品牌：</b>
          {{ drugData.brand }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>所属分类：</b>
          <span v-if="drugData.category == 1">可卡因</span>
          <span v-if="drugData.category == 2">维生素制剂</span>
          <span v-if="drugData.category == 3">鱼肝油</span>
          <span v-if="drugData.category == 4">药物饮料</span>
          <span v-if="drugData.category == 5">膳食纤维</span>
        </a-col>
        <a-col :span="8"><b>药品类别：</b>
          <span v-if="drugData.classification == 1">中药材</span>
          <span v-if="drugData.classification == 2">中药饮片</span>
          <span v-if="drugData.classification == 3">中西成药</span>
          <span v-if="drugData.classification == 4">化学原料药</span>
          <span v-if="drugData.classification == 5">抗生素</span>
          <span v-if="drugData.classification == 6">生化药品</span>
          <span v-if="drugData.classification == 7">放射性药品</span>
          <span v-if="drugData.classification == 8">血清</span>
          <span v-if="drugData.classification == 9">诊断药品</span>
        </a-col>
        <a-col :span="8"><b>通用名：</b>
          {{ drugData.commonName }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>剂型：</b>
          {{ drugData.dosageForm }}
        </a-col>
        <a-col :span="16"><b>用法：</b>
          {{ drugData.usages }}
        </a-col>
        <br/>
        <br/>
        <a-col :span="24"><b>适用症状：</b>
          {{ drugData.applicableSymptoms }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>适用疾病：</b>
          {{ drugData.applicableDisease }}
        </a-col>
        <br/>
        <br/>
        <a-col :span="8"><b>包装清单：</b>
          {{ drugData.packingList }}
        </a-col>
        <a-col :span="16"><b>使用剂量：</b>
          {{ drugData.dosageUse }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>有效期：</b>
          {{ drugData.validityPeriod }} 月
        </a-col>
        <a-col :span="8"><b>批准文号：</b>
          {{ drugData.approvalNumber }}
        </a-col>
        <a-col :span="8"><b>生产企业：</b>
          {{ drugData.manufacturer }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>单价：</b>
          {{ drugData.unitPrice }} 元
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ drugData.createDate }}
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
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
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
      previewImage: ''
    }
  },
  watch: {
    drugShow: function (value) {
      if (value) {
        if (this.drugData.images !== null && this.drugData.images !== '') {
          this.imagesInit(this.drugData.images)
        }
      }
    }
  },
  methods: {
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
    }
  }
}
</script>

<style scoped>

</style>
