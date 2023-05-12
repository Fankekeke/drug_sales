<template>
  <a-row :gutter="20" style="width: 100%;margin-top: 20px">
    <a-col :span="24" style="margin-top: 15px;margin-bottom: 35px;">
      <a-input-search placeholder="搜索药品" style="width: 300px;margin: 0 auto" v-model="key" @search="selectDrugList" />
      <a-button type="primary" style="margin-left: 25px" @click="cartOpen">
        购物车【{{ cartView.data.length }} 件】
      </a-button>
    </a-col>
    <a-col :span="24"></a-col>
    <a-col :span="4" v-for="(item, index) in drugList" :key="index" style="margin-bottom: 15px">
      <div style="width: 100%;margin-bottom: 15px;text-align: left;box-shadow: rgba(50, 50, 93, 0.25) 0px 50px 100px -20px, rgba(0, 0, 0, 0.3) 0px 30px 60px -30px;">
        <a-card :bordered="false" @click="drugDetail(item)" hoverable>
          <a-carousel autoplay style="height: 150px;" v-if="item.images !== undefined && item.images !== ''">
            <div style="width: 100%;height: 150px" v-for="(item, index) in item.images.split(',')" :key="index">
              <img :src="'http://127.0.0.1:9527/imagesWeb/'+item" style="width: 100%;height: 250px">
            </div>
          </a-carousel>
          <a-card-meta :title="item.drugName" :description="item.pharmacyName.slice(0, 25)+'...'" style="margin-top: 10px"></a-card-meta>
          <div style="font-size: 12px;font-family: SimHei;margin-top: 8px">
            <span>{{ item.brand }}</span> |
            <span  style="margin-left: 2px">{{ item.dosageForm }}</span> |
            <span style="color: #f5222d; font-size: 13px;float: right">{{ item.unitPrice }}元</span>
          </div>
        </a-card>
      </div>
    </a-col>
    <drug-view
      @close="handleDrugViewClose"
      @success="handleDrugViewSuccess"
      :drugShow="drugView.visiable"
      :drugData="drugView.data">
    </drug-view>
    <cart-view
      @close="handleCartViewClose"
      @success="handleCartViewSuccess"
      :cartShow="cartView.visiable"
      :cartData="cartView.data">
    </cart-view>
  </a-row>
</template>

<script>
import drugView from './DrugView'
import CartView from './CartView'

export default {
  name: 'Cart',
  components: {CartView, drugView},
  data () {
    return {
      drugList: [],
      drugView: {
        visiable: false,
        data: null
      },
      cartView: {
        visiable: false,
        data: []
      },
      key: ''
    }
  },
  mounted () {
    this.selectDrugList()
  },
  methods: {
    cartOpen () {
      this.cartView.visiable = true
    },
    handleDrugViewClose () {
      this.drugView.visiable = false
    },
    handleCartViewClose () {
      this.cartView.visiable = false
    },
    handleCartViewSuccess () {
      this.cartView.visiable = false
      this.cartView.data = []
      this.$message.success('添加订单成功，请前往我的订单付款')
    },
    handleDrugViewSuccess (drugData) {
      let check = false
      this.cartView.data.forEach(e => {
        if (e.drugId === drugData.drugId && e.pharmacyId === drugData.pharmacyId) {
          e.total = drugData.total
          check = true
        }
      })
      if (!check) {
        this.cartView.data.push(drugData)
      }
      this.drugView.visiable = false
    },
    drugDetail (row) {
      this.drugView.visiable = true
      this.drugView.data = row
    },
    selectDrugList () {
      this.$get(`/cos/pharmacy-inventory/drug/list`, {key: this.key}).then((r) => {
        this.drugList = r.data.data
      })
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
