<template>
  <transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- RenameDialog -->
			<div id="renameDialog" class="dialog">
				<div class="head">
					{{ Locale.values.pictures.rename_dialog_head }} {{ getName() }}
				</div>

				<div class="content">
					<div class="form-group">
						<label for="newname">Name:</label>
						<input type="text" class="form-control" id="newname" v-model="name">
					</div>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
						<strong>{{ Locale.values.settings.failed }}!</strong> {{ Locale.values.pictures.rename_dialog_failed }}
					</div>
				</div>
			
				<div class="footer">
					<button type="button" class="btn btn-default" v-on:click="save()">{{ Locale.values.pictures.rename_dialog_save }}</button>
					<button type="button" class="btn btn-default" v-on:click="close()">{{ Locale.values.pictures.rename_dialog_cancel }}</button>
				</div>
			</div>
		</div>
	
	</transition>

</template>

<script>
import Locale from '@/Locale';

export default {
  name: "RenameDialog",
  props: ["item"],
  data: function() {
    return {
      name: this.item !== null ? this.item.name : null,
      status: null,
      Locale: Locale
    };
  },
  methods: {
    getName: function() {
      if (this.item !== null) {
        return this.item.name;
      }
      return "";
    },

    save: function() {
      var that = this;
      new ComService().rename(this.item.path, this.name, function(data) {
        that.status = data.status;

        if (that.status === "OK") {
          oldname = that.item.name;

          if (that.item.name != null)
            that.item.name = that.item.name.replaceAll(oldname, that.name);
          if (that.item.path != null)
            that.item.path = that.item.path.replaceAll(oldname, that.name);
          if (that.item.preview != null)
            that.item.preview = that.item.preview.replaceAll(
              oldname,
              that.name
            );
          if (that.item.raw != null)
            that.item.raw = that.item.raw.replaceAll(oldname, that.name);
          if (that.item.thumbHigh != null)
            that.item.thumbHigh = that.item.thumbHigh.replaceAll(
              oldname,
              that.name
            );
          if (that.item.thumbLow != null)
            that.item.thumbLow = that.item.thumbLow.replaceAll(
              oldname,
              that.name
            );

          that.close();
        }
      });
    },

    close: function() {
      this.$parent.renameItem = null;
    }
  },
  watch: {
    item: function() {
      this.name = this.getName();
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
