const DeleteConfirmDialog = Vue.component(
	'deleteConfirmDialog', {
	template: `

	<transition name="fade">

		<div v-show="item">
			
			<!-- Dark overlay -->
			<div class="menuoverlay" v-on:click="close()"></div>
			
			<!-- DeleteConfirmDialog -->
			<div id="deleteConfirmDialog" class="dialog">
				<div class="head">
					{{ Locale.values.pictures.delete_dialog_head }} {{ getName() }}
				</div>

				<div class="content">
					{{ Locale.values.pictures.delete_dialog_content }}
					<p><strong>'{{ getPath() }}'</strong></p>

					<div class="alert alert-danger alert-dismissible" role="alert" v-show="status === 'ERROR'">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close" v-on:click="status = null"><span aria-hidden="true">&times;</span></button>
						<strong>{{ Locale.values.settings.failed }}!</strong> {{ Locale.values.pictures.delete_dialog_failed }}
					</div>
				</div>
			
				<div class="footer">
					<button type="button" class="btn btn-danger" v-on:click="deleteItem()">{{ Locale.values.pictures.delete_dialog_delete }}</button>
					<button type="button" class="btn btn-default" v-on:click="close()">{{ Locale.values.pictures.delete_dialog_cancel }}</button>
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

		getPath: function() {
			if(this.item !== null) {
				return this.item.path;
			}
			return "";
		},

		deleteItem: function() {
			var that = this;
			new ComService().delete(this.item.path, function(data) {
				that.status = data.status;

				if(that.status === "OK") {
					index = that.$parent.items.indexOf(that.item);

					if(index >= 0) {
						that.$parent.items.splice(index, 1);
					}
	
					that.close();
				}
			});
		},

		close: function() {
			this.$parent.deleteItem = null;
		},
	},
	watch: {
	    'item': function() {
	    	this.name = this.getName();
	    },
	},

});
