<template>
  <div class="container">
    <div class="alert alert-success" role="alert" v-show="success">
      Your modification has been submitted.
    </div>
    <div class="alert alert-success" role="alert" v-show="fail">
      [Error] Database Exception.
    </div>
    <table class="table table-striped table-hover">
      <thead>
      <tr>
        <th scope="col">Id</th>
        <th scope="col">Name</th>
        <th scope="col">Director</th>
        <th scope="col">Control</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(m) in movies">
        <th scope="row">{{m.id}}</th>
        <td>{{ m.name }}</td>
        <td>{{ m.director }}</td>
        <td>
          <button type="button" class="btn btn-danger" @click="remove(m.id)">Remove</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import {useStore} from "vuex";
import {ref} from "vue";
import axios from "axios";

export default {
  name: "movieControl",
  setup(){

    const store = useStore();

    let success = ref(false);
    let fail = ref(false)
    let movies = ref([]);

    const getInfo = function () {
      axios({
        method:"POST",
        url:"GetAllMovieServlet",
      }).then((res)=>{
        movies.value = res.data;
        store.dispatch("Finished");
      });
    }

    const remove = function (id) {
      store.dispatch("Load")
      axios({
        url:"su/DeleteMovieServlet",
        params:{
          id:id
        }
      }).then((res)=>{
        if(res.data['status'] === true){
          success.value = true;
          fail.value = false;
          getInfo()
        }
        else {
          success.value = false;
          fail.value = true;
        }
      })
    }

    //AJAX Request
    getInfo();
    return{
      movies,
      success,
      fail,
      remove
    }
  },
}
</script>

<style scoped>
td{
  line-height: 55px
}
th{
  line-height: 55px
}
table{
  width: 80%;
  margin: 0 auto;
}
</style>