<template>
  <main class="activity">
    <login-display />
    <div class="box">
      <h1>Activity</h1>
    </div>
    <h2>Books Completed: {{ completedCount }}</h2>
    <h2>Total Reading Minutes: {{ minutesReading }}</h2>
    <sessions-list :sessions="$store.state.sessions" />
  </main>
</template>

<script>
import LoginDisplay from '../components/LoginDisplay.vue';
import SessionsList from "../components/SessionsList.vue";

export default {
  components: { SessionsList, LoginDisplay },
  name: "activity",
  computed: {
    completedCount() {
      return this.$store.state.books.reduce((completions, book) => {
        return book.completed ? completions + 1 : completions;
      }, 0);
    },
    minutesReading() {
      return this.$store.state.sessions.reduce((total, session) => {
        return total + session.minutes;
      }, 0);
    },
  },

  created() {
    this.$store.dispatch("retrieveBooks");
    this.$store.dispatch("retrieveSessions");
  },
};
</script>

<style scoped>
h2 {
  margin-top: 1em;
  padding: 0.6em;
  padding-left: 1.5em;
  padding-right: 1.5em;
  border-radius: 0.2em;
  background-color: #8be1f5;
  font-weight: 800;
  background-image: linear-gradient(#05BCD9, #9dd7e2);
  color: white;
}
</style>