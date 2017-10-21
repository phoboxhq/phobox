const TagsDialog = Vue.component(
	'tagsDialog', {
	template: `

	<transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- TagsDialog -->
			<div id="renameDialog" class="dialog">
				<div class="head">
					{{ Locale.values.pictures.tags}} {{ getName() }}
				</div>

				<div class="content">
					<div class="form-group">
						<label for="newname">Name:</label>
						<input id="tagsInput" type="text" data-role="tagsinput" />

					</div>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
						<strong>{{ Locale.values.settings.failed }}!</strong> {{ Locale.values.pictures.rename_dialog_failed }}
					</div>
				</div>
			
				<div class="footer">
					<button type="button" class="btn btn-default" v-on:click="save()">{{ Locale.values.pictures.rename_dialog_save }}</button>
					<button type="button" class="btn btn-default" v-on:click="close()">{{ Locale.values.pictures.rename_dialog_cancel }}</button>
				</div>
			</div>
		</div>
	
	</transition>

	`,
	props: ['item'],
	data: function() {
		return {
			name: this.item !== null ? this.item.name : null,
			status: null,
			tags: [],
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
			this.tags = $("#tagsInput").tagsinput('items');

			new ComService().setTags(this.item.path, this.tags, function(data) {
				that.status = data.status;
				if(that.status === "OK") {
					that.close();
				}
			});
		},

		onShow: function() {
			var that = this;
			$('#tagsInput').tagsinput('removeAll');

			new ComService().getTags(this.item.path, function(data) {
				that.tags = data;
				that.tags.forEach(function(entry) {
					$("#tagsInput").tagsinput('add', entry);
				});
			});
		},

		close: function() {
			this.$parent.tagsItem = null;
		},
	},
	watch: {
	    'item': function() {
	    	this.name = this.getName();

	    	if(this.item !== null) {
	    		this.onShow();
	    	}
	    },
	},

});
