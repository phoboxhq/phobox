const Lightbox = Vue.component(
	'lightbox', {
	template: `
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
					{{ index+1 }} {{ Locale.values.pictures.of }} {{ maxItems }}
				</div>

				<img
					id="lightbox_image"
					class="preview"
					:src="selectedItem.thumb" />

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
					<a :href="selectedItem.path"
						class="close window_button" target="_blank" download>
						<i class="fa fa-download" aria-hidden="true"></i>
					</a>

					<!-- Favorite picture -->
					<button type="button" class="close window_button"
						v-on:click="onFavorite()">
						<i class="fa fa-star" aria-hidden="true"></i>
					</button>
				</div>
			</div>
		</div>
	</transition>
	`,
	props: ['items', 'selectedItem'],
	data: function() {
		return {
			Locale: Locale,
		}
	},
	computed: {
		hasNext: function() {
			index = this.items.indexOf(this.selectedItem);
			return index+1 < this.items.length;
		},

		hasPrevious: function() {
			index = this.items.indexOf(this.selectedItem);
			return index-1 >= 0;
		},

		index: function() {
			return this.items.indexOf(this.selectedItem);
		},

		maxItems: function() {
			return this.items.length;
		},
	},
	methods: {
		close: function() {
			this.$parent.selectedItem = null;
		},

		next: function() {
			if(this.hasNext) {
				index = this.items.indexOf(this.selectedItem);
				this.$parent.selectedItem = this.items[index+1];
			}
		},

		previous: function() {
			if(this.hasPrevious) {
				index = this.items.indexOf(this.selectedItem);
				this.$parent.selectedItem = this.items[index-1];
			}
		},

		onFavorite: function() {
			this.$parent.favoriteItem = this.selectedItem;
		},

		onShow: function() {
			if(this.selectedItem.landscape != null) {
			}

			var img = new Image();
			img.src = this.selectedItem.thumb;

			var lightboxImage = document.getElementById("lightbox_image");
			lightboxImage.style.transition = "all 0.3s";

			var lightboxWindow = document.getElementById("lightbox_window");
			var xPos = window.innerWidth/2 - lightboxWindow.clientWidth/2;
			lightboxWindow.style.left = xPos + "px";

			if(this.selectedItem.landscape != null && this.selectedItem.landscape || img.width >= img.height) {
				// landscape orientation
				lightboxImage.style.maxWidth = "100%";
				lightboxImage.style.maxHeight = undefined;

			} else {
				// portrait orientation
				lightboxImage.style.maxWidth = undefined;
				lightboxImage.style.maxHeight = window.innerHeight - (window.innerHeight*0.3) + "px";
			}
		},
	},

	updated: function() {
		if(this.selectedItem !== null) {
			this.onShow();
		}
	},

	created: function() {
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

	watch: {
		'selectedItem': function() {
		},
	},
})
