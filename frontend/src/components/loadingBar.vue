<template>
  <div class="progress">
    <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuemin="0"
         aria-valuemax="100" :style="{width:status + '%'}"></div>
  </div>
</template>

<script>
export default {
  name: "loadingBar",
  props: {
    status: Number
  }
}
</script>
