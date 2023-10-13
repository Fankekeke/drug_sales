<template>
  <a-modal v-model="show" title="供应商详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px;font-family: SimHei" v-if="enterpriseData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">供应商信息</span></a-col>
        <a-col :span="8"><b>供应商名称：</b>
          {{ enterpriseData.name }}
        </a-col>
        <a-col :span="8"><b>单位简称或代号：</b>
          {{ enterpriseData.abbreviation }}
        </a-col>
        <a-col :span="8"><b>统一社会信用代码：</b>
          {{ enterpriseData.creditCode }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>单位性质：</b>
          {{ enterpriseData.nature }}
        </a-col>
        <a-col :span="8"><b>二级供应商单位性质：</b>
          {{ enterpriseData.natureTwo }}
        </a-col>
        <a-col :span="8"><b>经营状态：</b>
          {{ enterpriseData.status }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>法定代表人：</b>
          {{ enterpriseData.corporateRepresentative }}
        </a-col>
        <a-col :span="16"><b>法定代表人身份证号：</b>
          {{ enterpriseData.corporateRepresentativeId }}
        </a-col>
        <br/>
        <br/>
        <a-col :span="24"><b>法定代表人电话：</b>
          {{ enterpriseData.corporateRepresentativePhone }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="24"><b>注册资本（万元）：</b>
          {{ enterpriseData.registeredCapital }}
        </a-col>
        <br/>
        <br/>
        <a-col :span="8"><b>注册资金币种：</b>
          {{ enterpriseData.registeredCapitalCurrency }}
        </a-col>
        <a-col :span="16"><b>成立日期：</b>
          {{ enterpriseData.establishmentDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>营业期限始期：</b>
          {{ enterpriseData.businessBeginDate }}
        </a-col>
        <a-col :span="8"><b>营业期限止期：</b>
          {{ enterpriseData.businessEndDate }}
        </a-col>
        <a-col :span="8"><b>注册地址：</b>
          {{ enterpriseData.registeredAddress }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>英文供应商名称：</b>
          {{ enterpriseData.enName }}
        </a-col>
        <a-col :span="8"><b>所属行业：</b>
          {{ enterpriseData.industry }}
        </a-col>
        <a-col :span="8"><b>单位简介：</b>
          {{ enterpriseData.unitDescription }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>省：</b>
          {{ enterpriseData.province }}
        </a-col>
        <a-col :span="8"><b>市：</b>
          {{ enterpriseData.city }}
        </a-col>
        <a-col :span="8"><b>区：</b>
          {{ enterpriseData.district }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>经营范围：</b>
          {{ enterpriseData.businessScope }}
        </a-col>
        <a-col :span="8"><b>创建时间：</b>
          {{ enterpriseData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">供应商图片</span></a-col>
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
  name: 'enterpriseView',
  props: {
    enterpriseShow: {
      type: Boolean,
      default: false
    },
    enterpriseData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.enterpriseShow
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
    enterpriseShow: function (value) {
      if (value) {
        if (this.enterpriseData.images !== null && this.enterpriseData.images !== '') {
          this.imagesInit(this.enterpriseData.images)
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
