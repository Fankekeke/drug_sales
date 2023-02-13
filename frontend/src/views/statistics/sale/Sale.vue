<template>
  <a-card :bordered="false" class="card-area">
    <div style="background:#ECECEC; padding:30px;margin-top: 30px">
      <a-row :gutter="30">
        <a-col :span="6" v-for="(item, index) in workStatusList" :key="index">
          <a-card :bordered="false">
            <span slot="title">
              <a-badge status="processing" v-if="item.repair !== undefined"/>
              <a-badge status="success" v-else/>
              <span style="font-size: 14px;font-family: SimHei">
                {{ item.staff.name }}
                <span v-if="item.repair !== undefined">【工作】</span>
                <span v-else>【空闲】</span>
              </span>
            </span>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </a-card>
</template>

<script>
export default {
  name: 'Work',
  data () {
    return {
      workStatusList: []
    }
  },
  mounted () {
    this.getWorkStatusList()
  },
  methods: {
    getWorkStatusList () {
      this.$get(`/cos/staff-info/workStatus`).then((r) => {
        this.workStatusList = r.data.data
        console.log(JSON.stringify(this.workStatusList))
      })
    }
  }
}
</script>

<style scoped>

</style>
