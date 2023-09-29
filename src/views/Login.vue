<template>
  <main id="login">
    <div class="box">
      <h1>Family Reading Tracker</h1>
    </div>
    <form id="login-form" @submit.prevent="login">
      <h2>Please Log In</h2>
      <div class="alert-error" role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
      <div class="alert-success" role="alert" v-if="$route.query.registration">
        Thank you for registering, please sign in.
      </div>
      <div class="text-input-block">
        <div class="form-input-group">
          <label for="username">Username</label>
          <input
            type="text"
            id="username"
            v-model="user.username"
            required
            autofocus
          />
        </div>
        <div class="form-input-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            v-model="user.password"
            required
          />
        </div>
      </div>
      <button type="submit">Sign in</button>
      <p>
        <router-link class="register-new-account" :to="{ name: 'register' }"
          >Need an account? Click here!</router-link>
      </p>
         <div class="kids-set-up">(Kids, get your parents to set things up!)</div>
      
    </form>
  </main>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "login",
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            if (response.data.user.authorities[0].name == "ROLE_CHILD") {
              this.$router.push("/books");
            } else if (
              response.data.user.authorities[0].name == "ROLE_PARENT") {
              this.$router.push("/family");
            }
          }
        })
        .catch((error) => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    },
  },
};
</script>

<style scoped>
#login-form {
margin-top: 0.5em;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-items: flex-end;
  font-weight: bold;
}
.alert-error {
  color: firebrick;
  margin-bottom: 1em;
}
.alert-success {
  color: navy;
  margin-bottom: 1em;
}
h2 {
  margin-top: 1em;
  padding: 0.4em;
  padding-left: 3em;
  padding-right: 3em;
  border-radius: 0.2em;
  background-color: #8be1f5;
  font-weight: 800;
  background-image: linear-gradient(#05BCD9, #9dd7e2);
  color: white;
}
.form-input-group {
  display: flex;
  margin-bottom: 1rem;
  align-items: center;
}
.text-input-block {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
label {
  margin-right: 0.5rem;
}
button {
  display: flex;
  text-align: flex-end;
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  padding: 0.3em;
  padding-left: 2em;
  padding-right: 2em;
  border: 2px solid black;
  border-radius: 8px;
  background-color: #31b4c9;
  font-weight: bold;
  color: white;
}
button:hover {
  background-color: #1590a3;
}
.register-new-account {
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