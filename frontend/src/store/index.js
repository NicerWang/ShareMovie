import {createStore} from 'vuex'

const state = sessionStorage.getItem('state') ? JSON.parse(sessionStorage.getItem('state')) : {
  isLoading:false,
  isSignedIn:false,
  isSu:false
}

const store = createStore({
  state:state,
  mutations:{
    SIGNIN(state,isSu){
      state.isSignedIn = true;
      state.isSu = isSu;
    },
    LOGOUT(state){
      state.isSignedIn = false;
      state.isSu = false;
    },
    FINISHED(state){
      state.isLoading = false;
    },
    LOAD(state){
      state.isLoading = true;
    }
  },
  actions:{
    SignIn({commit},state,isSu){
      commit("SIGNIN",state,isSu);
    },
    LogOut({commit},state){
      commit("LOGOUT",state);
    },
    Finished({commit},state){
      commit("FINISHED",state);
    },
    Load({commit},state){
      commit("LOAD",state);
    }
  }

});
export default store;