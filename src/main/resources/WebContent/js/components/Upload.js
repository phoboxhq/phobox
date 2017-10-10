const Upload = Vue.component(
	'dropzone', {
	template: `
	
	<div id="upload">
		<form id="dz" action="/api/upload" class="dropzone"></form>
	</div>
	`,
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
});