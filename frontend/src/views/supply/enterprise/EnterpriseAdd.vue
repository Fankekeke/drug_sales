<template>
  <a-modal v-model="show" title="导入供应商信息" @cancel="onClose" :width="500" :footer="null">
    <a-upload-dragger
      name="file"
      :multiple="true"
      action="http://127.0.0.1:9527/cos/enterprise-info/import"
      @change="handleChange"
    >
      <p class="ant-upload-drag-icon">
        <a-icon type="inbox" />
      </p>
      <p class="ant-upload-text">
        Click or drag file to this area to upload
      </p>
      <p class="ant-upload-hint">
        Support for a single or bulk upload
      </p>
    </a-upload-dragger>
    <a-button-group style="margin-top: 20px">
      <a-button type="primary" icon="cloud-download" @click="download"/>
    </a-button-group>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'

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
      previewImage: ''
    }
  },
  methods: {
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
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
