<template>
  <transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- DeleteConfirmDialog -->
			<div id="deleteConfirmDialog" class="dialog">
				<div class="head">
					{{ $t('pictures.delete_dialog_head') }} {{ getName() }}
				</div>

				<div class="content">
					{{ $t('pictures.delete_dialog_content') }}
					<p><strong>'{{ getPath() }}'</strong></p>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
						<strong>{{ $t('settings.failed') }}!</strong> {{ $t('pictures.delete_dialog_failed') }}
					</div>
				</div>
			
				<div class="footer">
					<button type="button" class="btn btn-danger" v-on:click="deleteItem()">{{ $t('pictures.delete_dialog_delete') }}</button>
					<button type="button" class="btn btn-default" v-on:click="close()">{{ $t('pictures.delete_dialog_cancel') }}</button>
				</div>
			</div>
		</div>
	
	</transition>
</template>

<script>
import ComService from '@/utils/ComService';

export default {
  name: "DeleteConfirmDialog",
  props: ["item"],
  data() {
    return {
      name: this.item !== null ? this.item.name : null,
      status: null
    };
  },
  methods: {
    getName() {
      if (this.item !== null) {
        return this.item.name;
      }
      return "";
    },

    getPath() {
      if (this.item !== null) {
        return this.item.path;
      }
      return "";
    },

    deleteItem() {
      new ComService().delete(this.item.path, (data) => {
        this.status = data.status;

        if (this.status === "OK") {
          let index = this.$parent.items.indexOf(this.item);

          if (index >= 0) {
            this.$parent.items.splice(index, 1);
          }

          this.close();
        }
      });
    },

    close() {
      this.$parent.deleteItem = null;
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
