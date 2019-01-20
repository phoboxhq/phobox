<template>
  <bar-chart v-if="chartdata.loaded" :chart-data="chartdata" />
</template>

<script>
import BarChart from './basic-charts/BarChart.js'

export default {
  name: "ImagesByTimeChart",
  props: ['selectedYear'],
  components: {
    BarChart,
  },
  data() {
    return {
      SERVER_PATH: process.env.SERVER_PATH,

      chartdata: {
        loaded: false,
        labels: this.$t('statistics.month_names'),
        datasets: [
        ]
      },
    };
  },

  watch: {
    selectedYear() {
      this.fetchImagesPerPeriod();
    }
  },

  mounted () {
    this.fetchImagesPerPeriod();
  },
  
  methods: {

    fetchImagesPerPeriod() {
        fetch(this.SERVER_PATH+"api/statistics/year/" + this.selectedYear)
            .then(response => response.json())
            .then(response => {

                let datasets = response.reduce((accumulator, currentValue) => {
                    let existing = accumulator.find(item => item.label === currentValue.camera);
          
                    if(!existing) {
                      existing = {
                        label: currentValue.camera,
                        map: {1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0 ,8:0 ,9:0, 10:0, 11:0, 12:0}
                      }
                      accumulator.push(existing);
                    }
          
                    existing.map[currentValue.month] = currentValue.count;
                    return accumulator;
                  }, []);
          
                  datasets.forEach((item, index) => {
                    item.data = Object.values(item.map);
                  })
          
                  this.$set(this.chartdata, 'datasets', datasets);
                  this.chartdata.loaded = true;
            });
        }
    }
};
</script>