<template>
  <div id="app">
  <navigation-bar/>
    <router-view />
    <navigation-footer/>
  </div>
</template>
<script>
  import NavigationBar from "./components/NavigationBar.vue";
  import NavigationFooter from './components/NavigationFooter.vue';

  export default {
    components: {
      NavigationBar,
      NavigationFooter

    }
  }

</script>


<style>
:root {
  --dark-blue: #05BCD9; 
  --light-blue: #9DDAE6; 
}

.box { 
    color: white;
    background-image: linear-gradient(var(--dark-blue),var(--light-blue));
    width: 100vw;
    text-align: center;

}

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
}

main {
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow: hidden;
  margin-bottom: 3.5em;
}

@media only screen and (max-width: 768px) {
.box { 
    color: white;
    background-image: linear-gradient(var(--dark-blue),var(--light-blue));
    width: 100vw;
    text-align: center;
}
}
</style>