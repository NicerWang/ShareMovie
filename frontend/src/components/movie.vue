<template>
  <div class="album py-5 bg-light">
    <div class="container">

      <div class="row row-cols-2 row-cols-sm-2 row-cols-md-4">
        <div class="col" v-for="(movie) in movies" @click="changeMovie(movie.id)">
          <div class="card shadow-sm">
            <img class="img-thumbnail" @dragstart.prevent :src="movie.imgLink" alt="" @load="loadFinished">
            <div class="card-body">
              <h5 class="card-title">{{ movie.name }}</h5>
              <p class="card-text">{{ movie.director }}</p>
              <div class="justify-content-between align-items-center">
                <small class="text-muted">{{ movie.releaseDate }}</small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11;display: block">
      <div id="liveToast" class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
          <img src="../assets/icon.svg" class="rounded me-2"  alt="icon"/>
          <strong class="me-auto">Developer</strong>
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
          Hello, enjoy the data here.
        </div>

      </div>

    </div>
    <br>
    <br>
    <pagination :max="total" :now="parseInt(nowPage)" @next="next" @prev="previous"></pagination>
  </div>

</template>

<script>

import {useStore} from "vuex";
import {useRouter, useRoute} from "vue-router";
import axios from "axios";
import { ref, watch} from "vue";
import pagination from "./pagination.vue";

export default {
  name: "movie",
  components:{
    pagination
  },
  setup(){
    const pageSize = 8;

    let store = useStore();
    let router = useRouter();
    let route = useRoute();
    let movies = ref([]);
    let loadCnt = 0;
    let total = ref(0);
    let picStatus = ref(false);
    let infoStatus = ref(false);
    let nowPage = ref(parseInt(route.params.page));

    watch([picStatus,infoStatus],(newValue,oldValue)=>{
      if(newValue[0] && newValue[1]){
        store.dispatch("Finished");
      }
    })
    watch(nowPage,()=>{
      loadCnt = 0;
      infoStatus.value = false;
      picStatus.value = false;
      store.dispatch("Load");
      update()
    });

    const changeMovie = function (id){
      router.push("/detail/" + id);
    }

    //AJAX Request
    const update = function () {
      infoStatus.value = false;
      picStatus.value = false;
      axios({
        url:"GetAllMovieServlet",
        params:{
          page:nowPage.value,
          pageSize:pageSize,
        },
        async:false
      }).then((res)=>{
        movies.value = res.data;
        for (const m of movies.value) {
          let date = new Date(m.releaseDate);
          m.releaseDate = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
        }
        infoStatus.value = true;
        // setTimeout(()=>{
        //   store.dispatch("Finished");
        // },200)
      });
    }


    const loadFinished = function () {
      loadCnt++;
      if(movies.value != null && loadCnt >= movies.value.length){
        picStatus.value = true;
      }
    }

    const next = function () {
      router.replace("/movie/" + (parseInt(route.params.page)  + 1));
    }
    const previous = function () {
      router.replace("/movie/" + (parseInt(route.params.page)  - 1));
    }

    axios({
      url:"GetMovieCntServlet",
    }).then((res)=>{
      total.value = Math.ceil(res.data / pageSize);
      update()
    })


    return {
      nowPage,
      movies,
      total,
      loadFinished,
      changeMovie,
      next,
      previous,
    }
  },
  watch:{
    '$route.params'(newval) {
      this.nowPage = newval.page;
    }
  }
}
</script>

<style scoped>
.card-body{
  height: 100%;
  margin: 0 auto;
  line-height: 100%;
}
.card-text{
  margin-top: 20%;
  font-size: 14px;
}

.card:hover{
  cursor: pointer;
}
.card{
  padding: 10px;
  margin-top: 15px;
  margin-bottom: 15px;
  margin-left: 5px;
}
p.card-text{
  margin: 8px;
}
.card:hover {

  background: #efefef;
  animation: pulse;
  animation-duration: 1.5s;
  animation-iteration-count: infinite;
}
.album{
  padding-top: 20px !important;
}

</style>