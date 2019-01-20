<template>
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
					:placeholder="$t('search.searchbar')">

			<span class="input-group-btn">
				<button class="btn btn-secondary" type="button"
					v-on:click="onSearch()">{{ $t('search.search_button') }}</button>
			</span>
		</div>

		<!-- Result area -->
		<div id="foundItems">
				<span v-if="isEmptyResultSet">{{ $t('search.no_results') }}</span>
          <file-item v-for="item in items"
                :item="item"
                :key="item.path"
                :selectedItem="selectedItem"></file-item>
		</div>

		<!-- Include the lightbox -->
		<lightbox v-if="items"
				:items="items"
				:selectedItem="selectedItem"></lightbox>

	</div>
</template>

<script>
import Lightbox from '@/components/Lightbox';
import ComService from '@/utils/ComService';
import FileItem from '@/components/files/FileItem';

export default {
  name: "Search",
  components: {
    Lightbox,
    FileItem
  },
  data() {
    return {
      search: null,
      selectedItem: null,
      items: [],
      lastSearch: null,
      isLoading: false,
      SERVER_PATH: process.env.SERVER_PATH
    };
  },

  computed: {
    isEmptyResultSet: function() {
      return (
        this.search === this.lastSearch &&
        this.search !== null &&
        this.search.length > 0 &&
        this.items.length === 0
      );
    }
  },

  methods: {
    onSearch: function() {
      this.isLoading = true;
      new ComService().search(this.search, (data) => {
        this.items = data;
        this.lastSearch = this.search;
        this.isLoading = false;
      });
    }
  }
};
</script>

<style>
.search_panel {
  max-width: 80%;
  margin: 0 auto;
  margin-top: -30px;
  padding: 0px 10px;
}

#foundItems {
  margin-top: 10px;
}

img.loading {
  background-color: #ffffff;
  padding: 10px;
  border-radius: 3px;
  z-index: 100;
  position: fixed;
  top: 10px;
  right: 10px;
  transition: all 0.3s;
}
</style>
