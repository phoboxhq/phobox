import { Pie } from "vue-chartjs";

export default {
  extends: Pie,
  props: {
    datasets: {
      type: Object,
      default: null
    }
  },
  data: () => ({
    options: {
      cutoutPercentage: 50,
      legend: {
        labels: {
          fontColor: "white",
          fontSize: 14
        }
      },
    }
  }),
  mounted() {
    this.renderChart(this.datasets, this.options);
  },
  watch: {
    datasets() {
      this.renderChart(this.datasets, this.options);
    }
  },
}