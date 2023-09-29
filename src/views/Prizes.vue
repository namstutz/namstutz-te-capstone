<template>
  <div class="prizes">
     <prizes-display/>
  </div>
</template>

<script>
import PrizesDisplay from '../components/PrizesDisplay.vue';

export default {
  components: { 
    PrizesDisplay,
    },

  name: "prizes",
  data() {
    return {
  
    };
  },
   created() {
    this.$store.dispatch('retrievePrizes');
  },
};
</script>

<style scoped>

</style>