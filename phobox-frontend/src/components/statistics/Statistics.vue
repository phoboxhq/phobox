<template>
  <div id="statistics">
    <h1>{{ $t('statistics.headline') }}</h1>
    <hr>

    <h3>{{ $t('statistics.tagging_state') }}</h3>
    <tagging-state-chart />

    <hr>

    <h3>{{ $t('statistics.overview_year') }}</h3>
    <div class="form-inline right_align">
      <input
        type="number"
        name="year"
        class="form-control"
        v-model="selectedYear"
        min="1826"
        :max="currentYear"
      >
    </div>

    <image-by-time-chart :selected-year="selectedYear"/>
  </div>
</template>

<script>
import ComService from "@/utils/ComService";
import ImageByTimeChart from "./ImageByTimeChart.vue";
import TaggingStateChart from "./TaggingStateChart.vue";

export default {
  name: "Statistics",
  components: {
    ImageByTimeChart,
    TaggingStateChart
  },
  data() {
    return {
      selectedYear: new Date().getFullYear()
    };
  },

  computed: {
    currentYear() {
      return new Date().getFullYear();
    }
  },

  methods: {}
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
