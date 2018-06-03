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
            <div class="judgeButton green" @click="onAccept">
                <i class="fa fa-check-circle" aria-hidden="true"></i>
                {{ Locale.values.approval.accept }}
                </div>
                
                <div class="spacer"></div>

                <!-- Decline -->
                <div class="judgeButton red" @click="onDecline">
                <i class="fa fa-trash" aria-hidden="true"></i>
                {{ Locale.values.approval.decline }}
            </div>
        </div>

    </div>
    `,
    props: [],
    data: function() {
    	return {
    		pictures: [],
            selectedPic: null,
            status: null,
            Locale: Locale,
    	};
    },
    methods: {

        init() {
            new ComService().getApprovalPictures((data) => {
                this.pictures = data;
                this.selectedPic = this.pictures.length > 0 ? this.pictures[0] : null;
                this.setMagnification();
            });
        },

        onPictureSelect(pic) {
            this.selectedPic = pic;

            // Activate maginfication view
            this.setMagnification();
        },

        setMagnification() {
            if(this.selectedPic === null)
                return;
            
            setTimeout(() => {
                $("#mainPic").elevateZoom({
                    zoomType: "lens",
                    zoomLens: false,
                    lensShape: "square",
                    lensSize: 300
                });
            }, 500);
        },

        onAccept() {
            if(this.selectedPic === null)
                return;
                
                new ComService().acceptApprovalPicture(this.selectedPic, (data) => {
                    this.status = data.status;
                    this.init();
                });
            },
            
        onDecline() {
            if(this.selectedPic === null)
                return;

            new ComService().declineApprovalPicture(this.selectedPic, (data) => {
                this.status = data.status;
                this.init();
			});
        },

        next() {
			if(this.hasNext) {
                index = this.pictures.indexOf(this.selectedPic);
                this.selectedPic = this.pictures[index+1];
                this.scrollToPreview();
			}
		},
        
		previous() {
            if(this.hasPrevious) {
                index = this.pictures.indexOf(this.selectedPic);
				this.selectedPic = this.pictures[index-1];
                this.scrollToPreview();
			}
        },

        stopZoom() {
            $.removeData($('#mainPic'), 'elevateZoom');
            $('.zoomContainer').remove();
        }
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
      },
      beforeDestroy: function() {
		this.stopZoom();
    }
});