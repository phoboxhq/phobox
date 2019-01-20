<template>
  <pie-chart v-if="taggingData.loaded" :datasets="taggingData"></pie-chart>
</template>

<script>
import PieChart from './basic-charts/PieChart.js'

export default {
  name: "TaggingStateChart",
  components: {
    PieChart,
  },
  data() {
    return {
      SERVER_PATH: process.env.SERVER_PATH,

      taggingData: {
        loaded: false,
        datasets: [{
          data: [10, 30],
          backgroundColor: ['#BDE57C', '#EB6969'],
          hoverBackgroundColor: ['#ACD171', '#EA5B5B'],

          borderColor: ['#00000000', '#00000000'],
          hoverBorderColor: ['#00000000', '#00000000'],
          
          borderWidth: [5, 5],
          hoverBorderWidth: [5, 5],
        }],
        labels: [this.$t('statistics.tagged'), this.$t('statistics.untagged')]
      }
    };
  },

  mounted () {
    this.fetchTaggingState();
  },
  
  methods: {

    fetchTaggingState() {
      fetch(this.SERVER_PATH+"api/statistics/tagging")
        .then((resp) => resp.json())
        .then((response) => {
          this.$set(this.taggingData.datasets[0].data, 0, response.taggedItems);
          this.$set(this.taggingData.datasets[0].data, 1, response.untaggedItems);
          this.taggingData.loaded = true;
        });
    }

  }
};
</script>