const ImportFormat = Vue.component(
	'importformat', {
	template: `

	<div class="panel panel-default">
		<div class="panel-heading">{{ Locale.values.settings.import_pattern_head }}</div>
		<div class="panel-body">

			<p>{{ Locale.values.settings.import_pattern_description }}</p>
			<div v-html="Locale.values.settings.import_pattern_keywords"></div>

			<form class="form-horizontal">
				<div class="form-group">
					<label for="formatinput" class="col-sm-2 control-label">{{ Locale.values.settings.import_pattern_formatting }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="formatinput" placeholder="format" v-model="format">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-default" v-on:click="saveImportPattern()">{{ Locale.values.settings.import_pattern_save }}</button>
					</div>
				</div>
			</form>

			<!-- Alerts for credentials -->
			<div class="alert alert-success alert-dismissible" role="alert" v-show="isStatusOk">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ Locale.values.settings.saved }}!</strong>
			</div>

			<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ Locale.values.settings.failed }}!</strong>
			</div>
		</div>

	`,
	data: function() {
		return {
			format: null,
			status: null,
			Locale: Locale,
		}
	},
	
	computed: {
		isStatusOk: function() {
			return this.status === "OK";
		}
	},
	
	methods: {
		fetchImportPattern: function() {
			var that = this;
			new ComService().fetchImportPattern(function(data) {
				that.format = data;
			});
		},

		saveImportPattern: function() {
			var that = this;
			new ComService()
				.saveImportPattern(this.format, function(data) {
					that.status = data.status;
				}
			);
		}, 
	},

	created: function() {
		this.fetchImportPattern();
	}
});