<template>
  <main class="family">
    <div class="box">
    <h1>Family Management</h1>
    </div>
    <h2>Logged In as {{ $store.state.user.username }}</h2>
    <h3>Family Members</h3>
    <ul>
      <li v-for="user in $store.state.familyUsers" :key="user.username" @click="clickFamily(user.username)">
        {{ user.username }} - {{ user.authorities[0].name == 'ROLE_PARENT' ? 'Parent' : 'Child' }}
      </li>
    </ul>
    <div>
      <button @click="showAddForm = !showAddForm">{{ showAddForm ? 'Cancel' : 'Add Family Member' }}</button>
    </div>
    <register-form 
      v-if="showAddForm"
      :role="'Child'" 
      :familyId="$store.state.user.familyId" 
      :chooseRole="true"
      @create-user="$store.dispatch('retrieveFamilyUsers')" 
    />
  </main>
</template>

<script>
import RegisterForm from '../components/RegisterForm.vue';

export default {
  components: { 
    RegisterForm
  },
  name: "family",
  data() {
    return {
      showAddForm: false,
    };
  },
  methods: {
    clickFamily(username) {
      this.$store.commit('SET_VIEW_TARGET_USER', username);
      this.$router.push('/activity');
    }
  },
  created() {
    this.$store.dispatch('retrieveFamilyUsers');
  }
};
</script>

<style scoped>
ul {
  display: inline-block;
  text-align: left;
  list-style-type: none;
  padding-inline-start: 0;
}
li {
  padding: .5em;
  margin: .5em;
  border: 1px solid #05BCD9;
  border-radius: 8px;
  background-color: #05BCD9;
  font-weight: bold;
  
}

li:hover {
  background-color: #7cd2e4;
  cursor: pointer;
}


</style>