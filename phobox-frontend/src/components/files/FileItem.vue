<template>
  <div class="item_area">
    <div class="menu_button" v-on:click="toggleMenu()">
      <i class="fa fa-caret-square-o-down" aria-hidden="true" :id="item.name"></i>
    </div>

    <div class="item" v-on:click="open(item)" :id="encodePath">
      <!-- File: Show-->
      <img class="item_thumb" 
          :src="SERVER_PATH + item.thumb"
          v-if="item.type === 'file' && item.generatingThumb === false" />

      <!-- File: Show waiting image if the preview is not available-->
      <img class="item_thumb" 
          src="@/assets/stopwatch.png"
          v-if="item.generatingThumb" />

      <p v-if="item.generatingThumb" class="reloadInfo">
        {{ $t('pictures.waitforthumb') }}
      </p>

      <!-- Directory: Show preview if exists-->
      <img class="item_thumb" 
          :src="SERVER_PATH + item.preview"
          v-on:click="open(item)"
          v-if="item.type === 'dir' && item.preview != null" />
  
      <!-- Directory: Show static image if preview not exists-->
      <img class="item_thumb_static" 
          src="@/assets/directory.png"
          v-on:click="open(item)"                
          v-if="item.type === 'dir' && item.preview == null" />

      <!-- Directory: Show name -->
      <div class="item_name" v-if="item.type === 'dir'" v-on:click="open(item)">
        <i class="fa fa-folder"></i>
        <span>{{ item.name }}</span>
      </div>
    </div>

    <itemContextMenu ref="dirContextMenu" :item="item" :parent="$parent"></itemContextMenu>
	</div>
</template>

<script>
import ComService from '@/utils/ComService';
import ItemContextMenu from '@/components/files/ItemContextMenu';

export default {
  name: 'FileItem',
  components: {
    ItemContextMenu
  },
  props: ["item"],
  data: function() {
    return {
      menuShow: false,
      contextMenuItem: undefined,
      SERVER_PATH: process.env.SERVER_PATH
    };
  },
  methods: {
    open(item) {
      // Set selected item to parent component
      // if it's a file, otherwise open directory
      if (item.type === "file") {
        this.$parent.selectedItem = item;
      } else {
        let path = new ComService().encodePath(item.path);
        this.$router.push({ path: "/photos/" + path });
        window.scrollTo(0, 0);
      }
    },

    downloadPath() {
      if (this.item.type === "dir") {
        let com = new ComService();
        return "api/photos/download/" + com.encodePath(this.item.path);
      } else {
        return this.item.path;
      }
    },

    toggleMenu(id) {
      let contextMenu = this.$refs.dirContextMenu;
      contextMenu.toggleMenu();
    }
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
.item_area {
  position: relative;
  float: left;
}

.item .reloadInfo {
  font-size: 11px;
  padding: 10px;
}

.item {
  position: relative;
  float: left;
  margin: 5px;
  border-radius: 3px;
  background-color: rgb(29, 27, 27);
  width: 150px;
  height: 150px;
  overflow: hidden;
  border: 1px solid #1d1b1b;
  box-shadow: 0px 0px 0px rgba(0, 0, 0, 0.63);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.68, 0.97);
  cursor: pointer;
}

.item:hover {
  border: 1px solid rgba(75, 103, 121, 0.77);
  box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.63);
}
.item:hover > .menu_button {
  transition: display 0.5s;
  display: block;
}

.item_thumb_static {
  padding: 20px 15px;
  margin-left: 0px !important;
  margin-top: 0px !important;
}

.item_name {
  position: absolute;
  left: 0px;
  bottom: 0px;
  padding: 3px 5px;
  background-color: rgba(75, 103, 121, 0.77);
  text-shadow: 0px 0px 5px rgba(43, 43, 43, 0.31);
  border-radius: 0px 3px;
}

.item img {
  max-width: 150px;
  max-width: 225px;
  margin-top: 0px;
  margin-left: -20px;
}

.menu_button:hover {
  color: #ffffff;
  font-size: 25px;
}

.menu_button {
  right: 6px;
  position: absolute;
  z-index: 30;
  font-size: 20px;
  color: #f0f0f0;
  bottom: 5px;
  cursor: pointer;
  padding: 5px;
  transition: display 0.5s, font-size 0.3s;
}

.menu {
  position: absolute;
  background-color: #1d1b1b;
  bottom: -165px;
  right: 10px;
  z-index: 40;
  width: 140px;

  font-size: 14px;
  text-align: left;
  -webkit-background-clip: padding-box;
  background-clip: padding-box;
  border: 2px solid rgba(19, 19, 19, 0.61);
  border-radius: 4px;
  -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
}

.menu ul {
  list-style: none;
  padding: 0px;
}

.menu ul li a {
  color: #ffffff;
}

.menu ul li a:hover {
  text-decoration: none;
}

.menu ul li {
  padding: 10px;
  border-bottom: 1px solid rgba(35, 34, 34, 0.55);
  cursor: pointer;
  transition: all 0.3s;
}

.menu ul li i {
  padding-right: 5px;
}

.menu .menu_element:hover {
  background-color: #191717;
}
</style>
