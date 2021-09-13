<template>
  <div>
    <div class="alert alert-success" role="alert" v-show="success">
      Your modification has been submitted.
    </div>
    <div class="alert alert-danger" role="alert" v-show="fail">
      [Error] Database Exception.
    </div>
    <table class="table table-striped table-hover">
      <thead>
      <tr>
        <th scope="col">Telephone</th>
        <th scope="col">Name</th>
        <th scope="col">Password(Hashed)</th>
        <th scope="col">Control</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(u) in user">
        <th scope="row">{{ u.tel }}</th>
        <td>{{ u.name }}</td>
        <td>{{ u.pwd }}</td>
        <td>
          <button type="button" class="btn btn-dark" @click="update(u.tel,'name')">New Name</button>
          &nbsp;
          <button type="button" class="btn btn-dark" @click="update(u.tel,'pwd')">New Password</button>
          &nbsp;
          <button type="button" class="btn btn-danger" @click="remove(u.tel)">Remove</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import axios from 'axios';
import {ref} from 'vue';
import {useStore} from "vuex";

export default {
  name: "userControl",
  setup() {

    const store = useStore();
    let success = ref(false);
    let fail = ref(false);
    let user = ref([]);

    const getInfo = function () {
      axios({
        method: "POST",
        url: "su/GetAllUserServlet",
      }).then((res) => {
        user.value = res.data;
        store.dispatch("Finished");
      });
    }

    const update = function (tel, type) {
      let info = prompt("User telephone : " + tel + "\n" + "Input New " + type + ":");
      if (info == null || info === "") {
        return;
      }
      store.dispatch("Load");
      axios({
        url: "su/UpdateUserInfoServlet",
        params: {
          [type]: info,
          tel: tel
        }
      }).then((res) => {
        if (res.data['status'] === true) {
          success.value = true;
          fail.value = false;
          getInfo()
        } else {
          success.value = false;
          fail.value = true;
        }
      })
    }

    const remove = function (tel) {
      store.dispatch("Load");
      axios({
        url: "su/DeleteUserServlet",
        params: {
          tel: tel
        }
      }).then((res) => {
        if (res.data['status'] === true) {
          success.value = true;
          getInfo()
        } else success.value = false;
      });
    }

    //AJAX Request
    getInfo();
    return {
      user,
      success,
      fail,
      update,
      remove
    }
  },
}
</script>

<style scoped>
td {
  line-height: 55px
}
th {
  line-height: 55px
}
table {
  width: 80%;
  margin: 0 auto;
}
</style>