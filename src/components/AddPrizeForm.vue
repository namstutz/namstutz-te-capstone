<template>
  <form id="add-prize-form" autocomplete="off" @submit.prevent="submitPrize">
    <div class="alert-msg" role="alert" v-if="addPrizeErrors">
      {{ addPrizeErrorMsg }}
    </div>
    
    <div class="form-input-group">
      <label for="name">Prize Name</label>
      <input type="text" id="name" v-model="prize.prizeName" ref="prizeName" :disabled="disableForm"/>
    </div>
    <div class="form-input-group">
      <label for="description">Description</label>
      <input type="text" id="description" v-model="prize.description" :disabled="disableForm"/>
      </div>
    <div class="form-input-group center-input">
      <label for="forChildren">For Children</label>
      <input type="checkbox" id="for-children" v-model="prize.forChildren" :disabled="disableForm"/>
    </div>
    <div class="form-input-group center-input">
      <label for="forParents">For Parents</label>
      <input type="checkbox" id="for-parents" v-model="prize.forParents" :disabled="disableForm"/>
    </div>
    <div class="form-input-group">
      <label for="maxPrizes">Max # Prizes</label>
      <input type="number" id="maxPrizes" v-model="prize.maxPrizes" required min="0" step="0" :disabled="disableForm" placeholder="Enter 0 for Unlimited"/>
      </div>
      <div class="form-input-group">
      <label for="milestone">Milestone</label>
      <input type="number" id="milestone" v-model="prize.milestone" required min="1" step="1" :disabled="disableForm"/>
    </div>
    <div class="form-input-group">
      <label for="startDate">Start Date</label>
      <input type="date" id="start-date" v-model="prize.startDate" required :disabled="disableForm"/>
    </div>
    <div class="form-input-group">
      <label for="endDate">End Date</label>
      <input type="date" id="end-date" v-model="prize.endDate" required :disabled="disableForm"/>
    </div>
    <button type="submit" :disabled="disableForm">{{ prize.id == undefined ? 'Submit Prize' : 'Update Prize' }}</button>
  </form>
</template>

<script>
import prizesService from "../services/PrizesService";

export default {
  props: ['editPrize'],
  data() {
    return {
      prize: this.editPrize,
      disableForm: false,
      addPrizeErrors: false,
      addPrizeErrorMsg: "",
    };
  },
  methods: {
    submitPrize() {
      if (this.prize.prizeName === "") {
        this.addPrizeErrors = true;
        this.addPrizeErrorMsg = "Please enter prize name.";
      } else {
        this.addPrizeErrorMsg = "";
        this.addPrizeErrors = false;
        this.disableForm = true;
        if ( this.prize.id == undefined ) {
          prizesService.submitPrize(this.prize).then((response) => {
            if (response.status == 201) {
              //Clear form for next entry
              this.prize = {};
              this.disableForm = false;
              this.$emit('create-prize');
            }
        }).catch(error => {
            if (error.status) {
              this.addPrizeErrors = true;
              this.addPrizeErrorMsg = "Error adding prize.";
            }
          });
        } else {
          prizesService.updatePrize(this.prize).then((response) => {
            if (response.status == 200) {
              this.prize = {};
              this.disableForm = false;
              this.$emit('create-prize');
            }
          }).catch(error => {
            if (error.status) {
              this.addPrizeErrors = true;
              this.addPrizeErrorMsg = "Error updating prize.";
            }
          });
        }
      }
    },
  },
  mounted() {
    this.$refs.prizeName.focus();
  }
};
</script>

<style scoped>
#add-prize-form {
  margin-top: 0.3em;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  font-weight: bold;
}
.form-input-group {
  margin: 0.3em;
  display: flex;
  align-items: center;
  margin-bottom: 0.7em;
  border: black 2px;

}
label {
  margin-right: 0.5em;
  font-size: 0.9em;
}

input {
  font-weight: bold;
}
.center-input {
  align-self: center;
}

button {
  margin-top: 0.5em;
  margin-bottom: 0.5em;
  padding: 0.3em;
  padding-left: 0.6em;
  padding-right: 0.6em;
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