<template>
  <div style="width: 100%">
    <p style="text-align: center;margin-top: 50px"><a-icon type="smile" theme="twoTone" style="font-size: 100px;" /></p>
    <p style="font-size: 26px;text-align: center;margin-top: 5px">支付成功</p>
    <p style="text-align: center">
      <a-button type="primary" @click="$router.push('/user/order')">
        返回
      </a-button>
    </p>
  </div>
</template>

<script>
import { getUrlKey } from '@/utils/urlKey'
import {mapState} from 'vuex'
export default {
  name: 'Pay',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    if (getUrlKey('out_trade_no') !== null) {
      this.$get('/cos/order-info/payment', { orderCode: getUrlKey('out_trade_no') }).then((r) => {
      })
    }
  }
}
</script>

<style scoped>

</style>
