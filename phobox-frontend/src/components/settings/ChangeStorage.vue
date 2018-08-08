<template>
  <div class="panel panel-default">
		<div class="panel-heading">{{ Locale.values.settings.storage_path_head }}</div>
		<div class="panel-body">

			<p>{{ Locale.values.settings.storage_path_description }}</p>

			<form class="form-horizontal">
				<div class="form-group">
					<div class="col-sm-10">
						<input type="text" class="form-control" id="pathinput" placeholder="" v-model="path">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-default" v-on:click="changePath()">
							{{ Locale.values.settings.storage_path_start }}</button>
					</div>
				</div>
			</form>

			<!-- Alerts for credentials -->
			<div class="alert alert-success alert-dismissible" role="alert" v-show="isStatusOk">
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
			  <strong>{{ Locale.values.settings.saved }}!</strong>{{ Locale.values.settings.storage_path_success }}
			</div>

			<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ Locale.values.settings.failed }}!</strong>{{ Locale.values.settings.storage_path_failed }}
			</div>
		</div>
	</div>
</template>

<script>
import Locale from '@/Locale';
import ComService from '@/utils/ComService';
Locale.init()

export default {
  name: "ChangeStorage",
  data() {
    return {
      status: null,
      path: null,
      Locale: Locale
    };
  },

  computed: {
    isStatusOk() {
      return this.status === "OK";
    }
  },

  methods: {
    changePath() {
      new ComService().changePath(this.path, data => {
        this.status = data.status;
      });
    }
  }
};
</script>

<style>
</style>
