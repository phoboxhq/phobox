<template>
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
                <li v-for="pic in pictures" :key="pic">
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
</template>

<script>
export default {
  name: "Approval",
  props: [],
  data: function() {
    return {
      pictures: [],
      selectedPic: null,
      status: null,
      Locale: Locale
    };
  },
  methods: {
    init() {
      new ComService().getApprovalPictures(data => {
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
      if (this.selectedPic === null) return;

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
      if (this.selectedPic === null) return;

      new ComService().acceptApprovalPicture(this.selectedPic, data => {
        this.status = data.status;
        this.init();
      });
    },

    onDecline() {
      if (this.selectedPic === null) return;

      new ComService().declineApprovalPicture(this.selectedPic, data => {
        this.status = data.status;
        this.init();
      });
    },

    next() {
      if (this.hasNext) {
        index = this.pictures.indexOf(this.selectedPic);
        this.selectedPic = this.pictures[index + 1];
        this.scrollToPreview();
      }
    },

    previous() {
      if (this.hasPrevious) {
        index = this.pictures.indexOf(this.selectedPic);
        this.selectedPic = this.pictures[index - 1];
        this.scrollToPreview();
      }
    },

    stopZoom() {
      $.removeData($("#mainPic"), "elevateZoom");
      $(".zoomContainer").remove();
    }
  },
  computed: {
    hasNext: function() {
      index = this.pictures.indexOf(this.selectedPic);
      return index + 1 < this.pictures.length;
    },

    hasPrevious: function() {
      index = this.pictures.indexOf(this.selectedPic);
      return index - 1 >= 0;
    }
  },
  watch: {},
  created() {
    this.init();

    var that = this;

    // Add cursor key listeners for navigation
    document.onkeydown = function(e) {
      e = e || window.event;

      // Right/Next
      if (e.keyCode === 39 && that.hasNext) {
        that.next();
      }

      // Left/Previous
      if (e.keyCode === 37 && that.hasPrevious) {
        that.previous();
      }
    };
  },
  beforeDestroy: function() {
    this.stopZoom();
  }
};
</script>

<style>
#approval {
  margin: 15px;
  margin-top: -40px;
  height: 100%;
}

#approval .pictureNavigation {
  height: 200px;
  background-color: #2b2b2b;
  padding: 10px;

  overflow: hidden;
  overflow-x: visible;
  overflow-y: hidden;

  white-space: nowrap;
}

#approval ul {
  white-space: nowrap;
  padding: 0px;
}

#approval ul li {
  display: inline-block;
}

#approval img.isSelected {
  border: 3px solid;
  border-color: #337ab7bf;
}

#approval img.preview {
  position: relative;
  float: left;
  margin-right: 5px;
  border-radius: 3px;
  max-width: 240px;
  max-height: 160px;
  transition: 0.3s border;
}

#approval .picPanel {
  margin-bottom: 10px;
  height: calc(100vh - 310px);
  text-align: center;
}

#approval img.selectedPic {
  border-radius: 3px;
  max-width: 100%;
  max-height: 100%;
  vertical-align: middle;
  transition: 0.3s all;
}

#approval .judgepanel {
  position: fixed;
  bottom: 0px;
  width: 100%;
  background-color: #191717;
  margin-left: -15px;
}

#approval .judgeButton {
  float: left;
  width: 50%;
  text-align: center;
  padding: 10px;
  cursor: pointer;
  transition: 0.3s background-color;
}

#approval .judgeButton:hover {
  background-color: #262626;
}

#approval .judgepanel .spacer {
  width: 2px;
  height: 100%;
  position: absolute;
  background-color: #272525;
  left: 50%;
}

.green {
  color: #5cba5e;
}

.red {
  color: #a94442;
}

/* Animations */
.fadeIn-enter-active {
  transition: opacity 0.3s;
}

.fadeIn-enter {
  opacity: 0;
}

.fadeIn-leave-to,
.fadeIn-leave-active {
  display: none;
  transition: none;
  opacity: 0;
}
</style>
