<template>
  <div class="pbreadcrumb">
    <div class="bc_element" 
        v-for="e in generateElements" 
        :key="e.path"
        v-on:click="open(e.path)">
        
        {{ e.name }}
    </div>
	</div>
</template>

<script>
import ComService from '@/utils/ComService';

export default {
  name: "Breadcrumb",
  props: ["path"],
  data: function() {
    return {};
  },
  methods: {
    open(path) {
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
    }
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

<style>
</style>
