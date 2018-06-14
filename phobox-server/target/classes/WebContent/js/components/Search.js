const Search = Vue.component(
	'Search', {
	template: `

	<div class="search_panel">

		<!-- Loading image -->
		<transition name="fade">
				<img src="img/loading.gif" v-if="isLoading" class="loading" />
		</transition>

		<!-- Search area -->
		<div class="input-group">
			<input id="searchinput"
					type="text" class="form-control"
					v-model="search"
					v-on:keyup.enter="onSearch()"
					:placeholder="Locale.values.search.searchbar">

			<span class="input-group-btn">
				<button class="btn btn-secondary" type="button"
					v-on:click="onSearch()">{{ Locale.values.search.search_button }}</button>
			</span>
		</div>

		<!-- Result area -->
		<div id="foundItems">
				<span v-if="isEmptyResultSet">{{ Locale.values.search.no_results }}</span>
				<div class="albumelement item" v-for="item in items" v-if="items">
						<img class="item_thumb"
								:src="item.thumb"
								v-on:click="selectedItem = item"
								v-if="item.type === 'file'" />
				</div>
		</div>

		<!-- Include the lightbox -->
		<lightbox v-if="items"
				:items="items"
				:selectedItem="selectedItem"></lightbox>

	</div>
	`,

	data: function() {
		return {
			search: null,
			selectedItem: null,
			items: [],
			lastSearch: null,
			isLoading: false,
			Locale: Locale,
		}
	},

	computed: {
		isEmptyResultSet: function() {
			return this.search === this.lastSearch
					&& this.search !== null
					&& this.search.length > 0
					&& this.items.length === 0;
		}
	},

	methods: {
		onSearch: function() {
			that = this;
			this.isLoading = true;
			new ComService().search(this.search, function(data) {
				that.items = data;
				that.lastSearch = that.search;
				that.isLoading = false;
			});
		}
	},
});
