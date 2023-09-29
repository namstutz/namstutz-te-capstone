<template>
  <div class="login-display">
    <span class="welcome-message">
      Welcome, {{ $store.state.user.username }}
    </span>
    <span class="switch-view-message" v-if="$store.state.user.authorities[0].name == 'ROLE_PARENT'">
      &nbsp;- View Information For: 
      <select id="switch-view" v-model="targetUser" @change="switchView()">
        <option disabled :value="undefined">Family Member</option>
        <option v-for="user in $store.state.familyUsers" :key="user.username" :value="user.username">
          {{ user.username }}
        </option>
      </select>
    </span>
  </div>
</template>

<script>
export default {
  name: 'login-display',
  computed: {
    targetUser: {
      get() {
        return this.$store.state.viewTargetUser.username;
      },
      set(username) {
        this.$store.commit('SET_VIEW_TARGET_USER', username);
      }
    }
  },
  methods: {
    switchView() {
      this.$store.dispatch('retrieveBooks');
      this.$store.dispatch('retrieveSessions');
      this.$store.dispatch('retrievePrizes');
      this.$emit('switch-user-view');
    }
  },
  created() {
    if (this.$store.state.user.authorities[0].name == 'ROLE_PARENT' && this.$store.state.familyUsers.length == 0) {
      this.$store.dispatch('retrieveFamilyUsers');
    }
  }
}
</script>

<style scoped>
.login-display {
  margin-bottom: 1em;
  font-weight: bold;
  color: #0799af;
}

</style>