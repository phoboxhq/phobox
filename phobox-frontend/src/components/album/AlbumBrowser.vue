<template>
  <div id="albumbrowser">
    <!-- Existing album selector -->
    <div id="albumselector">
        <div class="form-group">
            <label for="sel2">{{ Locale.values.album.select }}:</label>
            <select class="form-control" id="sel2" v-model="albumname">
                <option v-for="a in albums" :value="a" :label="a" :key="a"/>
            </select>
        </div>
    </div>

    <div id="albumitems">
        <div class="albumelement item" v-for="item in album.items" :key="item" v-if="album">
            <img class="item_thumb" 
                :src="item.thumb"
                v-on:click="selectedItem = item"
                v-if="item.type === 'file'" />
        </div>
    </div>

    <!-- Include the lightbox -->
    <lightbox v-if="album.items"
        :items="album.items" 
        :selectedItem="selectedItem"></lightbox>
  </div>
</template>

<script>
export default {
  name: "Approval",
  props: [],
  data: function() {
    return {
      albums: [],
      album: {},
      albumname: null,
      selectedItem: null,
      Locale: Locale
    };
  },
  methods: {
    init: function() {
      var that = this;
      new ComService().getAlbums(function(data) {
        that.albums = data;
      });
    },

    refresh() {
      path = this.$route.params.albumpath;
      if (path !== undefined) {
        var that = this;
        new ComService().getAlbum(path, function(data) {
          that.album = data;
        });
      }
    },

    open: function(albumName) {
      router.push({ path: "/albums/" + albumName });
    },

    goBack: function() {
      this.$router.go(-1);
    }
  },
  computed: {},
  watch: {
    $route: "refresh",
    albumname: function() {
      this.open(this.albumname);
    }
  },
  created() {
    // Load all album names
    this.init();

    // Load album content, if one album is selected
    this.refresh();
  }
};
</script>

<style>
#albumbrowser {
  padding: 10px;
}

#albumbrowser #albumselector {
  margin: -47px 5px 13px 5px;
  background-color: #191717;
  padding: 15px;
  border-radius: 3px;
}
</style>
