<template>
  <div>
    <a-row :gutter="15">
      <a-col :span="24">
        <div style="background:#ECECEC; padding:30px;margin-top: 30px">
          <a-card :bordered="false">
            <a-spin :spinning="dataLoading">
              <a-calendar>
                <ul slot="dateCellRender" slot-scope="value" class="events">
                  <li v-for="item in getListData(value)" :key="item.content">
                    <a-badge :status="item.type" :text="item.content" />
                  </li>
                </ul>
              </a-calendar>
            </a-spin>
          </a-card>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import moment from 'moment'
export default {
  name: 'Work',
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  data () {
    return {
      rowId: null,
      loading: false,
      courseInfo: [],
      dataLoading: false
    }
  },
  mounted () {
    this.dataInit()
  },
  methods: {
    isDuringDate (beginDateStr, endDateStr, curDataStr) {
      let curDate = new Date(curDataStr)
      let beginDate = new Date(beginDateStr)
      let endDate = new Date(endDateStr)
      if (curDate >= beginDate && curDate <= endDate) {
        return true
      }
      return false
    },
    getListData (value) {
      let listData = []
      this.courseInfo.forEach(item => {
        if ((moment(value).format('YYYY-MM-DD')) === (moment(item.createDate).format('YYYY-MM-DD'))) {
          listData.push({type: 'success', content: item.orderName})
        }
      })
      return listData || []
    },
    dataInit () {
      this.dataLoading = true
      this.$get(`/cos/repair-info/list/staff/${this.currentUser.userId}`).then((r) => {
        this.courseInfo = r.data.repairList
        this.dataLoading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
