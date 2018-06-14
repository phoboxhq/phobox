const Status = Vue.component(
	'Status', {
	template: `

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

	`,
	
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
});