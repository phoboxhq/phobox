<template>
  <transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- RenameDialog -->
			<div id="renameDialog" class="dialog">
				<div class="head">
					{{ $t('pictures.rename_dialog_head') }} {{ getName() }}
				</div>

				<div class="content">
					<div class="form-group">
						<label for="newname">{{ $t('pictures.rename_dialog_new_name') }}:</label>
						<input type="text" class="form-control" id="newname" v-model="name">
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
import ComService from '@/utils/ComService';

export default {
  name: "RenameDialog",
  props: ["item"],
  data() {
    return {
      name: this.item !== null ? this.item.name : null,
      status: null,
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
      new ComService().rename(this.item.path, this.name, data => {
        this.status = data.status;

        if (this.status === "OK") {
          let oldname = this.item.name;

          if (this.item.name != null)
            this.item.name = this.item.name.replaceAll(oldname, this.name);
          if (this.item.path != null)
            this.item.path = this.item.path.replaceAll(oldname, this.name);
          if (this.item.preview != null)
            this.item.preview = this.item.preview.replaceAll(
              oldname,
              this.name
            );
          if (this.item.raw != null)
            this.item.raw = this.item.raw.replaceAll(oldname, this.name);
          if (this.item.thumbHigh != null)
            this.item.thumbHigh = this.item.thumbHigh.replaceAll(
              oldname,
              this.name
            );
          if (this.item.thumbLow != null)
            this.item.thumbLow = this.item.thumbLow.replaceAll(
              oldname,
              this.name
            );

          this.close();
        }
      });
    },

    close() {
      this.$parent.renameItem = null;
    }
  },
  watch: {
    item() {
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
