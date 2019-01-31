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

        <!-- Download picture -->
        <a class="downloadAlbum" :href="downloadAlbum()" target="_blank" download v-show="albumname">
          <i class="fa fa-download" aria-hidden="true"></i> {{ $t('pictures.download') }}
        </a>

    </div>

    <div id="albumitems">
      <file-item v-for="item in album.items"
        :item="item"
        :key="item.path"
        :selectedItem="selectedItem"></file-item>
    </div>

    <!-- Include the lightbox -->
    <lightbox v-if="album.items"
        :items="album.items" 
        :selectedItem="selectedItem"></lightbox>

    <favoriteDialog :item="favoriteItem"></favoriteDialog>

  </div>
</template>

<script>
import Lightbox from '@/components/Lightbox';
import FileItem from '@/components/files/FileItem';
import FavoriteDialog from '@/components/dialogs/FavoriteDialog';
import ComService from '@/utils/ComService';

export default {
  name: "Approval",
  props: [],
  components: {
    Lightbox,
    FavoriteDialog,
    FileItem
  },
  data() {
    return {
      albums: [],
      album: {},
      albumname: null,
      selectedItem: null,
      favoriteItem: null,
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

    open(albumName) {
      this.$router.push({ path: "/albums/" + albumName });
    },

    downloadAlbum() {
      if (this.albumname == null) return;

      let com = new ComService();
      return "api/album/download/" + com.encodePath(this.albumname);
    },

    goBack() {
      this.$router.go(-1);
    }
  },
  computed: {},
  watch: {
    $route: "refresh",
    favoriteItem() {
      this.getAlbums();
    },
    albumname() {
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
</style>
