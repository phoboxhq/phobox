<template>
  <div class="pbreadcrumb">
    <div class="bc_element" v-for="e in generateElements" :key="e.path">
      <div class="bc_content openDirectories" v-on:click="toggleDirectryList(e.path, $event)">
        <i class="material-icons">expand_more</i>
      </div>
      <div class="bc_content directoryName" v-on:click="open(e.path, true)">{{ e.name }}</div>
    </div>

    <transition name="fade">
      <ul id="directoryList" v-show="directories" v-click-outside="onClickOutside">
        <li
          v-for="dir in directories"
          :key="dir.path"
          class="directoryListElement"
          v-on:click="open(dir.path, false)"
        >
          <i class="material-icons">folder</i>
          {{ dir.name }}
        </li>
        <li v-show="directories && directories.length === 0" class="directoryListElement">
          <i class="material-icons">error_outline</i>
         {{ $t('menu.not_subdirectories') }}
        </li>
      </ul>
    </transition>
  </div>
</template>

<script>
import ComService from "@/utils/ComService";

export default {
  name: "Breadcrumb",
  props: ["path"],
  data: function() {
    return {
      lastPath: null,
      directories: null
    };
  },
  methods: {
    open(path, closeDirectoryList) {
      if(closeDirectoryList)
        this.closeDirectoryList();

      let com = new ComService();
      let encodedUrl = com.encodePath(this.$route.params.path);
      let encodedPath = com.encodePath(path);

      if (encodedUrl === encodedPath) {
        // Reload item list for the current url
        this.$parent.scan(path);
      } else {
        // Navigate to the requested path
        this.$router.push({ path: "/photos/" + encodedPath });
      }
    },

    toggleDirectryList(path, event) {
      this.closeDirectoryList();

      if (path === this.lastPath) {
        this.lastPath = null;
        return;
      }

      new ComService().getDirectories(path, data => {
        this.directories = data;
        this.lastPath = path;

        let sourceElement = event.srcElement;
        if (sourceElement.className !== "openDirectories")
          sourceElement = sourceElement.parentElement;
        
        let list = document.getElementById("directoryList");
        list.style.left = sourceElement.getBoundingClientRect().left + "px";
        // list.style.opacity = "1";
      });
    },

    closeDirectoryList() {
      this.directories = null
    },

    onClickOutside() {
      this.closeDirectoryList();
      this.lastPath = null;
    },

  },
  computed: {
    generateElements() {
      let path = new ComService()
        .decodePath(this.path)
        .split("/")
        .slice(0);

      path = path.filter(function(n) {
        return n != "";
      });

      let elements = [];
      elements.push({ path: "%252F", name: "/" });

      if (path.length) {
        let tmpPath = "";
        path.forEach(function(f) {
          tmpPath = tmpPath + "%252F" + f;
          if (f.length > 15) {
            f = f.substr(0, 15) + " ...";
          }
          elements.push({ path: tmpPath, name: f });
        });
      }
      return elements;
    }
  }
};
</script>

<style scoped>
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
  margin-right: 4px;
  cursor: pointer;
  border-radius: 2px;
}

.pbreadcrumb .bc_content {
  background-color: rgb(41, 41, 41);
  color: #b5b5b5;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.68, 0.97);
  text-align: center;
}

.directoryName {
  min-width: 50px;
}

.pbreadcrumb .bc_content:hover {
  background-color: rgba(82, 82, 82, 0.35);
  box-shadow: 0px 0px 0px 4px rgba(41, 41, 41, 0.25);
}

.openDirectories {
  display: inline-block;
  padding: 7px 5px 5px 5px;
}
.openDirectories i {
  transform: translateY(16%);
  font-size: 17px;
}

.directoryName {
  display: inline-block;
  padding: 10px 10px 5px 10px;
  margin-left: -4px;
}

#directoryList {
  position: absolute;
  background-color: #292929e0;
  top: 44px;
  list-style: none;
  padding-left: 0px;
  box-shadow: 0px 0px 0px 4px rgba(41, 41, 41, 0.25);
  min-width: 110px;
}

.directoryListElement {
  cursor: pointer;
  padding: 10px;
}

.directoryListElement i {
  font-size: 12px;
  padding-right: 5px;
}

.directoryListElement:hover {
  background-color: rgba(82, 82, 82, 0.35);
}
</style>
