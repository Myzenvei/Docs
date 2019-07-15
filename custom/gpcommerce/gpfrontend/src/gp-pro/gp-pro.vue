<template>
  <div id="gp-pro">
    <!-- <img src="./assets/logo.png"> -->
    <router-link>Login</router-link>
    <router-link to="/registration">Registration</router-link>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'gp-pro',
};
</script>

<style scoped>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
