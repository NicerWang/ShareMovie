<template>
  <main class="form-signin">
    <div class="alert alert-danger" role="alert" v-show="wrong">
      Invalid input or duplicated username!
    </div>
    <form>
      <br>
      <br>
      <img class="mb-4" src="../assets/register.svg" alt="" width="72" height="57">
      <h1 class="h3 mb-3 fw-normal">Register for
        <br> Share Movie</h1>
      <div class="form-floating">
        <input type="tel" :class="telCls" id="floatingInput" placeholder=" " v-model="tel">
        <label for="floatingInput">Telephone</label>
        <div class="invalid-tooltip">
          Need exactly 11 numbers.
        </div>
      </div>
      <div class="form-floating">
        <input type="tel" :class="nameCls" id="floatingName" placeholder=" " v-model="name">
        <label for="floatingName">Name</label>
        <div class="invalid-tooltip">
          Need at least 3 characters(No special symbol).
        </div>
      </div>
      <div class="form-floating">
        <input type="password" :class="pwdCls" id="floatingPassword" placeholder=" " v-model="pwd">
        <label for="floatingPassword">Password</label>
        <div class="invalid-tooltip">
          Need at least 8 characters(No special symbol).
        </div>
      </div>
      <br>
      <br>
      <button type="button" class="w-100 btn btn-lg btn-primary" @click="reg">Submit</button>
    </form>
  </main>
</template>

<script>
import axios from "axios";
import {ref, watch} from 'vue';
import {useRouter} from "vue-router";
import {useStore} from "vuex";

export default {
  name: "register",
  setup(){

    const store = useStore();
    const router = useRouter();
    const telPattern = /^[0-9]{11}$/;
    const namePattern = /^[a-zA-Z0-9]{3,}$/;
    const pwdPattern = /^[a-zA-Z0-9]{8,16}$/;

    let name = ref("");
    let tel = ref("");
    let pwd = ref("");
    let wrong = ref(false);

    let telCls = ref({
      'form-control':true,
      'is-invalid':false
    });
    let pwdCls = ref({
      'form-control':true,
      'is-invalid':false
    });
    let nameCls = ref({
      'form-control':true,
      'is-invalid':false
    });


    watch([tel,name,pwd],()=>{
      if(tel.value !== "") telCls.value['is-invalid'] = !telPattern.test(tel.value);
      if(name.value !== "") nameCls.value['is-invalid'] = !namePattern.test(name.value);
      if(pwd.value !== "") pwdCls.value['is-invalid'] = !pwdPattern.test(pwd.value);
    })

    const reg = ()=>{
      store.dispatch("Load")
      if(!namePattern.test(name.value) || !telPattern.test(tel.value) || !pwdPattern.test(pwd.value)){
        store.dispatch("Finished");
        wrong.value = true;
      }else{
        axios({
          async:false,
          url:"AddUserServlet",
          params:{
            name:name.value,
            tel:tel.value,
            pwd:pwd.value
          }
        }).then((res)=>{
          if(res.data['status'] === true){
            router.replace("/login")
          }else{
            wrong.value = true;
          }
          store.dispatch("Finished");
        })
      }
    };

    store.dispatch("Finished");
    return{
      name,
      tel,
      pwd,
      telCls,
      nameCls,
      pwdCls,
      reg,
      wrong
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