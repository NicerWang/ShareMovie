<template>
  <nav>
    <ul class="pagination pagination-lg justify-content-center">
      <li :class="prevCls">
        <a class="page-link" @click="previous">&lt;&nbsp;Previous</a>
      </li>
      <li id="page-num" class="page-item disabled">
        <a class="page-link">{{ nowPage }}</a>
      </li>
      <li :class="nextCls">
        <a class="page-link" @click="next">Next&nbsp;&gt;</a>
      </li>
    </ul>
  </nav>
</template>

<script>
import {ref, watch} from "vue";

export default {
  name: "pagination",
  props:{
    max:Number,
    now:Number,
  },
  setup(props,context){
    let nowPage = ref(1);
    let prevCls = ref({
      'page-item':true,
      'disabled':false
    })
    let nextCls = ref({
      'page-item':true,
      'disabled':false
    });
    watch(props,()=>{

      nextCls.value['disabled'] = props.max === props.now;
      prevCls.value['disabled'] = (1 === props.now);
      nowPage.value = props.now;
    })

    const next = function () {
      context.emit("next");
    }
    const previous = function () {
      context.emit("prev");
    }
    return{
      nowPage,
      prevCls,
      nextCls,
      previous,
      next,
    }
  }
}
</script>

<style scoped>
ul{
  margin-bottom: 60px;
}
li{
  margin-left: 5%;
  margin-right: 5%;
}
li:hover{
  cursor: pointer;
}
#page-num{
  cursor: default;
}
</style>