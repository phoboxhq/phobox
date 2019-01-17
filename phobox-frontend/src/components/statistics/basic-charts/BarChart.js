import { Bar } from "vue-chartjs";

export default {
  extends: Bar,
  props: {
    chartData: {
      type: Object,
      default: null
    }
  },
  data: () => ({
    colors: [
      "#a6cee3",
      "#1f78b4",
      "#b2df8a",
      "#33a02c",
      "#fb9a99",
      "#e31a1c",
      "#fdbf6f",
      "#ff7f00",
      "#cab2d6"
    ],
    options: {
      responsive: true,
      maintainAspectRatio: false,
      legend: {
        labels: {
          fontColor: "white",
          fontSize: 14
        }
      },
      scales: {
        yAxes: [
          {
            ticks: {
              fontColor: "white",
              fontSize: 14,
              beginAtZero: true
            },
            gridLines: {
              display: true,
              color: "#2f2f2f"
            }
          }
        ],
        xAxes: [
          {
            ticks: {
              fontColor: "white",
              fontSize: 14,
              beginAtZero: true
            },
            gridLines: {
              display: true,
              color: "#2f2f2f"
            }
          }
        ]
      }
    }
  }),

  computed: {
    datasets() {
      return this.chartData.datasets;
    }
  },

  mounted() {
    this.renderData();
  },

  watch: {
    datasets() {
      this.renderData();
    }
  },

  methods: {
    renderData() {
      if (!this.chartData) {
        return;
      }

      this.fillDatasetsWithColor();
      this.renderChart(this.chartData, this.options);
    },

    fillDatasetsWithColor() {
      let colorIndex = 0;
      this.chartData.datasets.forEach((item, index) => {
        if (!item.backgroundColor) {
          if (colorIndex < this.colors.length) {
            item.backgroundColor = this.colors[colorIndex++];
          } else {
            item.backgroundColor = this.getRandomColor();
          }
        }
      });
    },

    getRandomColor() {
      var letters = "0123456789ABCDEF";
      var color = "#";
      for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
    }
  }
};