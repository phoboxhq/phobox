<template>
  <div id="albumbrowser">

    <!-- No albums exists in this moment -->
    <div v-if="!albums || albums.length === 0" id="no_albums">
      <i style="font-size: 20em; color: rgba(0, 0, 0, 0.27);"
            class="fa fa-frown-o" aria-hidden="true"></i>
      <p><b>{{ $t('album.empty') }}</b></p>
      <div v-html="$t('album.empty_help')"></div>
    </div>

    <!-- Existing album selector -->
    <div id="albumselector" v-if="albums && albums.length > 0">
        <div class="form-group">
            <label for="sel2">{{ $t('album.select') }}:</label>
            <select class="form-control" id="sel2" v-model="albumname">
                <option v-for="(a, key) in albums" :value="a" :label="a" :key="key">{{ a }}</option>
            </select>
        </div>

        <!-- Download album -->
        <a class="downloadAlbum" :href="downloadAlbum()" target="_blank" download v-show="albumname">
          <i class="fa fa-download" aria-hidden="true"></i> {{ $t('album.download') }}
        </a>

        <!-- Rename album -->
        <span class="renameAlbum button" @click="onToggleRename" v-show="albumname">
          <i class="fa fa-pencil" aria-hidden="true"></i> {{ $t('album.rename') }}
        </span>

        <!-- Delete album -->
        <span class="deleteAlbum button" @click="showAlbumDeletion=true" v-show="albumname" :title="$t('album.delete_info')">
          <i class="fa fa-trash" aria-hidden="true"></i> {{ $t('album.delete') }}
        </span>

        <div class="renameDialog" v-show="showAlbumRename">
          <label for="newname">{{ $t('album.rename_newname') }}:</label>
          <input type="text" class="form-control" id="newname" :ref="'newname'" v-model="newAlbumName" v-on:keyup.enter="onRenameAlbum">
          <button type="button" class="btn btn-primary" @click="onRenameAlbum">{{ $t('album.rename_ok') }}</button>
          <button type="button" class="btn btn-default" @click="showAlbumRename=false">{{ $t('album.rename_cancel') }}</button>
        </div>
    </div>

    <div id="albumitems">
      <file-item v-for="item in album.items"
        :item="item"
        :key="item.path"
        :albumname="albumname"
        :selectedItem="selectedItem"
        v-on:remove-favorite="onRemoveFavorite" />
    </div>

    <!-- Include the lightbox -->
    <lightbox v-if="album.items"
        :items="album.items" 
        :selectedItem="selectedItem" />

    <favoriteDialog :item="favoriteItem" />

    <confirm-dialog 
      v-show="showAlbumDeletion"
      :header="`${$t('album.delete_confirm_headline')} ${albumname}?`" 
      :contextText="`${$t('album.delete_info')} <br /><br /> <b>${$t('album.delete_confirm_text')}</b>`" 
      :doItText="$t('album.delete')" 
      :cancelText="$t('album.delete_cancel')"
      @onConfirm="onConfirmAlbumDeletion"
      @onCancel="onCancelAlbumDeletion" />

  </div>
</template>

<script>
import Lightbox from '@/components/Lightbox';
import FileItem from '@/components/files/FileItem';
import FavoriteDialog from '@/components/dialogs/FavoriteDialog';
import ConfirmDialog from '@/components/dialogs/ConfirmDialog';
import ComService from '@/utils/ComService';

export default {
  name: "Approval",
  props: [],
  components: {
    Lightbox,
    FavoriteDialog,
    ConfirmDialog,
    FileItem
  },
  data() {
    return {
      albums: [],
      album: {},
      albumname: null,
      selectedItem: null,
      favoriteItem: null,
      showAlbumDeletion: false,
      showAlbumRename: false,
      newAlbumName: null,
      SERVER_PATH: process.env.SERVER_PATH,
    };
  },
  methods: {
    getAlbums() {
      new ComService().getAlbums(data => {
        this.albums = data;
      });
    },

    refresh() {
      let path = this.$route.params.albumpath;
      if (path !== undefined) {
        new ComService().getAlbum(path, data => {
          this.album = data;
        });
      }
    },

    onRemoveFavorite() {
      this.refresh();
    },

    open(albumName) {
      this.$router.push({ path: "/albums/" + albumName });
    },

    downloadAlbum() {
      if (this.albumname == null) return;

      let com = new ComService();
      return this.SERVER_PATH + "api/album/download/" + com.encodePath(this.albumname);
    },

    deleteAlbum() {
      if (this.albumname == null) return;
      let com = new ComService();
      com.deleteAlbum(this.albumname, 
        (success) => {
          this.getAlbums();
          this.unselectAlbum();
          this.showAlbumDeletion = false;
        }, 
        (error) => {
          console.error("error", error.message)
        });
    },

    unselectAlbum() {
      this.albumname = null;
      this.album = {};
    },

    goBack() {
      this.$router.go(-1);
    },

    onToggleRename() {
      this.showAlbumRename = !this.showAlbumRename;
      if(this.showAlbumRename)
        this.focusInput('newname');
    },

    onRenameAlbum() {
      if (this.albumname == null) return;
      let com = new ComService();
      com.renameAlbum(this.albumname, this.newAlbumName, 
        (success) => {
          this.getAlbums();
          this.open(this.newAlbumName);
          this.showAlbumRename = false;
          this.newAlbumName = null;
        }, 
        (error) => {
          console.error("error", error.message)
        });
    },

    onConfirmAlbumDeletion() {
      this.deleteAlbum();
    },

    onCancelAlbumDeletion() {
      this.showAlbumDeletion = false;
    },

    focusInput(inputRefId) {
      setTimeout(() =>{
        if(this.$refs[inputRefId])
          this.$nextTick(() => this.$refs[inputRefId].focus());
      }, 100);
    }
  },
  computed: {},
  watch: {
    $route: "refresh",
    favoriteItem() {
      this.getAlbums();
    },
    albumname() {
      if(this.albumname)
        this.open(this.albumname);
    }
  },
  created() {
    // Load all album names
    this.getAlbums();

    // Load album content, if one album is selected
    this.refresh();
  }
};
</script>

<style>
#albumbrowser {
  padding: 10px;
}

#no_albums {
  max-width: 700px;
  margin: 0 auto;
  text-align: center;
}

#albumbrowser #albumselector {
  margin: -47px 5px 13px 5px;
  background-color: #191717;
  padding: 15px;
  border-radius: 3px;
}

#albumbrowser a {
  color: #ffffff;
}

.button {
  color: #ffffff;
  margin-left: 10px;
}

.button:hover {
  text-decoration: underline;
  cursor: pointer;
}

.renameDialog {
  margin-top: 10px;
  background-color: #232323;
  padding: 10px;
  border-radius: 2px;
}

</style>
