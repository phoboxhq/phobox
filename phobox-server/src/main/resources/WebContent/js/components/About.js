const About = Vue.component(
	'', {
	template: `

	<div id="about">
		<div class="about_panel">
			<h1>{{ Locale.values.about.head }}</h1>
			<h2>{{ Locale.values.about.background_head }}</h2>
			<div v-html="Locale.values.about.background_text"></div>
		
			<h2>{{ Locale.values.about.author_and_contact_head }}</h2>
			<div v-html="Locale.values.about.author_and_contact_text"></div>
		</div>
	</div>
	`,
	data: function() {
		return {
			Locale: Locale,
		}
	}

});