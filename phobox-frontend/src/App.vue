<template>
  <div id="app">
    
		<!-- Shows the side and top menu -->
		<menu></menu>

		<!-- Shows the directory content -->
		<transition name="fade">
      <router-view/>
		</transition>
  </div>
</template>

<script>

import Menu from '@/components/Menu'

export default {
  name: 'App',
  components: {
    Menu
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
