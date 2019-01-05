<template>
  <div id="app">
    
		<!-- Shows the side and top menu -->
    <main-menu></main-menu>

		<!-- Shows the directory content -->
		<transition name="fade">
      <router-view/>
		</transition>
  </div>
</template>

<script>
import MainMenu from '@/components/MainMenu'

export default {
  name: 'App', 
  components: {
   MainMenu
  }
}
</script>

<style>
@import '../node_modules/lato-webfont/lato.min.css';
@import '../node_modules/font-awesome/css/font-awesome.min.css';
@import '../node_modules/bootstrap-css-only/css/bootstrap.min.css';

#app {
  font-family: 'Lato', 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

body {
  background-color: #212121;
  color: #F0F0F0;
  font-size: 14px;
  margin-top: 95px;
  font-family: 'Lato', Arial, Helvetica, sans-serif !important;
  height: unset !important;
}

h1, h2, h3, h4, h5 {
  font-family: 'Lato';
  font-weight: 900;
}
</style>
