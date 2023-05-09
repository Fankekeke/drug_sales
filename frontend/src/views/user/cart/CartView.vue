<template>
  <a-drawer
    title="购物车"
    :maskClosable="false"
    width=1000
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="show"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <div style="font-size: 13px;font-family: SimHei">
      <a-row :gutter="20" style="width: 100%;margin-top: 20px">
        <a-col :span="12" v-for="(item, index) in cartData" :key="index" style="margin-bottom: 15px">
          <div style="width: 100%;margin-bottom: 15px;text-align: left;box-shadow: rgba(50, 50, 93, 0.25) 0px 50px 100px -20px, rgba(0, 0, 0, 0.3) 0px 30px 60px -30px;">
            <a-card :bordered="false" hoverable>
              <a-carousel autoplay style="height: 150px;" v-if="item.images !== undefined && item.images !== ''">
                <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
                  <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">
                </div>
              </a-carousel>
              <a-card-meta :title="item.drugName + ' * '+ item.total + '件'" :description="item.pharmacyName.slice(0, 25)+'...'" style="margin-top: 10px"></a-card-meta>
              <div style="font-size: 12px;font-family: SimHei;margin-top: 8px">
                <span>{{ item.brand }}</span> |
                <span  style="margin-left: 2px">{{ item.dosageForm }}</span> |
                <span style="color: #f5222d; font-size: 13px;float: right">{{ item.unitPrice }}元</span>
              </div>
            </a-card>
          </div>
        </a-col>
        <a-col v-if="cartData.length === 0">
          <p style="font-size: 23px;font-weight: 500;text-align: center">无数据</p>
          <p style="text-align: center"><a-icon type="folder-open" style="font-size: 50px" theme="twoTone"/></p>
        </a-col>
      </a-row>
    </div>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">添加到订单</a-button>
    </div>
  </a-drawer>
</template>

<script>
import moment from 'moment'
import {mapState} from "vuex";
moment.locale('zh-cn')
export default {
  name: 'cartView',
  props: {
    cartShow: {
      type: Boolean,
      default: false
    },
    cartData: {
      type: Array
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.cartShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false
    }
  },
  watch: {
    cartShow: function (value) {
      if (value) {
        this.selectDrugDetail()
      }
    }
  },
  methods: {
    handleSubmit () {
      if (this.cartData.length === 0) {
        this.$message.error('无数据信息！')
        return false
      }
      let drugList = []
      this.cartData.forEach(e => {
        drugList.push({pharmacyId: e.pharmacyId, drugId: e.drugId, total: e.total, unitPrice: e.unitPrice})
      })
      let values = {userId: this.currentUser.userId, drugString: JSON.stringify(drugList)}
      this.$post('/cos/order-info/orderSubmit', values).then((r) => {
        this.$emit('success')
      })
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-meta-description {
  font-size: 12px;
  font-family: SimHei;
}
>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}
>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}
.ant-carousel >>> .slick-slide {
  text-align: center;
  height: 150px;
  line-height: 150px;
  overflow: hidden;
}
</style>
