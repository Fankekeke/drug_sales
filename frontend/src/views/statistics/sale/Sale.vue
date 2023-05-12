<template>
  <a-card :bordered="false" class="card-area">
    <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
      <a-row :gutter="30">
        <a-col :span="6" v-for="(item, index) in workStatusList" :key="index">
          <a-card :bordered="false">
            <span slot="title">
              <a-badge status="processing"/>
              <span style="font-size: 14px;font-family: SimHei">
                {{ item.pharmacyName }}
                <span style="margin-left: 15px">评价得分：【{{ item.score }}分】</span>
              </span>
            </span>
          </a-card>
        </a-col>
      </a-row>
    </div>
    <a-row :gutter="20">
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions1" :series="series1"></apexchart>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-card hoverable :bordered="false" style="width: 100%">
          <a-skeleton active v-if="loading" />
          <apexchart v-if="!loading" type="bar" height="300" :options="chartOptions2" :series="series2"></apexchart>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'Work',
  data () {
    return {
      workStatusList: [],
      loading: false,
      series1: [],
      chartOptions1: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '十天内各家订单数量统计',
          align: 'left'
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        yaxis: {
          title: {
            text: ''
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 单'
            }
          }
        }
      },
      series2: [],
      chartOptions2: {
        chart: {
          type: 'bar',
          height: 300
        },
        title: {
          text: '十天内各家订单收益统计',
          align: 'left'
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%'
          }
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: []
        },
        yaxis: {
          title: {
            text: ''
          }
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 元'
            }
          }
        }
      }
    }
  },
  mounted () {
    this.getWorkStatusList()
    this.selectOrderDays()
  },
  methods: {
    getWorkStatusList () {
      this.$get(`/cos/pharmacy-info/evaluate/rank`).then((r) => {
        this.workStatusList = r.data.data
      })
    },
    selectOrderDays () {
      this.loading = true
      this.$get(`/cos/pharmacy-info/selectOrderDays`).then((r) => {
        this.series1 = []
        if (r.data.orderNumDays.length !== 0) {
          this.chartOptions1.xaxis.categories = Array.from(r.data.orderNumDays[0].value, ({days}) => days)
          r.data.orderNumDays.forEach(e => {
            let value = { name: e.name, data: Array.from(e.value, ({count}) => count) }
            this.series1.push(value)
          })
        }
        if (r.data.orderPriceDays.length !== 0) {
          this.chartOptions1.xaxis.categories = Array.from(r.data.orderPriceDays[0].value, ({days}) => days)
          this.chartOptions2.xaxis.categories = Array.from(r.data.orderPriceDays[0].value, ({days}) => days)
          r.data.orderPriceDays.forEach(e => {
            let value = { name: e.name, data: Array.from(e.value, ({price}) => price) }
            this.series2.push(value)
          })
        }
        setTimeout(() => {
          this.loading = false
        }, 200)
      })
    }
  }
}
</script>

<style scoped>

</style>
