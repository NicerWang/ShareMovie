import { createRouter, createWebHashHistory , createWebHistory} from 'vue-router'
const Login = ()=>import("../components/login.vue")
const Movie = ()=>import("../components/movie.vue")
const UpdateInfo = ()=>import("../components/updateInfo.vue")
const Register = ()=>import("../components/register.vue")
const Detail = ()=>import("../components/detail.vue")
const User =  ()=>import("../components/admin/userControl.vue")
const Automate = ()=>import("../components/admin/automate.vue")
const Admin = ()=>import("../components/admin/adminPanel.vue")
const MovieCtl = ()=>import("../components/admin/movieControl.vue")
import store from "../store";


const routes = [
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
  },
  {
    path: '/admin/u',
    name: 'User',
    component: User
  },
  {
    path: '/admin/m',
    name: 'MovieControl',
    component: MovieCtl
  },
  {
    path: "/",
    redirect: "/login"
  },
  {
    path: "/admin/a",
    name: "Automate",
    component: Automate
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/movie',
    redirect: '/movie/1',
  },
  {
    path: '/movie/:page',
    name: 'Movie',
    component: Movie
  },
  {
    path: '/detail/:movie',
    name: 'Detail',
    component: Detail
  },
  {
    path: '/update',
    name: 'UpdateInfo',
    component: UpdateInfo
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})
router.beforeEach((to,from,next)=>{
  if(to.fullPath === "/login" && store.state.isSignedIn) {
    router.push("/movie");
    return;
  }
  if(to.fullPath.search("detail|movie|logout|update") !== -1){
    if(store.state.isSignedIn){
      store.dispatch("Load");
      next();
    }
    else{
      router.push("/login");
    }
  }
  else if(to.fullPath.search("register|login") !== -1){
    if(!store.state.isSignedIn){
      store.dispatch("Load");
      next();
    }
    else{
      router.push("/movie");
    }
  }else if(to.fullPath.search("admin") !== -1){
    if(store.state.isSu){
      store.dispatch("Load");
      next();
    }
    else{
      router.push("/");
    }
  }else{
    next(false);
  }
})

export default router