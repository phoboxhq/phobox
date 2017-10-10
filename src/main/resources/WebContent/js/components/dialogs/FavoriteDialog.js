const FavoriteDialog = Vue.component(
	'favoriteDialog', {
	template: `

	<transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- FavoriteDialog -->
			<div id="favoriteDialog" class="dialog">
				<div class="head">
					{{ Locale.values.pictures.favorite_dialog_head }} {{ getName() }}
				</div>

				<div class="content">
					<!-- Album type switch -->
					<div style="padding:5px;">
						<label class="radio-inline"><input type="radio" value="newAlbum" v-model="albumSwitch">{{ Locale.values.pictures.favorite_dialog_new }}</label>
						<label class="radio-inline"><input type="radio" value="existingAlbum" v-model="albumSwitch">{{ Locale.values.pictures.favorite_dialog_existing }}</label>
					</div>

					<!-- New album input -->
					<div class="form-group" v-if="albumSwitch === 'newAlbum'">
						<label for="newname">{{ Locale.values.pictures.favorite_dialog_name }}:</label>
						<input type="text" class="form-control" id="newname" v-model="albumname">
					</div>

					<!-- Existing album selector -->
				    <div class="form-group" v-if="albumSwitch === 'existingAlbum'">
						<label for="sel2">{{ Locale.values.pictures.favorite_dialog_select }}:</label>
				        <select class="form-control" id="sel2" v-model="albumname">
				        	<option v-for="a in albums" :value="a" :label="a">
				        </select>
				    </div>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
						<strong>{{ Locale.values.settings.failed }}!</strong> {{ Locale.values.pictures.rename_dialog_failed }}
					</div>
				</div>
			
				<div class="footer">
					<button type="button" class="btn btn-default" v-on:click="save()">{{ Locale.values.pictures.favorite_dialog_add }}</button>
					<button type="button" class="btn btn-default" v-on:click="close()">{{ Locale.values.pictures.rename_dialog_cancel }}</button>
				</div>
			</div>
		</div>
	
	</transition>

	`,
	props: ['item'],
	data: function() {
		return {
			albums: [],
			albumSwitch: "existingAlbum",
			albumname: null,
			status: null,
			Locale: Locale,
		}
	},
	methods: {
		getName: function() {
			if(this.item !== null) {
				return this.item.name;
			}
			return "";
		},

		save: function() {
			var that = this;
			new ComService().addToAlbum(this.item.path, this.albumname, function(data) {
				that.status = data.status;

				if(that.status === "OK") {
					that.close();
				}
			});
		},

		close: function() {
			this.$parent.favoriteItem = null;
		},
	},
	watch: {
	    'item': function() {
	    	this.name = this.getName();
	    },
	},
	created: function() {
		that = this;
		new ComService().getAlbums(function(data) {
            that.albums = data;
        });
	}

});
