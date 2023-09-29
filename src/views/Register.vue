<template>
  <main id="register" class="text-center">
    <div class="box">
      <h1>Create Account</h1>
    </div>
    <register-form class="register-form" :role="'Parent'" :familyId="0" :chooseRole="false" @create-user="onCreateUser()" />
    <p><router-link class="log-in" :to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
  </main>
</template>

<script>
import RegisterForm from '../components/RegisterForm.vue';


export default {
  components: { 
    RegisterForm,
    },
  name: 'register',
  methods: {
    onCreateUser() {
      this.$router.push({
                path: '/login',
                query: { registration: 'success' },
              });
    }
  }
};
</script>

<style scoped>
.register-form {
margin-top: 0.5em;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-items: flex-end;
  font-weight: bold;
}
.log-in {
display: flex;
  text-align: flex-end;
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  padding: 0.3em;
  padding-left: 0.5em;
  padding-right: 0.5em;
  border: 2px solid black;
  border-radius: 8px;
  background-color: #31b4c9;
  font-weight: bold;
  color: white;
  text-decoration: none;
}
.register-new-account:hover {
  background-color: #1590a3;
}
.kids-set-up {
  color: #1590a3;
}

</style>
