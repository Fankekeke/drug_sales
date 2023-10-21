<template>
  <a-card :bordered="false" class="card-area">
    <div style="background:#ECECEC; padding:30px;margin-top: 30px;margin-bottom: 30px">
      <a-row :gutter="30">
        <a-col :span="6" :key="index">
          <a-month-picker @change="onChange" style="width: 100%" placeholder="Select month" />
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
import moment from "moment";

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
    this.selectOrderDays(moment(new Date()).format('YYYY-MM'))
  },
  methods: {
    onChange (date, dateString) {
      this.selectOrderDays(dateString)
    },
    selectOrderDays (date) {
      date = date + '-01'
      this.loading = true
      this.$get(`/cos/pharmacy-info/selectStatisticsByMonth`, {
        date
      }).then((r) => {
        this.series1 = []
        this.series2 = []
        if (r.data.num.length !== 0) {
          this.chartOptions1.xaxis.categories = Array.from(r.data.num, ({name}) => name)
          r.data.num.forEach(e => {
            let value = { name: e.name, data: [e.value] }
            this.series1.push(value)
          })
        }
        if (r.data.price.length !== 0) {
          this.chartOptions2.xaxis.categories = Array.from(r.data.price, ({name}) => name)
          r.data.price.forEach(e => {
            let value = { name: e.name, data: [e.value] }
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
