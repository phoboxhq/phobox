const Approval = Vue.component(
	'approval', {
    template: `
    <div id="approval">
        <!-- Show selected picture -->
        <div class="picPanel">
            <img :src="selectedPic" v-if="selectedPic" class="selectedPic" />
        </div>

        <!-- List all elements -->
        <div class="pictureNavigation">
            <ul>
                <li v-for="pic in pictures">
                    <img :src="pic" class="preview" @click="onPictureSelect(pic)" />
                </li>
            </ul>
        </div>
       
        <!-- Accept/Decline panel-->
        <div class="judgepanel">
            <!-- Accept -->
            <div class="judgeButton">
                <i class="fa fa-check-circle" aria-hidden="true"></i>
            </div>

            <!-- Decline -->
            <div class="judgeButton">
                <i class="fa fa-trash" aria-hidden="true"></i>
            </div>

        </div>

    </div>
    `,
    props: [],
    data: function() {
    	return {
    		pictures: [],
            selectedPic: null,
            Locale: Locale,
    	};
    },
    methods: {

        init: function() {
            var that = this;
            new ComService().getApprovalPictures(function(data) {
                that.pictures = data;
            });
        },

        onPictureSelect: function(pic) {
            this.selectedPic = pic;
        },

    /*	refresh () {
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
        },*/

    },
    computed: {},
    watch: {
	},
	created () {
        this.init();
  	}
});