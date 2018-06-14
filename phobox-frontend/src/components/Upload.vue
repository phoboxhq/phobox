<template>
  <div id="upload">
		<form id="dz" action="/api/upload" class="dropzone"></form>
	</div>
</template>

<script>
export default {
  name: "Upload",
  methods: {
    initDropzone: function() {
      self = this;
      self.$nextTick(function() {
        self.dz = new Dropzone("#dz");
      });
    }
  },
  created: function() {
		// Disable auto initialisation, because a page (router) switch
		// does not provide the reinitialisation.
		Dropzone.autoDiscover = false;
		this.initDropzone();
	}
};
</script>

<style>
#upload {
	margin: 0 auto;
	max-width: 700px;
}

#upload .dropzone {
	background: #4c7ba2;
    font-weight: bold;
    font-size: 20px;
    border-radius: 10px;
    color: #f5f5f5;
    border-style: dashed;
    border-width: 3px;
}

#upload .dropzone .dz-preview .dz-image {
	overflow: inherit;
}
</style>
