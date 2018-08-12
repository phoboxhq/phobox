<template>
	<div class="panel panel-default">
		<div class="panel-heading">{{ $t('settings.access_auth_head') }}</div>
		<div class="panel-body">

			<p>{{ $t('settings.access_auth_description') }}</p>

			<form class="form-horizontal">
				<div class="form-group">
					<label for="inputUsername" class="col-sm-2 control-label">{{ $t('settings.access_auth_username') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="inputUsername" 
							:placeholder="$t('settings.access_auth_username')" v-model="credentials.username">
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword3" class="col-sm-2 control-label">{{ $t('settings.access_auth_password') }}</label>
					<div class="col-sm-10">
						<input type="password" class="form-control" id="inputPassword3" 
							:placeholder="$t('settings.access_auth_password')" v-model="credentials.password">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-default" v-on:click="saveCredentials()">{{ $t('settings.access_auth_save') }}</button>
						<button class="btn btn-default" v-on:click="resetCredentials()">{{ $t('settings.access_auth_reset') }}</button>
					</div>
				</div>
			</form>

			<!-- Alerts for credentials -->
			<div class="alert alert-success alert-dismissible" role="alert" v-show="isStatusOk">
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
			  <strong>{{ $t('settings.saved') }}!</strong>{{ $t('settings.access_auth_success') }}
			</div>

			<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ $t('settings.failed') }}!</strong>{{ $t('settings.access_auth_failed') }}
			</div>
		</div>
	</div>
</template>

<script>
import ComService from '@/utils/ComService';

export default {
  name: "SecurityAccess",
  data() {
    return {
      credentials: {},
      status: null
    };
  },

  computed: {
    isStatusOk() {
      return this.status === "OK";
    }
  },

  methods: {
    fetchCredentials() {
      new ComService().fetchCredentials(data => {
        this.credentials = data === undefined ? { username: "", password: "" } : data;
      });
    },

    saveCredentials() {
      new ComService().saveCredentials(
        this.credentials.username,
        this.credentials.password,
        data => {
          this.status = data.status;
        }
      );
    },

    resetCredentials() {
      new ComService().resetCredentials(data => {
        this.status = data.status;
      });
    }
  },

  created() {
    this.fetchCredentials();
  }
};
</script>

<style>
</style>
