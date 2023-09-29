<template>
  <footer>
    <nav v-if="$store.state.token != ''">
      <ul>
        <router-link v-bind:to="{ name: 'bookRecommended' }">
          <li>
          📖
          </li>
        </router-link>
        
        <router-link v-bind:to="{ name: 'books' }">
          <li>
            📚
          </li>
        </router-link>

        <router-link v-bind:to="{ name: 'activity' }">
          <li>
            📋
          </li>
        </router-link>

        <router-link v-bind:to="{ name: 'prizes' }">
          <li>
            🏆
          </li>
        </router-link>
          
        <router-link v-if="parentLoggedIn" v-bind:to="{ name: 'family' }">
          <li>
            👨‍👩‍👧‍👦
          </li>
        </router-link>

        <router-link v-bind:to="{ name: 'logout' }">
          <li>
            🔚
          </li>
        </router-link>
        
      </ul>
    </nav>
  </footer>
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

footer{
  display: none;
}
@media only screen and (max-width: 768px) {
  footer {
    list-style-type: none;
    display: flex;
    align-items: center;
    flex-direction: row;
    background-image: linear-gradient(var(--dark-blue),var(--light-blue));
    position: fixed;
    bottom: 0em;
    margin-top: 0.5em;
    left: 0em;
    right: 0em;
    justify-content: space-around;
    padding-right: 2.5em;
  }

  ul {
    line-height: 2em;
    list-style-type: none;
    display: flex;
    justify-content: space-evenly;
  }

  nav {
    flex-grow: 1;
  }

  li {
    background-color: rgb(184, 237, 250);
    border-radius: 1em;
    padding-left: 0.6em;
    padding-right: 0.6em;
    padding-top: 0.1em;
    padding-bottom: 0.1em;
  }
  a:link {
    font-size: 1.5em;
    text-decoration: none;
  }

  a:visited {
    font-size: 1.5em;
    text-decoration: none;
  }

  a:hover {
    font-size: 1.5em;
    text-decoration: none;
  }

  a:active {
    font-size: 1.5em;
    text-decoration: none;
  }
}
</style>