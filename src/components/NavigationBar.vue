<template>
  <header>
    <img src="../assets/valley-libraries-logo.png" />
    <nav v-if="$store.state.token != ''">
      <ul class="topNavContainer">
        <router-link v-bind:to="{ name: 'bookRecommended' }">
          <li>Read!</li>
        </router-link>

        <router-link v-bind:to="{ name: 'books' }">
          <li>Books</li>
        </router-link>

        <router-link v-bind:to="{ name: 'activity' }">
          <li>Activity</li>
        </router-link>

        <router-link v-bind:to="{ name: 'prizes' }">
          <li>Prizes</li>
        </router-link>

        <router-link v-if="parentLoggedIn" v-bind:to="{ name: 'family' }">
          <li>Family</li>
        </router-link>

        <router-link v-bind:to="{ name: 'logout' }">
          <li>Logout</li>
        </router-link>
      </ul>
    </nav>
  </header>
</template>

<script>
export default {
  computed: {
    parentLoggedIn() {
      return (
        this.$store.state.user.authorities[0].name == "ROLE_PARENT"
      );
    },
  },
};
</script>

<style scoped>
img {
  height: 5em;
  display: block;
  padding: 1em;
  margin: 1.2em;
  margin-bottom: .5em;
}

img:hover {
  box-shadow: 0 0 2px 1px rgba(0, 140, 186, 0.5);
}

header {
  display: flex;
  flex: 1;
  align-items: center;
  flex-direction: row;
  justify-content: center;
}

.topNavContainer {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  
}


nav {
  display: flex;
  flex-direction: row;
  flex: 1;
  justify-content: flex-end;
}

ul {
  list-style-type: none;
}

li {
  padding: 0.3em;
  font-weight: bold;
  border: 2px solid #9DDAE6;
  border-radius: 4px 1px;
  margin: .1em;
  background-color: #9DDAE6;
}

a {
  color: black;
  text-decoration: none;
}


li:hover {
  transform: scale(1.1);
  border: 2px solid black;
  box-shadow: 3px 3px rgb(139, 139, 139);
  color: white;
  background-color: black;
  
}

li:active {
  background-color: #05BCD9;
}

@media only screen and (max-width: 768px) {
  header {
    display: flex;
    align-content: center;
    justify-content: center;
  }
}
@media only screen and (max-width: 768px) {
  nav {
    display: none;
  }
}
</style>