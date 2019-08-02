<template>
	<div class="search_panel">

		<!-- Search area -->
		<div class="input-group">
			<input id="searchinput"
					type="text" class="form-control"
					v-model="search"
					v-on:keyup.enter="onSearch(true)"
					:placeholder="$t('search.searchbar')">

			<span class="input-group-btn">
				<button class="btn btn-secondary" type="button"
					v-on:click="onSearch(true)">{{ $t('search.search_button') }}</button>
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
    
		<!-- Loading image -->
		<transition name="fade">
      <div class="moreImagesContainer">
				<img src="@/assets/loading.gif" v-if="isLoading" class="loading" />
      </div>
		</transition>

    <!-- Show a button to load more images -->
    <div v-if="items.length > 0 && items.length === ((page-1)*pageSize)" class="moreImagesContainer">
        <button type="button" class="btn btn-secondary moreImagesBtn"
            v-on:click="onSearch(false)">
            {{ $t('pictures.load_more') }}
        </button>
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
      page: 1,
      pageSize: 100,
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

  created() {
      this.focusInput('searchinput');
  },

  methods: {
    onSearch(newSearch) {
      this.isLoading = true;
      this.page = newSearch ? 1 : this.page;

      new ComService().search(this.search, this.page, this.pageSize, (data) => {
        this.items = newSearch ? data : this.items.concat(data);
        this.lastSearch = this.search;
        this.isLoading = false;
        this.page += 1;
      });
    },

    focusInput(inputRefId) {
      setTimeout(() =>{
        if(this.$refs[inputRefId])
          this.$nextTick(() => this.$refs[inputRefId].focus());
      }, 100);
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
  margin-top: 10px;
  margin-bottom: 10px;
  border-radius: 3px;
  transition: all 0.3s;
  text-align: center;
}

.moreImagesContainer {
  clear: both;
  text-align: center;
}
</style>
