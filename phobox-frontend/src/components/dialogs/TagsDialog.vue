<template>
  <transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- TagsDialog -->
			<div id="renameDialog" class="dialog">
				<div class="head">
					{{ $t('pictures.tags') }} {{ getName() }}
				</div>

				<div class="content">
					<div class="form-group">
						<label for="newname">Name:</label>
						<input id="tagsInput" type="text" data-role="tagsinput" />

					</div>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
						<strong>{{ $t('settings.failed') }}!</strong> {{ $t('pictures.rename_dialog_failed') }}
					</div>
				</div>
			
				<div class="footer">
					<button type="button" class="btn btn-default" v-on:click="save()">{{ $t('pictures.rename_dialog_save') }}</button>
					<button type="button" class="btn btn-default" v-on:click="close()">{{ $t('pictures.rename_dialog_cancel') }}</button>
				</div>
			</div>
		</div>
	
	</transition>
</template>

<script>
export default {
  name: "TagsDialog",
  props: ["item"],
  data() {
    return {
      name: this.item !== null ? this.item.name : null,
      status: null,
      tags: [],
    };
  },
  methods: {
    getName() {
      if (this.item !== null) {
        return this.item.name;
      }
      return "";
    },

    save() {
      var that = this;
      this.tags = $("#tagsInput").tagsinput("items");

      new ComService().setTags(this.item.path, this.tags, function(data) {
        that.status = data.status;
        if (that.status === "OK") {
          that.close();
        }
      });
    },

    onShow() {
      var that = this;
      $("#tagsInput").tagsinput("removeAll");

      new ComService().getTags(this.item.path, function(data) {
        that.tags = data;
        that.tags.forEach(function(entry) {
          $("#tagsInput").tagsinput("add", entry);
        });
      });
    },

    close() {
      this.$parent.tagsItem = null;
    }
  },
  watch: {
    item() {
      this.name = this.getName();

      if (this.item !== null) {
        this.onShow();
      }
    }
  }
};
</script>

<style>
.dialog {
  position: fixed;
  z-index: 100;
  top: 10%;
  max-width: 500px;
  box-shadow: 0px 0px 15px 0px black;
  border-radius: 5px;
  margin: 15px auto;
  left: 0;
  right: 0;
  background-color: #212020;
}

.dialog .head {
  font-weight: bold;
  background-color: #151414;
  padding: 10px;
  border-radius: 5px 5px 0px 0px;
}

.dialog .content {
  padding: 10px;
}

.dialog .footer {
  padding: 10px;
  text-align: right;
}

.bootstrap-tagsinput .tag {
  color: #151414;
  font-size: 13px;
}
</style>
