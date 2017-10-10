const Sidemenu = Vue.component(
	'sidemenu', {
    template: `
		<div id="menu">
			<!-- Head menu -->
			<div class="headmenu" v-on:click="toggle()">
				<i style="font-size: 30px; vertical-align: middle;" 
					class="fa fa-camera-retro"></i>
				<img src="img/logo.png" />
			</div>

			<!-- Dark overlay -->
			<transition name="fade">
				<div class="menuoverlay" 
					v-on:click="toggle()"
					v-if="show"></div>
			</transition>
			
			<!-- Left menubar -->
			<transition name="slide">
				<div class="menubar" v-if="show">
					<div class="menubar_head">PHOBOX</div>

					<div 
						class="menuelement"
						v-for="entry in menuelements" 
						v-on:click="open(entry.address)">

						<a 
							:href="entry.address" 
							v-on:click="toggle()">
							{{ entry.title }}
						</a>
					</div>
				</div>
			</transition>
		</div>
    `,
    data: function() {
    	return {
    		show: false,
    		menuelements: [
	    		{ title: Locale.values.menu.pictures,	address: "#/photos" },
				{ title: Locale.values.menu.albums,		address: "#/albums" },
				{ title: Locale.values.menu.status,		address: "#/status" },
				{ title: Locale.values.menu.upload,		address: "#/upload" },
				{ title: Locale.values.menu.settings,	address: "#/settings" },
				{ title: Locale.values.menu.about, 		address: "#/about" },
    		],
    	};
    },
    methods: {
    	toggle : function() {
    		this.show = !this.show;
    	},

    	open : function(address) {
    		this.show = false;
    		window.location = address;
    	}
    },
    computed: {},
});