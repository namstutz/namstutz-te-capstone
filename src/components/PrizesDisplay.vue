<template>
  <main class="prize-add-form">
    <login-display/>
    <div class="box">
     <h1>Prizes</h1>
    </div>
    <div v-if="parentLoggedIn">
      <button @click="toggleForm()">
        {{ showAddForm ? 'Cancel' : 'Add Prize' }}
      </button>
    </div>
    <add-prize-form v-if="showAddForm" 
      :editPrize="editPrize"
      @create-prize="prizeSubmitted()" 
    />
    <div class="prize-display" >
      <div v-for="prize in this.$store.state.prizes" v-bind:key="prize.id" class="prize-panel">
        <div class="prize-panel-inActive" :class="{ completed: prize.completed, 'Active': prize.currentlyActive }">
        <div class="prize-name">
          {{ prize.prizeName }}
        </div>
        <div class="prize-description">
          {{ prize.description }}
        </div>
        <div class="prize-requirements">
        <div class="prize-user-group">{{ prizeEligibility(prize) }} Prize</div>
        <div class="prize-milestone">
          <section class="label">Minutes to read:</section> {{ prize.milestone}}
        </div>
        
        <div class="prize-maximum">
          <section class="label">Prizes Remaining:</section>{{ prize.maxPrizes == 0? "Unlimited" : Math.max(prize.maxPrizes - prize.claimedPrizes, 0) + ' of ' + prize.maxPrizes }}
        </div>
      
        <div class="prize-start-date">
          <section class="label">Start:</section> 
            {{ timeStampDate(prize.startDate) }}
        </div>
        <div class="prize-end-date">
          <section class="label">End:</section>  
          {{ timeStampDate(prize.endDate) }}
        </div>
        </div> 
        <div>  
          <section class="label">Progress</section>
        </div>
        <div class="progress-bar">
          <div class="progress-color" :style="{width: progressPercent(prize.progressMinutes,prize.milestone)+'%'}">
            {{ progressPercent(prize.progressMinutes,prize.milestone) }}%
          </div> 
        </div>
        <div class="prize-completed" v-if="prize.completed">
        <section class="label">Completed on {{ timeStampDate(prize.completionDate) }}! 🎉</section>
        </div>
        <div class="edit-buttons" v-if="parentLoggedIn">  
          <button @click="openUpdateForm(prize)" :disabled="showAddForm">Edit</button>
          <button v-if="prize.expired" v-on:click="deletePrize(prize.id)" :disabled="showAddForm">Delete</button> 
        </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import PrizesService from '../services/PrizesService';
import AddPrizeForm from './AddPrizeForm.vue';
import LoginDisplay from './LoginDisplay.vue';

export default {
  data() {
    return {
      showAddForm: false,
      editPrize: {}
    };
  },
  computed: {
    parentLoggedIn() {
      return (
        this.$store.state.user.authorities[0].name == "ROLE_PARENT"
      );
    }
  },
  name: "prizes-form",
  methods: {
    toggleForm() {
      this.showAddForm = !this.showAddForm
      if (!this.showAddForm) {
        this.editPrize = {};
      }
    },
    prizeEligibility( prize ) {
      let str = "";
      if (prize.forChildren) {
        str += "Child ";
      }
      if (prize.forChildren && prize.forParents) {
        str += "and ";
      }
      if (prize.forParents) {
        str += "Parent ";
      }
      return str;
    },
    timeStampDate( timestamp ) {
      if(timestamp != undefined){
      const parts = timestamp.split(/[T .]/);
      return parts[0];
      }
    },
    progressPercent(minutes,total) {
      return Math.min(Math.floor((minutes/total)*(100)),100);
    },
    prizeSubmitted() {
      this.$store.dispatch('retrievePrizes');
      this.showAddForm = false;
      this.editPrize = {};
    },
    openUpdateForm(prize) {
      if (!this.showAddForm) {
        this.editPrize = prize;
        this.editPrize.startDate = this.timeStampDate(this.editPrize.startDate);
        this.editPrize.endDate = this.timeStampDate(this.editPrize.endDate);
        this.showAddForm = true;
      }
    },
    deletePrize(id){
      if (confirm('Are you sure you want to delete this prize?')) {
        PrizesService.deletePrize(id)
        .then(response => {
          if(response.status === 200) {
            this.$store.commit("DELETE_PRIZE", id);
          }
        });
      }
    },
    
  },
  components: {
    AddPrizeForm,
    LoginDisplay
  }
}
</script>

<style scoped>
button {
  margin-top: 1em;
  margin-bottom: 1em;
  margin: .2em;
  padding: 0.3em;
  padding-left: 1.5em;
  padding-right: 1.5em;
  border: 2px outset black;
  border-radius: 8px;
  background-color:#05BCD9;
  font-weight: 800;
  color: white;
}
button:hover {
  background-color: #1590a3;
  border: 2px inset black;
  
}
button:disabled {
  background-color: gray;

}

.prize-display {
  margin-top: .5em;
  display: flex;
  justify-content: space-evenly;
  flex-wrap: wrap;
  margin-bottom: 3em;
}


.prize-panel-inActive {
  margin: 0.4em;
  border: 8px solid #676867;
  border-radius: 18px;
  background-color: grey;
  padding: .4em;
  width: 15em;
  height: auto;
  text-align: center; 
}


.Active {
  border: 8px outset #05BCD9;
  background-image: linear-gradient(#05BCD9,#9DDAE6);
}

.completed {
  border: 8px solid #16b14a;
  background-image: linear-gradient(#16b14a,#9de6bb);
}
 

.prize-panel-inActive:hover {
  box-shadow: 4px 4px #cfcfcf;
  transform: scale(1.05);
}

.prize-name{
  font-size: 1.5em;
  font-weight: bold;
  text-align: center;
  border-bottom: 2px solid black;
  margin: .5em;
  color: white;
  padding-bottom: .3em;
}

.prize-description {
  font-size: 1em;
  text-align: center;
  font-weight: bold;
  margin-bottom: .8em;
  background-color: white;
  padding: .3em;
  border: 2px solid #b6b6b6;
  border-radius: 8px;
}

.label {
  font-size: 0.9em;
  font-weight: bold;
  text-align: center;
  padding-right: .3em;
}
.prize-user-group{
  display:flex;
  padding: .3em;
  margin-bottom: .3em;
  font-weight: bold;
}

.prize-maximum{
  display:flex;
  padding: .3em;
  margin-bottom: .3em;
}

.prize-milestone {
  display:flex;
  padding: .3em;
  margin-bottom: .3em;
}

.prize-start-date {
  display: flex;
  padding: .3em;
  margin-bottom: .3em;
}

.prize-end-date {
  display: flex;
  padding: .3em;
  margin-bottom: .3em;
}

.prize-completed {
  display: flex;
  padding: .3em;
  background-color: white;
  border: 1px solid grey;
  margin-bottom: .3em;
  border-radius: 8px;
  justify-content: center;
}


.progress-bar {
  width: 100%;
  background-color: rgb(155, 154, 154);
  border: 2px inset rgb(153, 151, 151);
  border-radius: 8px;
}

.progress-color {
  width: 0%; 
  background-color: #2408be;
  color: white;
  border-radius: 8px;
}

.edit-buttons {
  margin: 1em;
  display: flex;
  justify-content: center;
}

.prize-requirements {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  padding: .3em;
  background-color: white;
  border: 2px solid rgb(182, 180, 180);
  margin-bottom: .3em;
  border-radius: 8px;
}

</style>