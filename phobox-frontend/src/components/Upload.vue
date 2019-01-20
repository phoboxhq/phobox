<template>
  <div id="upload">
    <vue-dropzone ref="myVueDropzone" 
      id="dropzone" 
      :options="dropzoneOptions">
    </vue-dropzone>
	</div>
</template>

<script>
import vue2Dropzone from 'vue2-dropzone'

export default {
  name: "Upload",
  components: {
    vueDropzone: vue2Dropzone
  },
  data() {
    return {
      dropzoneOptions: {
          url: process.env.SERVER_PATH + 'api/upload',
          thumbnailWidth: 150,
          maxFilesize: 50,
      }
    }
  },
};
</script>

<style>
@import '../../node_modules/vue2-dropzone/dist/vue2Dropzone.min.css';

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
