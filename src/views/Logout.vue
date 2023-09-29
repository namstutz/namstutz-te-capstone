<template>
  <main class="logout">
    <h1>Logout</h1>
  </main>
</template>

<script>
export default {
  created() {
    this.$store.commit("LOGOUT");
    this.$router.push("/login");
  }
};
</script>
