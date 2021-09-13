<template>
  <div class="container">
    <div class="d-flex align-items-center p-3 my-3 bg-white text-black rounded shadow-sm">
      <img class="me-3" src="../../assets/admin.svg" alt="" width="48" height="38">
      <div class="lh-1">
        <h1 class="h6 mb-0 text-black lh-1">Administration Panel</h1>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">User Control</h5>
            <p class="card-text">Modify user info or delete user</p>
            <router-link to="/admin/u" class="btn btn-primary">Go</router-link>
          </div>
        </div>
      </div>
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Delete Movie</h5>
            <p class="card-text">Delete movie and its info</p>
            <router-link to="/admin/m" class="btn btn-primary">Go</router-link>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Automate</h5>
            <p class="card-text">Get data from "movie.douban.com" automatically</p>
            <router-link to="/admin/a" class="btn btn-primary">Go</router-link>
          </div>
        </div>
      </div>
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title">Recommend Info</h5>
            <p class="card-text">Result of recommendations.</p>
            <router-link to="/admin/r" class="btn btn-primary">Go</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {useStore} from "vuex";

export default {
  name: "adminPanel",
  setup() {
    let store = useStore();
    store.dispatch("Finished")
  }
}
</script>

<style scoped>
.card {
  margin: 40px !important;
}
html,
body {
  overflow-x: hidden;
}

body {
  padding-top: 56px;
}
.bg-white {
  background-color: #ffffff;
}

</style>