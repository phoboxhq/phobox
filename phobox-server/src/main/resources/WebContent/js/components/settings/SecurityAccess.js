const SecurityAccess = Vue.component(
	'securityaccess', {
	template: `

	<div class="panel panel-default">
		<div class="panel-heading">{{ Locale.values.settings.access_auth_head }}</div>
		<div class="panel-body">

			<p>{{ Locale.values.settings.access_auth_description }}</p>

			<form class="form-horizontal">
				<div class="form-group">
					<label for="inputUsername" class="col-sm-2 control-label">{{ Locale.values.settings.access_auth_username }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="inputUsername" 
							:placeholder="Locale.values.settings.access_auth_username" v-model="credentials.username">
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword3" class="col-sm-2 control-label">{{ Locale.values.settings.access_auth_password }}</label>
					<div class="col-sm-10">
						<input type="password" class="form-control" id="inputPassword3" 
							:placeholder="Locale.values.settings.access_auth_password" v-model="credentials.password">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-default" v-on:click="saveCredentials()">{{ Locale.values.settings.access_auth_save }}</button>
						<button class="btn btn-default" v-on:click="resetCredentials()">{{ Locale.values.settings.access_auth_reset }}</button>
					</div>
				</div>
			</form>

			<!-- Alerts for credentials -->
			<div class="alert alert-success alert-dismissible" role="alert" v-show="isStatusOk">
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
			  <strong>{{ Locale.values.settings.saved }}!</strong>{{ Locale.values.settings.access_auth_success }}
			</div>

			<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ Locale.values.settings.failed }}!</strong>{{ Locale.values.settings.access_auth_failed }}
			</div>
		</div>

	`,
	data: function() {
		return {
			credentials: {},
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
		fetchCredentials: function() {
			var that = this;
			new ComService().fetchCredentials(function(data) {
				that.credentials = data === undefined ? {username: '', password: ''} : data;
			});
		},

		saveCredentials: function() {
			var that = this;
			new ComService()
				.saveCredentials(
					this.credentials.username, 
					this.credentials.password, 
					function(data) {
						that.status = data.status;
					}
			);
		}, 

		resetCredentials: function() {
			var that = this;
			new ComService().resetCredentials(function(data) {
				that.status = data.status;
			});
		}
	},

	created: function() {
		this.fetchCredentials();
	}
});