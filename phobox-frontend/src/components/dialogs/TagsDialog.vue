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
            {{ $t('pictures.tags_dialog_hint') }}
            <br />
            <br />
						<label for="newname">{{ $t('pictures.tags_dialog_tags') }}:</label>
						<input id="tagsInput" type="text" data-role="tagsinput" />

					</div>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
						<strong>{{ $t('settings.failed') }}!</strong> {{ $t('pictures.tags_dialog_failed') }}
					</div>
				</div>
			
				<div class="footer">
          <!-- Loading image -->
          <transition name="fade">
              <img src="@/assets/loading.gif" v-if="isLoading" class="loading" />
          </transition>

					<button type="button" class="btn btn-default" :disabled="isLoading" v-on:click="save()">{{ $t('pictures.rename_dialog_save') }}</button>
					<button type="button" class="btn btn-default" :disabled="isLoading" v-on:click="close()">{{ $t('pictures.rename_dialog_cancel') }}</button>
				</div>
			</div>
		</div>
	
	</transition>
</template>

<script>
import $ from 'jquery';
import TagsInputComponents from 'bootstrap-tagsinput';
import ComService from '@/utils/ComService';

export default {
  name: "TagsDialog",
  props: ["item"],
  data() {
    return {
      name: this.item !== null ? this.item.name : null,
      status: null,
      isLoading: false,
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
      this.tags = $("#tagsInput").tagsinput("items");
      this.isLoading = true;

      new ComService().setTags(this.item.path, this.tags, (data) => {
        this.isLoading = false;
        this.status = data.status;
        if (this.status === "OK") {
          this.close();
        }

      });
    },

    onShow() {
      $("#tagsInput").tagsinput("removeAll");

      new ComService().getTags(this.item.path, (data) => {
        this.tags = data;
        this.tags.forEach((entry) => {
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
@import '../../../node_modules/bootstrap-tagsinput/dist/bootstrap-tagsinput.css';

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

.dialog .footer .loading {
  position: initial;
  padding: 2px;
  background-color: white;
}

.tag {
  font-size: 13px;
  background-color: #3898ff;
  color: #FFFFFF;
  padding: 5px;
  border-radius: 3px;
}
</style>
