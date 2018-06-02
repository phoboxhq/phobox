const Approval = Vue.component(
	'approval', {
    template: `
    <div id="approval">
        <!-- Show selected picture -->
        <div class="picPanel">
            <transition name="fadeIn">
                <img :src="selectedPic" id="mainPic" 
                    :data-zoom-image="selectedPic"
                    :key="selectedPic" 
                    v-if="selectedPic" 
                    class="selectedPic" />
            </transition>
        </div>

        <!-- List all elements -->
        <div class="pictureNavigation">
            <ul>
                <li v-for="pic in pictures">
                    <img :src="pic" class="preview" @click="onPictureSelect(pic)" v-bind:class="{ isSelected: pic === selectedPic }" />
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

            setTimeout(() => {
                console.log($("#mainPic"));
    
                $("#mainPic").elevateZoom({
                    zoomType: "lens",
                    zoomLens: false,
                    lensShape: "square",
                    lensSize: 300
                });
            }, 500);
        },

        next: function() {
			if(this.hasNext) {
                index = this.pictures.indexOf(this.selectedPic);
				this.selectedPic = this.pictures[index+1];
			}
		},

		previous: function() {
			if(this.hasPrevious) {
				index = this.pictures.indexOf(this.selectedPic);
				this.selectedPic = this.pictures[index-1];
			}
		},
    },
    computed: {
        hasNext: function() {
			index = this.pictures.indexOf(this.selectedPic);
			return index+1 < this.pictures.length;
		},

		hasPrevious: function() {
			index = this.pictures.indexOf(this.selectedPic);
			return index-1 >= 0;
		},
    },
    watch: {
	},
	created () {
        this.init();

        var that = this;

        // Add cursor key listeners for navigation
        document.onkeydown = function (e) {
            e = e || window.event;

            // Right/Next
            if(e.keyCode === 39 && that.hasNext) {
                that.next();
            }

            // Left/Previous
            if(e.keyCode === 37 && that.hasPrevious) {
                that.previous();
            }
        };
  	}
});