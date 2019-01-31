<template>
  <transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- FavoriteDialog -->
			<div id="favoriteDialog" class="dialog">
				<div class="head">
					{{ $t('pictures.favorite_dialog_head') }} {{ getName() }}
				</div>

				<div class="content">
					<!-- Album type switch -->
					<div style="padding:5px;">
						<label for="newAlbum" class="radio-inline">{{ $t('pictures.favorite_dialog_new') }}</label>
            <input id="newAlbum" type="radio" value="newAlbum" v-model="albumSwitch" @click="focusNewAlbumInput">
            
            <br />
						<label for="existingAlbum" class="radio-inline">{{ $t('pictures.favorite_dialog_existing') }}</label>
            <input id="existingAlbum" type="radio" value="existingAlbum" v-model="albumSwitch">
					</div>

					<!-- New album input -->
					<div class="form-group" v-if="albumSwitch === 'newAlbum'">
						<label for="newname">{{ $t('pictures.favorite_dialog_name') }}:</label>
						<input type="text" class="form-control" id="newname" :ref="'newnameInput'" v-model="albumname">
					</div>

					<!-- Existing album selector -->
          <div class="form-group" v-if="albumSwitch === 'existingAlbum'">
            <label for="sel2">{{ $t('pictures.favorite_dialog_select') }}:</label>
              <select class="form-control" id="sel2" v-model="albumname">
                <option v-for="a in albums" :value="a" :label="a" :key="a">{{a}}</option>
              </select>
          </div>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null">
              <span aria-hidden="true">&times;</span>
            </button>
						<strong>{{ $t('settings.failed') }}!</strong> {{ $t('pictures.rename_dialog_failed') }}
					</div>
				</div>
			
				<div class="footer">
					<button type="button" class="btn btn-default" v-on:click="save()">{{ $t('pictures.favorite_dialog_add') }}</button>
					<button type="button" class="btn btn-default" v-on:click="close()">{{ $t('pictures.rename_dialog_cancel') }}</button>
				</div>
			</div>
		</div>
	
	</transition>
</template>

<script>
import ComService from '@/utils/ComService';

export default {
  name: "FavoriteDialog",
  props: ["item"],
  data() {
    return {
      albums: [],
      albumSwitch: "existingAlbum",
      albumname: null,
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

    loadAlbums () {
      new ComService().getAlbums(data => {
        this.albums = data;
        if(this.albums && this.albums.length && this.albums.length > 0) {
          this.albumSwitch = "existingAlbum"
        } else {
          this.albumSwitch = "newAlbum"
        }
      });
    },

    save () {
      new ComService().addToAlbum(this.item.path, this.albumname, data => {
        this.status = data.status;

        if (this.status === "OK") {
          this.close();
          this.loadAlbums();
        }
      });
    },

    close () {
      this.$parent.favoriteItem = null;
    },

    focusNewAlbumInput() {
      setTimeout(() =>{
        if(this.$refs['newnameInput'])
          this.$nextTick(() => this.$refs['newnameInput'].focus());
      }, 100);
    }

  },
  watch: {
    item () {
      this.name = this.getName();
      this.focusNewAlbumInput();
    }
  },
  created () {
    this.loadAlbums();
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
