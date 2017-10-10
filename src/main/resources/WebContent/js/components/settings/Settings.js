const Settings = Vue.component(
	'settings', {
	template: `

	<div id="settings_panel">
		<h1>{{ Locale.values.menu.settings }}</h1>
		<securityaccess></securityaccess>
		<importformat></importformat>
		<createthumbnails></createthumbnails>
		<changestorage></changestorage>
	</div>
	`,
	data: function() {
		return {
			Locale: Locale,
		}
	},
	
	computed: {},
	
	methods: {

	}
});