<template>
  <transition name="fade">
		<div id="lightbox" v-if="selectedItem" @keydown.prevent="onKeydown">
			<!-- Dark overlay -->
			<div
				class="menuoverlay"
				v-on:click="close()"></div>

			<!-- Lightbox window to presenting the picture -->
			<div id="lightbox_window">

				<!-- Current position of the picture -->
				<div class="picture_position">
					{{ index+1 }} {{ $t('pictures.of') }} {{ maxItems }}
				</div>

				<img
					id="lightbox_image"
					class="preview"
					:src="SERVER_PATH + selectedItem.thumb" />

				<div class="window_buttons">
					<!-- Close window -->
					<button type="button" class="close window_button"
						v-on:click="close()">
						<i class="fa fa-times" aria-hidden="true"></i>
					</button>

					<!-- Previous picture -->
					<button type="button" class="close window_button"
						v-on:click="previous()"
						:disabled="!hasPrevious">
						<i class="fa fa-arrow-left" aria-hidden="true"></i>
					</button>

					<!-- Next picture -->
					<button type="button" class="close window_button"
						v-on:click="next()"
						:disabled="!hasNext">
						<i class="fa fa-arrow-right" aria-hidden="true"></i>
					</button>

					<!-- Download picture -->
					<a :href="'ext' + selectedItem.path"
						class="close window_button" target="_blank" download>
						<i class="fa fa-download" aria-hidden="true"></i>
					</a>

					<!-- Favorite picture -->
					<button type="button" class="close window_button"
						v-on:click="onFavorite()">
						<i class="fa fa-star" aria-hidden="true"></i>
					</button>
				</div>
				
				<!-- Show a button to load more images -->
		        <div v-if="isFragment && !hasNext">
		            <button type="button" class="btn btn-secondary moreImagesBtn"
		                v-on:click="onLoadMoreItems()">
		                {{ $t('pictures.load_more') }}
		            </button>
		        </div>
			</div>
		</div>
	</transition>
</template>

<script>
import $ from 'jquery'
import Swipe from '@/utils/Swipe';

export default {
  name: "Lightbox",
  props: ["items", "selectedItem"],
  data() {
    return {
      swipe: null,
      SERVER_PATH: process.env.SERVER_PATH
    };
  },
  computed: {
    hasNext() {
      let index = this.items.indexOf(this.selectedItem);
      return index + 1 < this.items.length;
    },

    hasPrevious() {
      let index = this.items.indexOf(this.selectedItem);
      return index - 1 >= 0;
    },

    index() {
      return this.items.indexOf(this.selectedItem);
    },

    maxItems() {
      return this.items.length;
    },

    isFragment() {
      return this.$parent.isFragment;
    }
  },
  methods: {
    close() {
      this.$parent.selectedItem = null;
      this.swipe = null;
    },

    next() {
      if (this.hasNext) {
        let index = this.items.indexOf(this.selectedItem);
        this.$parent.selectedItem = this.items[index + 1];
        this.onShow();
      }
    },

    previous() {
      if (this.hasPrevious) {
        let index = this.items.indexOf(this.selectedItem);
        this.$parent.selectedItem = this.items[index - 1];
        this.onShow();
      }
    },

    onFavorite() {
      this.$parent.favoriteItem = this.selectedItem;
    },

    scrollToItem() {
      let id = this.selectedItem.path.replace(/\//g, "_");
      let element = document.getElementById(id);

      if (element !== undefined && element !== null) {
        element.scrollIntoView();
      }
    },

    onLoadMoreItems() {
      this.$parent.onLoadMoreItems();
    },

    onShow() {
      let img = new Image();
      img.src = this.selectedItem.thumb;

      let lightboxImage = document.getElementById("lightbox_image");

      if (img.width >= img.height) {
        lightboxImage.style.maxWidth = "100%";
        lightboxImage.style.maxHeight = undefined;
      } else {
        lightboxImage.style.maxHeight =
          window.innerHeight - window.innerHeight * 0.3 + "px";
        lightboxImage.style.maxWidth = undefined;
      }

      let lightboxWindow = document.getElementById("lightbox_window");
      let xPos = window.innerWidth / 2 - lightboxImage.width / 2;
      lightboxWindow.style.left = xPos + "px";

      this.scrollToItem();

      if (this.swipe == null) {
        this.addSwipeListener();
      }
    },

    addSwipeListener() {
      this.swipe = new Swipe(
        document.getElementById("lightbox_window"),
        () => {
          // on left
          this.next();
        },
        function() {
          // on right
          this.previous();
        }
      );
    }
  },

  updated() {
    if (this.selectedItem !== null) {
      this.onShow();
    }
  },

  created() {
    // Add cursor key listeners for navigation
    document.onkeydown = (e) => {
      e = e || window.event;

      // Right/Next
      if (e.keyCode === 39 && this.hasNext) {
        this.next();
      }

      // Left/Previous
      if (e.keyCode === 37 && this.hasPrevious) {
        this.previous();
      }
    };

    $(window).resize(() => {
      this.onShow();
    });
  },

  watch: {
    selectedItem() {}
  }
};
</script>

<style>
#lightbox {
  position: fixed;
  top: 0px;
  z-index: 100;
}

#lightbox #lightbox_window {
  position: fixed;
  top: 10%;
  /*left: 10%*/
  max-width: 80%;
  z-index: 110;

  transition: all 0.3s;
  box-shadow: 0px 0px 15px 0px black;
  border-radius: 3px;
}

#lightbox #lightbox_window img {
  transition: all 0.3s;
  border-radius: 3px;
}

.picture_position {
  position: absolute;
  right: 5px;
  top: -20px;
  opacity: 0.4;
  font-size: 10px;
}

.window_buttons {
  /* fixed on the right side */
  position: fixed;
  top: 10%;
  right: 0px;

  /* Floating with image
    position: absolute;
    top: -16px;
    right: -57px;
    float: left;
    */
  width: 65px;
}

.window_button {
  margin: 6px 10px;
  padding: 15px !important;

  color: #fff;
  content: "\00D7";
  opacity: 0.6 !important;
  transition: all 0.3s;
}

.close:focus,
.close:hover {
  color: #fff !important;
  opacity: 1 !important;
}

button:hover[disabled] {
  cursor: not-allowed;
}

button.close[disabled] {
  opacity: 0.3 !important;
}
</style>
