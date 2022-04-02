<template>
  <el-card>
    <h1 class="me-author-name">Whut-SE</h1>
    <div class="me-author-description">
      <span><i class="el-icon-location-outline"></i> &nbsp;武汉</span>
      <span><i class="me-icon-job"></i> &nbsp;心疼giegie</span>
    </div>

  </el-card>

</template>

<script>
  export default {
    name: 'CardMe',
    data() {
      return {

      }
    },
    methods: {
    }
  }
</script>

<style scoped>
  .me-author-name {
    text-align: center;
    font-size: 30px;
    border-bottom: 1px solid #5FB878;
  }

  .me-author-description {
    padding: 8px 0;
  }

  .me-icon-job {
    padding-left: 16px;
  }

  .me-author-tool {
    text-align: center;
    padding-top: 10px;
  }

  .me-author-tool i {
    cursor: pointer;
    padding: 4px 10px;
    font-size: 30px;
  }
</style>
