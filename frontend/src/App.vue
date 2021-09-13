<template>
  <navbar v-show="$route.fullPath.search('login') === -1"></navbar>
  <loading :status="status" v-show="isLoading" v-bind:align="isLoading"></loading>
  <router-view v-slot="{ Component }" v-show="!isLoading">
    <transition name="fade" mode="out-in">
      <component :is="Component"/>
    </transition>
  </router-view>
</template>

<script>
import navbar from "./components/navBar.vue";
import loading from "./components/loadingBar.vue";
import {computed, onMounted, ref} from "vue";
import {useStore} from "vuex";

export default {
  components: {
    navbar,
    loading,
  },
  setup() {
    let status = ref(100);
    const store = useStore();
    let isLoading = computed(() => {
      return store.state.isLoading;
    })
    const saveState = () => {
      sessionStorage.setItem('state', JSON.stringify(store.state))
    }
    onMounted(() => window.addEventListener('unload', saveState))
    return {
      status,
      isLoading
    }
  },
}

</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

* {
  user-select: none
}

body {
  background-color: #f8f9fa;
}

header {
  background-color: #ffffff;
}
</style>
