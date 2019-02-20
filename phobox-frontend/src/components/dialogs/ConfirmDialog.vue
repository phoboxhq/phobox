<template>
  <transition name="fade">
    <div>
      <!-- Dark overlay -->
      <div class="menuoverlay" v-on:click="close()"></div>
      
      <!-- DeleteConfirmDialog -->
      <div id="deleteConfirmDialog" class="dialog">
        <div class="head">
          {{ header }}
        </div>

        <div class="content">
          <div v-html="contextText" class="contextText"></div>
        </div>
      
        <div class="footer">
          <button type="button" class="btn btn-danger" @click="$emit('onConfirm')">{{ doItText }}</button>
          <button type="button" class="btn btn-default" @click="$emit('onCancel')">{{ cancelText }}</button>
        </div>
      </div>
    </div>
	</transition>
</template>

<script>
export default {
  name: "ConfirmDialog",
  props: ["header", "contextText", "doItText", "cancelText", "onConfirm", "onCancel"],
  data() {
    return {
    };
  },
  methods: {
  },
};
</script>

<style>
.dialog {
  position: fixed;
  z-index: 100;
  top: 10%;
  max-width: 500px;
  box-shadow: 0px 0px 15px 0px black;
  border-radius: 5px;
  margin: 15px auto;
  left: 0;
  right: 0;
  background-color: #212020;
}

.dialog .head {
  font-weight: bold;
  background-color: #151414;
  padding: 10px;
  border-radius: 5px 5px 0px 0px;
}

.dialog .content {
  padding: 10px;
}

.dialog .footer {
  padding: 10px;
  text-align: right;
}
</style>
