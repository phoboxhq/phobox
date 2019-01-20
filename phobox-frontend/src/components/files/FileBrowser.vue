<template>
  <div id="filebrowser">
    <breadcrumb :path="path"></breadcrumb>

    <!-- Contextmenu for open directory -->
    <div class="directoryOptions" v-if="currentPath != '/'" >
        <div class="menu_button" v-on:click="onToggleDirectoryMenu()">
            <i class="fa fa-caret-square-o-down" aria-hidden="true" id="currentItemContextButton"></i>
        </div>
        <itemContextMenu ref="dirContextMenu" :item="currentItem" :parent="this"></itemContextMenu>
    </div>

    <transition name="fade">
        <!-- List all elements as FileItem -->
        <div class="items">
            <file-item v-for="(item, key) in items"
                :item="item"
                :key="key"
                :selectedItem="selectedItem"></file-item>
        </div>
    </transition>

    <!-- Show a button to load more images -->
    <div v-if="isFragment && fragmentSize >= pageSize" class="moreImagesContainer">
        <button type="button" class="btn btn-secondary moreImagesBtn"
            v-on:click="onLoadMoreItems()">
            {{ $t('pictures.load_more') }}
        </button>
    </div>

    <!-- Loading image -->
    <transition name="fade">
        <img src="@/assets/loading.gif" v-if="isLoading" class="loading" />
    </transition>

    <!-- Include the lightbox -->
    <lightbox
        :items="items"
        :selectedItem="selectedItem"></lightbox>

    <div class="not_found_center"
        v-if="isItemListEmpty && !isProcessing">
        <i style="font-size: 20em; color: rgba(0, 0, 0, 0.27);"
            class="fa fa-frown-o" aria-hidden="true"></i>
        <h4>{{ $t('pictures.not_found') }}</h4>
        <a href="#"
            v-on:click="goBack()">
            {{ $t('pictures.go_back') }}</a>
    </div>

    <div class="not_found_center"
        v-if="isItemListEmpty && isProcessing">
        <i style="font-size: 20em; color: rgba(0, 0, 0, 0.27);"
            class="fa fa-spinner" aria-hidden="true"></i>
        <h4>{{ $t('pictures.not_found') }}</h4>
        <a v-on:click="refresh()" style="cursor:pointer;">
            {{ $t('pictures.reload') }}</a>
    </div>

    <renameDialog :item="renameItem"></renameDialog>

    <deleteConfirmDialog :item="deleteItem"></deleteConfirmDialog>
    <favoriteDialog :item="favoriteItem"></favoriteDialog>

    <tagsDialog :item="tagsItem"></tagsDialog>

  </div>
</template>

<script>
import ComService from '@/utils/ComService';
import Breadcrumb from '@/components/files/Breadcrumb';
import FileItem from '@/components/files/FileItem';
import ItemContextMenu from '@/components/files/ItemContextMenu';
import RenameDialog from '@/components/dialogs/RenameDialog';
import DeleteConfirmDialog from '@/components/dialogs/DeleteConfirmDialog';
import FavoriteDialog from '@/components/dialogs/FavoriteDialog';
import TagsDialog from '@/components/dialogs/TagsDialog';
import Lightbox from '@/components/Lightbox';

export default {
  name: "FileBrowser",
  components: {
    Breadcrumb,
    FileItem,
    ItemContextMenu,
    RenameDialog,
    DeleteConfirmDialog,
    TagsDialog,
    FavoriteDialog,
    Lightbox
  },
  props: [],
  data() {
    return {
      items: null,
      path: null,
      pageSize: 30,
      currentPath: null,
      selectedItem: null,
      renameItem: null,
      deleteItem: null,
      tagsItem: null,
      favoriteItem: null,
      currentItem: null,
      isLoading: true,
      isFragment: false,
      fragmentSize: 0,
      isProcessing: false,
      page: 1
    };
  },
  methods: {
    refresh () {
      this.path = this.$route.params.path;
      this.scan(this.path);
    },

    scan (path) {
      this.isLoading = true;
      new ComService().scan(path, (data) => {
        this.items = data.items;
        this.isLoading = false;
        this.isFragment = data.fragment;
        this.fragmentSize = data.items.length;
        this.currentPath = data.path;
        this.isProcessing = data.processing;
        this.currentItem = data;
        this.page = 1;
      });
    },

    onLoadMoreItems () {
      this.isLoading = true;
      new ComService().loadMore(this.currentPath, this.page, this.pageSize, (data) => {
        this.items = this.items.concat(data.items);
        this.isLoading = false;
        this.isFragment = data.fragment;
        this.fragmentSize = data.items.length;
        this.currentPath = data.path;
        this.currentItem = data;
        this.page += 1;
      });
    },

    goBack () {
      this.$router.go(-1);
    },

    onToggleDirectoryMenu () {
      let contextMenu = this.$refs.dirContextMenu;
      contextMenu.toggleMenu();
    }
  },
  computed: {
    isItemListEmpty () {
      return this.items === null || this.items.length === 0;
    }
  },
  watch: {
    '$route': "refresh"
  },
  created () {
    this.refresh();
  }
};
</script>

<style>
.pbreadcrumb {
  position: fixed;
  z-index: 80;
  top: 46px;
  background-color: #1b1b1b;
  width: 100%;
  height: 45px;
  padding: 5px;
  padding-left: 10px;
  box-shadow: 0 0 8px 6px #151515c7;
}

.pbreadcrumb .bc_element {
  float: left;
  background-color: rgba(66, 66, 66, 0.35);
  padding: 6px;
  margin-right: 3px;
  border-radius: 2px;
  color: #b5b5b5;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.68, 0.97);
  cursor: pointer;
}

.pbreadcrumb .bc_element:first-child {
  width: 50px;
  text-align: center;
}

.pbreadcrumb .bc_element:hover {
  background-color: rgba(82, 82, 82, 0.35);
  box-shadow: 0px 0px 0px 4px rgba(41, 41, 41, 0.25);
}

.items {
  padding: 7px;
}

.btn {
  background-color: #337ab7;
}

.moreImagesBtn {
  margin: 15px auto;
}

.moreImagesContainer {
  clear: both;
  text-align: center;
}

.not_found_center {
  width: 300px;
  margin: 0 auto;
  text-align: center;
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

.btn {
  color: #ffffff;
  background-color: #337ab7;
  border-color: #1d4d75;
}

.btn:hover {
  color: #ffffff;
  background-color: #2e5f8a;
  border-color: #1d4d75;
}

.directoryOptions {
  position: fixed;
  top: 92px;
  width: 100px;
  right: 5px;
  z-index: 90;
}
</style>
