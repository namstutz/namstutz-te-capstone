<template>
  <form id="register-form" autocomplete="off" @submit.prevent="register">
    <div class="alert-msg" role="alert" v-if="registrationErrors">
      {{ registrationErrorMsg }}
    </div>
    <div class="form-input-group" v-if="chooseRole">
      <input type="radio" id="child-radio" value="Child" v-model="user.role" :disabled="disableForm">
      <label for="child-radio">Child</label>
      <input type="radio" id="parent-radio" value="Parent" v-model="user.role" :disabled="disableForm">
      <label for="parent-radio">Parent</label>
    </div>
    <div class="text-input-block">
      <div class="form-input-group">
        <label for="username">Username</label>
        <input type="text" id="username" v-model="user.username" required ref="usernameInput" :disabled="disableForm" />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required autocomplete="new-password" :disabled="disableForm" />
      </div>
      <div class="form-input-group">
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" v-model="user.confirmPassword" required ref="confirmPasswordInput" :disabled="disableForm" />
      </div>
    </div>
    <button type="submit" :disabled="disableForm">Create Account</button>
  </form>
</template>

<script>
import authService from '../services/AuthService';

export default {
  name: 'register-form',
  props: ['role', 'familyId', 'chooseRole'],
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: this.role,
        familyId: this.familyId
      },
      disableForm: false,
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
        this.user.confirmPassword = '';
        this.$refs.confirmPasswordInput.focus();
      } else {
        this.clearErrors();
        this.disableForm = true;
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              //Clear form for next entry
              this.user = {
                username: '',
                password: '',
                confirmPassword: '',
                role: this.role,
                familyId: this.familyId
              };
              this.disableForm = false;
              this.$emit('create-user');
            }
          })
          .catch((error) => {
            this.disableForm = false;
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
  mounted() {
    this.$refs.usernameInput.focus();
  }
};

</script>

<style scoped>
#register-form {
  margin-top: 1em;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
.alert-msg {
  color: firebrick;
  margin-bottom: 1em;
}
.text-input-block {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
.form-input-group {
  display: flex;
  margin-bottom: 1rem;
  
}
label {
  margin-right: 0.5rem;
}
button {
  display: flex;
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
}
button:hover {
  background-color: #1590a3;
}
</style>