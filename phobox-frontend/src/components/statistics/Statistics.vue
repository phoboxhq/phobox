<template>
	<div id="statistics">
    <h1>{{ $t('statistics.headline') }}</h1>
    <hr >

    <h3>{{ $t('statistics.overview_year') }}</h3>
    <div class="form-inline right_align">
      <input type="number" name="year" class="form-control"
        v-model="selectedYear" min="1826" :max="currentYear">

      <button type="button" class="btn btn-default" @click="onYearChanged">
        {{ $t('statistics.load') }}
      </button>
    </div>

    <bar-chart v-if="loaded" :chart-data="chartdata"></bar-chart>

	</div>
</template>

<script>
import ComService from '@/utils/ComService';
import BarChart from './BarChart.vue'

export default {
  name: "Statistics",
  components: {
    BarChart
  },
  data() {
    return {
      SERVER_PATH: process.env.SERVER_PATH,
      selectedYear: (new Date()).getFullYear(),
      loaded: false,
      chartdata: {
        labels: this.$t('statistics.month_names'),
        datasets: [
        ]
      }
    };
  },

  computed: {
    currentYear() {
      return (new Date()).getFullYear();
    }
  },

  mounted () {
    this.fetchImagesPerPeriod();
  },
  
  methods: {

    onYearChanged() {
      this.loaded = false;
      this.fetchImagesPerPeriod();
    },

    fetchImagesPerPeriod() {

      new ComService().getCountedItemsByYear(this.selectedYear, (response) => {

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
        this.loaded = true;
      });
    }

  }
};
</script>

<style>
#statistics {
  margin: 0 auto;
  max-width: 700px;
}

.right_align {
  float: right;
}

input[type="number"] {
  margin-right: 5px;
}
</style>
