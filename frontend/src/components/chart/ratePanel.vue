<template>
  <div class="card text-start mb-3">
    <div class="card-header"><h2>Rating Changes</h2></div>
    <div>
      <v-chart :loading="loadStatus" class="chart" :option="rateChangeOption" :autoresize="true"
               :style="{height:'400px',width:'100%'}"/>
    </div>
  </div>
  <div class="card text-start mb-3">
    <div class="card-header"><h2>Daily Average Rating</h2></div>
    <div>
      <v-chart :loading="loadStatus" class="chart" :option="dayAverRateOption" :autoresize="true"
               :style="{height:'400px',width:'100%'}"/>
    </div>
  </div>
  <div class="card text-start mb-3">
    <div class="card-header"><h2>Daily Comment Sum</h2></div>
    <div>
      <v-chart :loading="loadStatus" class="chart" :option="dayCommentSumOption" :autoresize="true"
               :style="{height:'400px',width:'100%'}"/>
    </div>
  </div>
  <div class="card text-start mb-3">
    <div class="card-header"><h2>Rating Distribution</h2></div>
    <div>
      <v-chart :loading="loadStatus" class="chart" :option="starPieOption" :autoresize="true"
               :style="{height:'400px',width:'100%'}"/>
    </div>
  </div>
</template>

<script>
import VChart, {THEME_KEY} from "vue-echarts";
import * as echarts from 'echarts/core';

import {reactive, ref} from "vue";
import axios from "axios";

export default {
  name: "ratePanel",
  components:{
    VChart,
  },
  provide: {
    [THEME_KEY]: "light"
  },
  props:{
    movieId:String
  },
  setup(props){

    let loadStatus = ref(true);
    let movieData = [];
    let starData = [];

    const getRandomColors = function () {
      const colors = [['rgba(97, 92, 143, 0.7)', 'rgba(89, 173, 190, 0.7)', 'rgba(177, 227, 212, 0.7)'], ['rgba(171, 183, 255, 0.7)', 'rgba(189, 213, 255, 0.7)', 'rgba(224, 241, 255, 0.7)'],
        ['rgba(71, 71, 69, 0.7)', 'rgba(112, 111, 109, 0.7)', 'rgba(171, 171, 173, 0.7)'], ['rgba(211, 121, 132, 0.7)', 'rgba(234, 153, 164, 0.7)', 'rgba(251, 216, 217, 0.7)'], ['rgba(36, 90, 106, 0.7)', 'rgba(146, 189, 130, 0.7)', 'rgba(194, 230, 166, 0.7)']];
      return colors[Math.floor(Math.random() * 100) % 5];
    }

    let rateChangeColors = getRandomColors();
    let dayCommentSumColors = getRandomColors();
    let dayAverRateColors = getRandomColors();

    let rateChangeOption = reactive({
      tooltip: {
        trigger: 'axis',
      },
      dataset: {
        dimensions: ['date', 'accumulateAverRating'],
        source: []
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
      },
      yAxis: {
        type: 'value',
        boundaryGap: [0, '100%']
      },
      dataZoom: [{
        start: 0,
        end: 100,
        minSpan: 20,
        backgroundColor: '#fff',
        fillerColor: rateChangeColors[2],
      }],
      series: [
        {
          type: 'line',
          showBackground: true,
          sampling: 'lttb',
          connectNulls: true,
          clip: false,
          itemStyle: {
            color: rateChangeColors[0]
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: rateChangeColors[1]
            }, {
              offset: 1,
              color: rateChangeColors[2]
            }])
          }
        }
      ]
    });

    let dayAverRateOption = reactive({
      tooltip: {
        trigger: 'axis',
      },
      dataset: {
        dimensions: ['date', 'dayAverRating'],
        source: []
      },
      xAxis: {
        type: 'category',
        boundaryGap: true,
      },
      yAxis: {
        type: 'value',
        boundaryGap: [0, '100%']
      },
      dataZoom: [{
        start: 0,
        end: 100,
        minSpan: 20,
        backgroundColor: '#fff',
        fillerColor: dayAverRateColors[2],
        color: "rgba(0, 0, 0, 1)"
      }],
      series: [
        {
          type: 'bar',
          showBackground: true,
          sampling: 'lttb',
          connectNulls: true,
          clip: false,
          itemStyle: {
            color: dayAverRateColors[1]
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: dayAverRateColors[1]
            }, {
              offset: 1,
              color: dayAverRateColors[2]
            }])
          }
        }
      ]
    });

    let dayCommentSumOption = reactive({
      tooltip: {
        trigger: 'axis',
      },
      dataset: {
        dimensions: ['date', {
          name:'dayCommentsNum'
        }],
        source: []
      },
      xAxis: {
        type: 'category',
        boundaryGap: true,
      },
      yAxis: {
        type: 'value',
        boundaryGap: [0, '100%']
      },
      dataZoom: [{
        start: 0,
        end: 100,
        minSpan: 20,
        backgroundColor: '#fff',
        fillerColor: dayCommentSumColors[2],
        color: "rgba(0, 0, 0, 1)"
      }],
      series: [
        {
          type: 'bar',
          showBackground: true,
          sampling: 'lttb',
          connectNulls: true,
          clip: false,
          itemStyle: {
            color: dayCommentSumColors[1]
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: dayCommentSumColors[1]
            }, {
              offset: 1,
              color: dayCommentSumColors[2]
            }])
          }
        }
      ]
    });

    let starPieOption =  reactive({
      legend: {
        bottom: '10%',
        right: '10%',
        orient: 'vertical'
      },
      dataset: {
        dimensions: ['name', 'value'],
        source: []
      },
      series: [
        {
          type: 'pie',
          radius: ['50%', '90%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '30',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
        }
      ]
    });

    const update = function () {
      movieData.sort();
      let data = movieData.map(val => {
        const temp = new Date(val.date);
        val.date = temp.getFullYear() + "-" + (temp.getMonth() + 1) + "-" + temp.getDate();
        return val;
      });
      let starDate = starData.map(val => {
        val.name = val.name + " Star";
        return val;
      })
      dayAverRateOption.dataset.source = data;
      dayCommentSumOption.dataset.source = data;
      rateChangeOption.dataset.source = data;
      starPieOption.dataset.source = starData;
      dayAverRateOption.yAxis.max = 5;
      dayAverRateOption.yAxis.min = 0;
      rateChangeOption.yAxis.max = 5;
      rateChangeOption.yAxis.min = 0;
      setTimeout(()=>{
        loadStatus.value = false;
      },1000)
    }

    //AJAX Request
    axios({
      url: "GetMovieDataServlet",
      params: {
        movie: props.movieId,
        type: "rate"
      }
    }).then((res) => {
      movieData = res.data;
      if (movieData.length === 0) {
        return;
      }
      return axios({
        url:"GetMovieDataServlet",
        params:{
          movie: props.movieId,
          type: "star"
        }
      });
    }).then((res)=>{
      starData = res.data;
      update();
    });
    return{
      dayAverRateOption,
      dayCommentSumOption,
      rateChangeOption,
      starPieOption,
      loadStatus
    }
  }

}
</script>

<style scoped>

</style>