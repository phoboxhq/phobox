<template>
  <div class="panel panel-default">
		<div class="panel-heading">{{ Locale.values.settings.storage_thumbnails_head }}</div>
		<div class="panel-body">

			<p>{{ Locale.values.settings.storage_thumbnails_description }}</p>

			<button class="btn btn-default" v-on:click="createThumbnails()">
				{{ Locale.values.settings.storage_thumbnails_start }}</button>

			<!-- Alerts for credentials -->
			<div class="alert alert-success alert-dismissible" role="alert" v-show="isStatusOk">
			  <button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
			  <strong>{{ Locale.values.settings.saved }}!</strong>{{ Locale.values.settings.storage_thumbnails_success }}
			</div>

			<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ Locale.values.settings.failed }}!</strong>{{ Locale.values.settings.storage_thumbnails_failed }}
			</div>
		</div>
	</div>
</template>

<script>
import Locale from '@/Locale';
import ComService from '@/utils/ComService';
Locale.init()

export default {
  name: "CreateThumbnails",
  data() {
    return {
      status: null,
      Locale: Locale
    };
  },

  computed: {
    isStatusOk() {
      return this.status === "OK";
    }
  },

  methods: {
    createThumbnails() {
      new ComService().createThumbnails(data => {
        this.status = data.status;
      });
    }
  }
};
</script>

<style>
</style>
