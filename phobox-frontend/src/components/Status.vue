<template>
  <div class="status_panel">

    <div class="batch">
      <p class="title">{{ $t('status.import_state') }}</p>
      <p class="binfo">{{ statusData.importStatus }}</p>
    </div>

    <div class="batch">
      <p class="title">{{ $t('status.remaining_files') }}</p>
      <p class="binfo">{{ statusData.remainingfiles }}</p>
    </div>

    <div class="batch">
      <p class="title">{{ $t('status.current_file') }}</p>
      <p class="binfo">{{ statusData.file }}</p>
    </div>

    <div class="batch">
      <p class="title">{{ $t('status.free_space') }}</p>
      <p class="binfo">{{ freespaceInGb }} GB</p>
    </div>

    <div class="batch">
      <p class="title">{{ $t('status.max_space') }}</p>
      <p class="binfo">{{ maxspaceInGb }} GB</p>
    </div>

    <div class="batch">
      <p class="title">{{ $t('status.number_of_pictures') }}</p>
      <p class="binfo">{{ numberOfPictures }}</p>
    </div>

    <div class="batch">
      <p class="title">{{ $t('status.uptime') }}</p>
      <p class="binfo">{{ uptime }}</p>
    </div>

  </div>
</template>

<script>
import ComService from '@/utils/ComService';

export default {
  name: "Status",
  data() {
    return {
      statusData: {},
      intervalId: null
    }
  },
  computed: {
    freespaceInGb() {
      return (this.statusData.freespace / 1024).toFixed(1);
    },

    maxspaceInGb() {
      return (this.statusData.maxspace / 1024).toFixed(1);
    },

    numberOfPictures() {
      return this.statusData.numberOfPictures;
    },

    uptime() {
      let hours = (this.statusData.uptime / 1000 / 60 / 60).toFixed(1);

      if(hours > 24) {
        return `${(hours / 24).toFixed(1)} ${this.$t('status.days')}`;
      } else {
        return `${hours} ${this.$t('status.hours')}`;
      }
    }
  },
  
  methods: {
    fetchStatus() {
      new ComService().status(data => {
        this.statusData = data;
      });
    }
  },
  
  created() {
    this.fetchStatus();
    this.intervalId = setInterval(this.fetchStatus, 3000);
  },

  beforeDestroy() {
    clearInterval(this.intervalId);
    }
};
</script>

<style>
.status_panel {
  margin: 0 auto;
  max-width: 700px;
}

.status_panel:after {
  content: "\f05a";
    font-family: FontAwesome;
    position: absolute;
    font-size: 800px;
    color: rgba(25, 25, 25, 0.34);
    top: -240px;
    left: -100px;
    z-index: 1;
}

.status_panel .batch {
  width: 200px;
    height: 100px;
    float: left;
    margin: 10px;
    padding: 10px;
    background-color: rgb(24, 23, 23);
    border-radius: 3px;
    transition: all 0.5s;
    border: 1px solid #1d1b1b;
    box-shadow: 0px 0px 0px rgba(0, 0, 0, 0.63);
    transition: all 0.5s cubic-bezier(0.4, 0, 0.68, 0.97);
    z-index: 2;
    position: relative;
}

.status_panel .batch:hover {
  border: 1px solid rgba(75, 103, 121, 0.77);
    box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.63);
}

.status_panel .batch .title {
  font-weight: bold;
    font-size: 16px;
    color: #d4d4d4;
}
</style>
