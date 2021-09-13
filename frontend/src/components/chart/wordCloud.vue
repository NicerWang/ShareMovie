<template>
  <div>
    <v-chart :loading="loadStatus" class="chart" :option="option" :autoresize="true" :style="{height:'400px',width:'100%'}"/>
  </div>
</template>

<script>

import VChart, {THEME_KEY} from "vue-echarts";
import {ref} from "vue";
import axios from "axios";

export default ({
  name: "wordCloud",
  components: {
    VChart
  },
  provide: {
    [THEME_KEY]: "light"
  },
  props: {
    movieId: String,
  },
  setup(props) {

    let loadStatus = ref(true);
    let movieData = ref([]);
    let option = ref({});

    const update = function () {
      const originData = movieData.value;
      const data = originData.map(val => ({
        ...val
      }));
      option.value = {
        series: [{
          type: 'wordCloud',
          shape: 'rectangle',
          left: 'center',
          top: 'center',
          height: '100%',
          sizeRange: [20, 80],
          rotationRange: [-45, 45],
          rotationStep: 45,
          gridSize: 10,
          drawOutOfBound: false,
          textStyle: {
            fontFamily: 'sans-serif',
            fontWeight: 'bold',
            color: function () {
              return 'rgb(' + [
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160)
              ].join(',') + ')';
            }
          },
          emphasis: {
            focus: 'self',
            textStyle: {
              shadowBlur: 10,
              shadowColor: '#333'
            }
          },
          data
        }]
      };
      setTimeout(() => {
        loadStatus.value = false;
      }, 500)
    }

    //Ajax Request
    axios({
      url: "GetMovieDataServlet",
      params: {
        movie: props.movieId,
        type: "word"
      }
    }).then((res) => {
      movieData.value = res.data;
      if (movieData.value.length === 0) {
        return;
      }
      update()
    });

    return {
      option,
      loadStatus,
    };
  }
});
</script>

