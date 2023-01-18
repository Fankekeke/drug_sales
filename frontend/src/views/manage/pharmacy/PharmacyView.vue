<template>
  <a-modal v-model="show" title="药店详情" @cancel="onClose" :width="800">
    <template slot="footer">drug
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="pharmacyData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">药店信息</span></a-col>
        <a-col :span="8"><b>药店名称：</b>
          {{ pharmacyData.name }}
        </a-col>
        <a-col :span="8"><b>药店编号：</b>
          {{ pharmacyData.code }}
        </a-col>
        <a-col :span="8"><b>详细地址：</b>
          {{ pharmacyData.address }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>营业状态：</b>
          <span v-if="pharmacyData.businessStatus == 1" style="color: green">营业中</span>
          <span v-if="pharmacyData.businessStatus == 2" style="color: red">歇业</span>
        </a-col>
        <a-col :span="8"><b>法人姓名：</b>
          {{ pharmacyData.legalPerson }}
        </a-col>
        <a-col :span="8"><b>联系电话：</b>
          {{ pharmacyData.phone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>营业时间：</b>
          {{ pharmacyData.businessHours }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ pharmacyData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">药店图片</span></a-col>
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
  name: 'pharmacyView',
  props: {
    pharmacyShow: {
      type: Boolean,
      default: false
    },
    pharmacyData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.pharmacyShow
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
    pharmacyShow: function (value) {
      if (value) {
       if (this.pharmacyData.images !== null && this.pharmacyData.images !== '') {
          this.imagesInit(this.pharmacyData.images)
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
