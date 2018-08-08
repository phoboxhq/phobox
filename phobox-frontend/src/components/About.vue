<template>
  <div id="about">
		<div class="about_panel">
			<h1>{{ Locale.values.about.head }}</h1>
			<h2>{{ Locale.values.about.background_head }}</h2>
			<div v-html="Locale.values.about.background_text"></div>
		
			<h2>{{ Locale.values.about.author_and_contact_head }}</h2>
			<div v-html="Locale.values.about.author_and_contact_text"></div>
		</div>
	</div>
</template>

<script>
import Locale from '@/Locale';
Locale.init()

export default {
  name: "About",
  data: function() {
    return {
      Locale: Locale
    };
  }
};
</script>

<style>
#about {
  margin: 0 auto;
  max-width: 700px;
}

.about_panel {
  margin: -20px 10px;
  padding: 10px;
  border: 1px solid #0e0d0d;
  border-radius: 6px;
  background: #191717;
}

.about_panel p {
  text-align: justify;
}
</style>
