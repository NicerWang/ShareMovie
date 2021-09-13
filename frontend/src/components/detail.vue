<template>
  <div class="container">
    <div class="card text-start mb-3">
      <div class="row">
        <div class="col-md-4 img-box">
          <img :src="movie.imgLink" @dragstart.prevent class="rounded img-thumbnail" alt="...">
        </div>
        <div class="col-md-5">
          <div class="card-body">
            <h1 class="text-left">{{ movie.name }}</h1>
            <p class="card-text">Director:&nbsp;{{ movie.director }}</p>
            <p class="card-text">Genres:&nbsp;{{ movie.genres }}</p>
            <p class="card-text">Actors:&nbsp;{{ movie.actors }}</p>
            <p class="card-text">Release:&nbsp;{{ movie.releaseDate }}</p>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card-body">
            <br>
            <h3 class="text-left">Current</h3>
            <br>
            <div class="progress" style="height: 50px;font-size: 35px">
              <div class="progress-bar" role="progressbar" :style="{width:score * 20 + '%'}" aria-valuemin="0"
                   aria-valuemax="10">{{ score }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="finished" class="card text-start mb-3">
      <div class="card-header"><h2>Top50 Word Cloud</h2></div>
      <word-cloud :movie-id="movie.id"/>
    </div>
    <rate-panel v-if="finished" :movie-id="movie.id"></rate-panel>
  </div>
</template>

<script>
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import {ref} from "vue";
import axios from "axios";
import wordCloud from './chart/wordCloud.vue';
import ratePanel from "./chart/ratePanel.vue";

import * as echarts from 'echarts/core';
import {
  BarChart,LineChart,PieChart
} from 'echarts/charts';
import {
  TooltipComponent, GridComponent, DatasetComponent, DataZoomComponent, LegendComponent,
} from 'echarts/components';
import {
  CanvasRenderer
} from 'echarts/renderers';
echarts.use(
    [TooltipComponent, GridComponent, DatasetComponent, DataZoomComponent, LegendComponent, BarChart, LineChart, PieChart, CanvasRenderer]
)


export default {
  name: "detail",
  components: {
    wordCloud,
    ratePanel
  },
  setup() {
    let store = useStore();
    let route = useRoute();
    let movie = ref({});
    let score = ref();
    let finished = ref(false);

    //Ajax Request
    axios({
      url: "GetMovieInfoServlet",
      params: {
        movie: route.params.movie
      }
    }).then((res) => {
      movie.value = res.data[0];
      score.value = res.data[1];
      let date = new Date(movie.value.releaseDate);
      movie.value.releaseDate = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
      store.dispatch("Finished");
      setTimeout(() => {
        finished.value = true;
      }, 0)
    });

    return {
      movie,
      score,
      finished,
    }
  },
}
</script>

<style scoped>
.card {
  margin: 5px;
  padding: 10px;
}

.container {
  margin-top: 30px;
}

img {
  margin-top: 10%;
  margin-bottom: 10%;
}

.card-body {
  margin: 20px;
}

h1 {
  margin-bottom: 10%;
}

.img-box {
  text-align: center;
}

h2 {
  margin: 0;
}
</style>