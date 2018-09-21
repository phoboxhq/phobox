<template>
	<div class="panel panel-default">
		<div class="panel-heading">{{ $t('settings.import_pattern_head') }}</div>
		<div class="panel-body">

			<p>{{ $t('settings.import_pattern_description') }}</p>
			<div v-html="$t('settings.import_pattern_keywords')"></div>

			<form class="form-horizontal">
				<div class="form-group">
					<label for="formatinput" class="col-sm-2 control-label">{{ $t('settings.import_pattern_formatting') }}</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="formatinput" placeholder="format" v-model="format">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-default" v-on:click="saveImportPattern()">{{ $t('settings.import_pattern_save') }}</button>
					</div>
				</div>
			</form>

			<!-- Alerts for credentials -->
			<div class="alert alert-success alert-dismissible" role="alert" v-show="isStatusOk">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ $t('settings.saved') }}!</strong>
			</div>

			<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
				<strong>{{ $t('settings.failed') }}!</strong>
			</div>
		</div>
  </div>
</template>

<script>
import ComService from '@/utils/ComService';

export default {
  name: "ImportFormat",
  data() {
    return {
      format: null,
      status: null,
    };
  },

  computed: {
    isStatusOk() {
      return this.status === "OK";
    }
  },

  methods: {
    fetchImportPattern() {
      new ComService().fetchImportPattern(data => {
        this.format = data;
      });
    },

    saveImportPattern() {
      new ComService().saveImportPattern(this.format, data => {
        this.status = data.status;
      });
    }
  },

  created() {
    this.fetchImportPattern();
  }
};
</script>

<style>
</style>
