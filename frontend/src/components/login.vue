<template>
  <div class="form-signin">
    <div class="alert alert-danger" role="alert" v-show="wrong">
      Wrong Password or Telephone
    </div>
    <form>
      <br>
      <br>
      <img class="mb-4" src="../assets/icon.svg" alt="" width="72" height="57">
      <h1 class="h3 mb-3 fw-normal">Sign in to<br>Share Movie</h1>
      <div class="form-floating">
        <input type="tel" :class="telCls" id="floatingInput" placeholder="Telephone" v-model="tel">
        <label for="floatingInput">Telephone</label>
        <div class="invalid-tooltip">
          Need exactly 11 numbers.
        </div>
      </div>
      <div class="form-floating">
        <input type="password" :class="pwdCls" id="floatingPassword" placeholder="Password" v-model="pwd">
        <label for="floatingPassword">Password</label>
        <div class="invalid-tooltip">
          Need at least 8 characters(No special symbol).
        </div>
      </div>
      <br>
      <br>
      <button class="w-100 btn btn-lg btn-primary" @click="signIn" type="button">Sign in</button>
    </form>
  </div>
</template>

<script>

import {useStore} from 'vuex'
import {useRouter} from "vue-router";
import {ref, watch} from 'vue';
import axios from "axios";

export default {
  name: "login",
  setup() {

    const store = useStore();
    const router = useRouter();
    const telPattern = /^[0-9]{11}$/;
    const pwdPattern = /^[a-zA-Z0-9]{8,16}$/;

    let tel = ref("");
    let pwd = ref("");
    let wrong = ref(false);
    let telCls = ref({
      'form-control':true,
      'is-invalid':false
    })
    let pwdCls = ref({
      'form-control':true,
      'is-invalid':false
    })

    watch([tel,pwd],()=>{
      if(tel.value !== "") telCls.value['is-invalid'] = !telPattern.test(tel.value);
      if(pwd.value !== "") pwdCls.value['is-invalid'] = !pwdPattern.test(pwd.value);
    })


    const sendRequest = () => {
      axios({
        method: "POST",
        url: "LogInServlet",
        params: {
          tel: tel.value,
          pwd: pwd.value,
        }
      }).then((res) => {
        if (res.data[0]) {
          store.dispatch("SignIn", res.data[1]);
          router.push("/movie");
        } else {
          pwd.value = "";
          wrong.value = true;
        }
        store.dispatch("Finished");
      });
    }

    const signIn = () => {
      store.dispatch("Load")
      if (!telPattern.test(tel.value) || !pwdPattern.test(pwd.value)) {
        store.dispatch("Finished");
      }
      else sendRequest();
    }
    store.dispatch("Finished");

    return {
      tel,
      pwd,
      wrong,
      telCls,
      pwdCls,
      signIn
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

.form-signin {
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