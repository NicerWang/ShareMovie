<template>
    <header class="d-flex flex-wrap justify-content-center py-3  border-bottom">
      <a href="/" class="d-flex align-items-center mb-0  me-auto text-dark text-decoration-none">
        &nbsp;&nbsp;&nbsp;
        <img src="../assets/icon.svg" alt="">
        <span class="fs-4">&nbsp;ShareMovie</span>
      </a>
      <ul class="nav nav-pills">
        <li class="nav-item" v-for="(item,idx) in items_before"><router-link :to="item.to" :class="item.cls">{{ item.msg }}</router-link></li>

        <li class="nav-item"><button type="button" class="btn btn-danger" @click="logout" v-show="status">Logout</button></li>
        &nbsp;
      </ul>
    </header>
</template>

<script>

import {ref, reactive, computed} from 'vue'
import {useStore} from 'vuex'
import {useRouter} from "vue-router";
import axios from "axios";

export default {
  name: "navbar",
  setup(){
    const store = useStore();
    const router = useRouter();
    const activated = ["nav-link","active"]
    const common = ["nav-link"]
    let status = computed(()=>{
      return store.state.isSignedIn;
    })
    let items_before = ref([
      reactive({to:"/login",msg:"Sign in",cls:activated}),
      reactive({to:"/register",msg:"Register",cls: common}),
    ])
    if(status.value === true){
      items_before = ref([
        reactive({to:"/movie",msg:"Movie",cls:activated}),
        reactive({to:"/update",msg:"UpdateInfo",cls: common}),
      ])
    }
    const logout = ()=>{
      axios({
        url:"LogOutServlet",
        async:true
      }).then(()=>{
        store.dispatch("LogOut");
        store.dispatch("Finished")
        router.push("/login");
      })
    }
    return{
      items_before,
      activated,
      common,
      status,
      logout
    }
  },
  watch:{
    $route(to,from){
      for(let i = 0; i < this.items_before.length; i++){
        this.items_before[i].cls = this.common;
        if(to.fullPath.search(this.items_before[i].to) !== -1){
          this.items_before[i].cls = this.activated;
        }
      }
    },
    "$store.state.isSignedIn"(newVal,oldVal){
      if(newVal === oldVal){}
      else if(newVal){
        this.items_before = ref([
          reactive({to:"/movie",msg:"Movie",cls:this.activated}),
          reactive({to:"/update",msg:"UpdateInfo",cls: this.common})
        ])
      }else{
        this.items_before = ref([
          reactive({to:"/login",msg:"Sign in",cls:this.activated}),
          reactive({to:"/register",msg:"Register",cls: this.common}),
        ])
      }
    }

  },
}
</script>
<style scoped>
li{
  height: 40px;
  margin: 2px;
}
button{
  height: 40px;
}

</style>