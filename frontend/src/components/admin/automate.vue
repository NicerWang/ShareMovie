<template>
  <div>
    <div class="alert alert-success" role="alert" v-show="success">
      Your modification has been submitted.
    </div>
    <div class="alert alert-danger" role="alert" v-show="fail">
      [Error] One task is running, or database exception.
    </div>
    <div class="container">
      <div class="card">
        <h5 class="card-header">Automate</h5>
        <div class="card-body">
          <form class="row g-3">
            <label for="basic-url" class="form-label">Input MovieID below.</label>
            <div class="input-group mb-3">
              <span class="input-group-text" id="basic-addon3">https://movie.douban.com/subject/</span>
              <input type="text" class="form-control" id="basic-url" aria-describedby="basic-addon3" v-model="id">
            </div>
          </form>
          <a class="btn btn-primary" @click="submit">submit</a>
        </div>
      </div>
      <div class="card">
        <h5 class="card-header">Works</h5>
        <div class="card-body">
          <table class="table table-striped table-hover">
            <thead>
            <tr>
              <th scope="col">Movie Id</th>
              <th scope="col">LastUpdateTime</th>
              <th scope="col">Status</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="task in tasks">
              <th scope="row">{{ task.id }}</th>
              <td>{{ task.start }}</td>
              <td>{{ task.status }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {ref} from "vue";
import {useStore} from "vuex";
import axios from "axios";

export default {
  name: "automate",
  setup() {

    let tasks = ref([]);
    let store = useStore();
    let id = ref();
    let success = ref(false);
    let fail = ref(false);

    const getInfo = function () {
      axios({
        url: "/su/GetAllTaskServlet"
      }).then((res) => {
        tasks.value = res.data;
        for (let i = 0; i < tasks.value.length; i++) {
          let newDate = new Date(tasks.value[i].start)
          tasks.value[i].start = newDate.getFullYear() + "-" + (newDate.getMonth() + 1) + "-" + newDate.getDate() + " " + newDate.getHours() + ":" + newDate.getMinutes();
        }
        store.dispatch("Finished");
      });
    }

    const submit = function () {
      store.dispatch("Load");
      axios({
        url: "/su/AddTaskServlet",
        params: {
          id: id.value
        }
      }).then((res) => {
        store.dispatch("Finished")
        if (res.data['status'] === true) {
          success.value = true;
          fail.value = false;
          getInfo()
        } else {
          success.value = false;
          fail.value = true;
        }
      })
    };

    //AJAX Request
    getInfo();
    return {
      tasks,
      id,
      success,
      fail,
      submit
    }
  },

}
</script>

<style scoped>
.card {
  margin-top: 40px;
}

td {
  line-height: 40px
}

th {
  line-height: 40px
}
</style>