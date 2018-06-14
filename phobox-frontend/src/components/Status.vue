<template>
  <div class="status_panel">

		<div class="batch">
			<p class="title">{{ Locale.values.status.import_state }}</p>
			<p class="binfo">{{ statusData.importStatus }}</p>
		</div>

		<div class="batch">
			<p class="title">{{ Locale.values.status.remaining_files }}</p>
			<p class="binfo">{{ statusData.remainingfiles }}</p>
		</div>

		<div class="batch">
			<p class="title">{{ Locale.values.status.current_file }}</p>
			<p class="binfo">{{ statusData.file }}</p>
		</div>

		<div class="batch">
			<p class="title">{{ Locale.values.status.free_space }}</p>
			<p class="binfo">{{ freespaceInGb }} GB</p>
		</div>

		<div class="batch">
			<p class="title">{{ Locale.values.status.max_space }}</p>
			<p class="binfo">{{ maxspaceInGb }} GB</p>
		</div>

		<div class="batch">
			<p class="title">{{ Locale.values.status.number_of_pictures }}</p>
			<p class="binfo">{{ numberOfPictures }}</p>
		</div>

	</div>
</template>

<script>
export default {
  name: "Status",
  data: function() {
		return {
			statusData: {},
			intervalId: null,
			Locale: Locale,
		}
	},
  computed: {
		freespaceInGb: function() {
			return (this.statusData.freespace / 1024).toFixed(1);
		},

		maxspaceInGb: function() {
			return (this.statusData.maxspace / 1024).toFixed(1);
		},

		numberOfPictures: function() {
			return this.statusData.numberOfPictures;
		},
	},
	
	methods: {
		fetchStatus: function() {
			that = this;
			new ComService().status(function(data) {
				that.statusData = data;
			});
		}
	},
	
	created: function() {
		this.fetchStatus();
		this.intervalId = setInterval(this.fetchStatus, 3000);
	},

	beforeDestroy: function() {
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
