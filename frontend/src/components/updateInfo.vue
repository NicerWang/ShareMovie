<template>
  <main class="form-signin">
    <div class="alert alert-danger" role="alert" v-show="wrong">
      Invalid Password or Username
    </div>
    <form>
      <br>
      <br>
      <img class="mb-4" src="../assets/update.svg" alt="" width="72" height="57">
      <h1 class="h3 mb-3 fw-normal">Update Your Information</h1>
      <div class="form-floating">
        <input type="tel" class="form-control" id="floatingTel" readonly v-model="tel">
        <label for="floatingTel">Telephone</label>
      </div>

      <div class="form-floating">
        <input type="text" :class="nameCls" id="floatingName" placeholder=" " v-model="name">
        <label for="floatingName">Name</label>
        <div class="invalid-tooltip">
          Need at least 3 characters(No special symbol).
        </div>
      </div>
      <div class="form-floating">
        <input type="password" :class="pwdCls" id="floatingPassword" placeholder=" " v-model="pwd">
        <label for="floatingPassword">New Password</label>
        <div class="invalid-tooltip">
          Need at least 8 characters(No special symbol).
        </div>
      </div>
      <br>
      <br>
      <button type="button" class="w-100 btn btn-lg btn-primary" @click="update">Update</button>
    </form>
  </main>
</template>

<script>
import axios from "axios";
import {ref, watch} from 'vue';
import {useRouter} from "vue-router";
import {useStore} from "vuex";

export default {
  name: "updateInfo",
  setup() {
    const store = useStore();
    const router = useRouter();
    store.dispatch("Load");
    const namePattern = /^[a-zA-Z0-9]{3,15}$/;
    const pwdPattern = /^[a-zA-Z0-9]{8,16}$/;

    let name = ref("");
    let pwd = ref("");
    let tel = ref("");
    let wrong = ref(false);

    let pwdCls = ref({
      'form-control':true,
      'is-invalid':false
    });
    let nameCls = ref({
      'form-control':true,
      'is-invalid':false
    });

    watch([name,pwd],()=>{
      if(name.value !== "") nameCls.value['is-invalid'] = !namePattern.test(name.value);
      if(pwd.value !== "") pwdCls.value['is-invalid'] = !pwdPattern.test(pwd.value);
    })

    //AJAX Request
    axios({
      url: "GetUserInfoServlet"
    }).then((res) => {
      let user = res.data;
      tel.value = user['tel'];
      name.value = user['name'];
    })

    const update = () => {
      if (name.value === "" || !namePattern.test(name.value)) {
        wrong.value = true;
        return;

      }
      if (pwd.value !== ""){
        if(!pwdPattern.test(pwd.value)){
          return;
        }
      }
      axios({
        url: "UpdateUserInfoServlet",
        params: {
          name: name.value.trim(),
          pwd: pwd.value.trim(),
          tel: tel.value,
        },
        async: false
      }).then(() => {
        router.push("/movie")
      })
    }
    store.dispatch("Finished")
    return {
      name,
      pwd,
      tel,
      wrong,
      nameCls,
      pwdCls,
      update,
    }
  }
}
</script>

<style scoped>
.form-signin {
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;
}

.form-signin .checkbox {
  font-weight: 400;
}

.form-signin .form-floating:focus-within {
  z-index: 2;
}

.form-signin input[type="tel"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>