<template>
  <div class="contextmenu">
    <transition name="fade">
      <div class="menu" v-show="menuShow" v-click-outside="onClickOutside">
        <ul>
          <li class="menu_element" v-on:click="closeMenu()">
            <a class="downloadDirectory" :href="downloadPath()" target="_blank" download>
              <i class="fa fa-download" aria-hidden="true"></i> {{ $t('pictures.download') }}
            </a>
          </li>
          <li class="menu_element" v-on:click="onRename"><i class="fa fa-pencil" aria-hidden="true"></i> {{ $t('pictures.rename') }}</li>
          <li class="menu_element" v-on:click="onDelete"><i class="fa fa-trash" aria-hidden="true"></i> {{ $t('pictures.delete') }}</li>
          <li class="menu_element" v-on:click="onTags"><i class="fa fa-tags" aria-hidden="true"></i> {{ $t('pictures.tags') }}</li>
          <li class="menu_element" v-on:click="onFavorite"><i class="fa fa-star" aria-hidden="true"></i> {{ $t('pictures.album') }}</li>
        </ul>
      </div>
    </transition>
	</div>
</template>

<script>
import ComService from '@/utils/ComService';
import ClickOutside from '@/directives/ClickOutside';

export default {
  name: "ItemContextMenu",
  props: ["item", "parent"],
  directives: {
    ClickOutside
  },
  data() {
    return {
      contextMenuItem: undefined,
      menuShow: false
    };
  },
  methods: {
    downloadPath() {
      if (this.item == null) return;

      if (this.item.type === "dir") {
        let com = new ComService();
        return "api/photos/download/" + com.encodePath(this.item.path);
      } else {
        return this.item.path;
      }
    },

    toggleMenu(id) {
      this.menuShow = !this.menuShow;
    },

    closeMenu() {
      this.menuShow = false;
    },

    onClickOutside(e) {
      // Hide menu if no element is clicked and the contextmenu is open
      if (
        (e.srcElement != undefined || e.srcElement === null) &&
        (e.srcElement.id === "" || e.srcElement.id === "filebrowser") &&
        this.menuShow
      ) {
        this.closeMenu();
      }
    },

    onDownload() {
      this.closeMenu();
    },

    onRename() {
      this.parent.renameItem = this.item;
      this.closeMenu();
    },

    onDelete() {
      this.parent.deleteItem = this.item;
      this.closeMenu();
    },

    onTags() {
      this.parent.tagsItem = this.item;
      this.closeMenu();
    },

    onFavorite() {
      this.parent.favoriteItem = this.item;
      this.closeMenu();
    },
  },
  computed: {
    encodePath() {
      return this.item.path.replace(/\//g, "_");
    }
  },

  created() {}
};
</script>

<style>
</style>
