const AlbumBrowser = Vue.component(
	'albumbrowser', {
    template: `
    <div id="albumbrowser">

        <!-- Existing album selector -->
        <div id="albumselector">
            <div class="form-group">
                <label for="sel2">Select album:</label>
                <select class="form-control" id="sel2" v-model="albumname">
                    <option v-for="a in albums" :value="a" :label="a">
                </select>
            </div>
        </div>

        <div id="albumitems">
            <div class="albumelement item" v-for="item in album.items" v-if="album">
                <img class="item_thumb" 
                    :src="item.thumbLow"
                    v-on:click="selectedItem = item"
                    v-if="item.type === 'file'" />
            </div>
        </div>

        <!-- Include the lightbox -->
        <lightbox v-if="album.items"
            :items="album.items" 
            :selectedItem="selectedItem"></lightbox>
    </div>
    `,
    props: [],
    data: function() {
    	return {
    		albums : [],
            album: {},
            albumname: null,
            selectedItem: null,
            Locale: Locale,
    	};
    },
    methods: {

        init: function() {
            var that = this;
            new ComService().getAlbums(function(data) {
                that.albums = data;
            });
        },

    	refresh () {
    		path = this.$route.params.albumpath;
            if(path !== undefined) {
        		var that = this;
                new ComService().getAlbum(path, function(data) {
                    that.album = data;
                });
            }
    	},

        open: function(albumName) {
            router.push({ path: "/albums/"+albumName });
        },

        goBack: function() {
            this.$router.go(-1);
        },

    },
    computed: {},
    watch: {
        '$route': 'refresh',
	    albumname: function() {
            this.open(this.albumname);
        },
	},
	created () {
        // Load all album names
    	this.init();

        // Load album content, if one album is selected
        this.refresh();
  	}
});