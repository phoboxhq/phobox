<template>
  <div id="menu">
    <!-- Head menu -->
    <div class="headmenu" v-on:click="toggle()">
      <!-- <i style="font-size: 30px; vertical-align: middle;"
        class="fa fa-camera-retro"></i> -->
      <img src="@/assets/logo.png" />
    </div>

    <!-- Dark overlay -->
    <transition name="fade">
      <div class="menuoverlay"
        v-on:click="toggle()"
        v-if="show"></div>
    </transition>

    <!-- Left menubar -->
    <transition name="slide">
      <div class="menubar" v-if="show">
        <div class="menubar_head">
          <i class="fa fa-camera-retro" style="margin-right:5px"></i>PHOBOX
        </div>

        <div
          class="menuelement"
          v-for="(entry, key) in menuelements" :key="key"
          v-on:click="open(entry.address)">

          <i :class="entry.icon" style="margin-right:5px"></i>
          <a
            :href="entry.address"
            v-on:click="toggle()">
            {{ entry.title }}
          </a>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: "MainMenu",
  data: function() {
    return {
      show: false,
      menuelements: [
        {
          title: this.$t('menu.pictures'),
          address: "#/photos",
          icon: "fa fa-folder-open"
        },
        {
          title: this.$t('menu.approval'),
          address: "#/approval",
          icon: "fa fa-gavel"
        },
        {
          title: this.$t('menu.albums'),
          address: "#/albums",
          icon: "fa fa-star"
        },
        {
          title: this.$t('menu.upload'),
          address: "#/upload",
          icon: "fa fa-upload"
        },
        {
          title: this.$t('menu.search'),
          address: "#/search",
          icon: "fa fa-search"
        },
        {
          title: this.$t('menu.statistics'),
          address: "#/statistics",
          icon: "fa fa-pie-chart"
        },
        {
          title: this.$t('menu.status'),
          address: "#/status",
          icon: "fa fa-heartbeat"
        },
        {
          title: this.$t('menu.settings'),
          address: "#/settings",
          icon: "fa fa-wrench"
        },
        {
          title: this.$t('menu.about'),
          address: "#/about",
          icon: "fa fa-info-circle"
        }
      ]
    };
  },
  methods: {
    toggle() {
      this.show = !this.show;
    },

    open(address) {
      this.show = false;
      window.location = address;
    }
  },
  computed: {}
};
</script>

<style>
#menu {
  z-index: 100;
  position: fixed;
}

.headmenu {
  position: fixed;
  top: 0px;
  padding: 8px;
  background-color: #171717;
  width: 100%;
  cursor: pointer;
}

.menuoverlay {
  background-color: rgba(0, 0, 0, 0.9);
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0px;
  left: 0px;
  z-index: 100;
}

.menubar {
  color: #ffffff;
  transition: all 0.5s;
  background-color: #111010;
  position: fixed;
  top: 0px;
  left: 0px;
  height: 100%;
  width: 200px;
  z-index: 110;
}

.menubar_head {
  background-color: #337ab7;
  padding: 10px;
  font-weight: bold;
}

.menuelement {
  padding: 10px;
  padding-left: 30px;
  border-bottom: solid 1px #171717;
  transition: all 0.5s ease;
}

.menuelement a {
  color: #ffffff;
}

.menuelement:hover {
  background-color: #151515;
}

.menuelement a:hover {
  text-decoration: none;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s;
}
.slide-enter,
.slide-leave-to {
  transform: translateX(-100%);
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}
</style>
