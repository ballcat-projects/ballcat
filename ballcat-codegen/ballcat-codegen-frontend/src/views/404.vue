<template>
  <a-result status="404" title="404" sub-title="Sorry, the page you visited does not exist.">
    <template #extra>
      <a-button type="primary" @click="handleToHome">
        Back Home
      </a-button>
    </template>
  </a-result>
</template>
<script>
export default {
  data() {
    return {}
  },
  methods: {
    handleToHome() {
      this.$router.push({ path: '/' })
    }
  }
}
</script>
