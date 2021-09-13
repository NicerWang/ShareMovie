import { createApp } from 'vue'

import store from "./store";
import router from "./router";
import axios from "axios";
import "bootstrap";
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import "animate.css"
import "echarts-wordcloud"

axios.defaults.baseURL = "/api/";
// axios.defaults.baseURL = "http://localhost:8080/"
axios.defaults.withCredentials=true;

import App from './App.vue'

createApp(App).use(store).use(router).mount('#app')
