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
					<div class="menubar_head">
						<i class="fa fa-camera-retro" style="margin-right:5px"></i>PHOBOX
					</div>

					<div
						class="menuelement"
						v-for="entry in menuelements"
						v-on:click="open(entry.address)">

						<i :class="entry.icon" style="margin-right:5px"></i>
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
					{ title: Locale.values.menu.pictures,	address: "#/photos",	icon: "fa fa-folder-open" },
					{ title: Locale.values.menu.approval,	address: "#/approval",	icon: "fa fa-gavel" },
					{ title: Locale.values.menu.search,		address: "#/search",	icon: "fa fa-search" },
					{ title: Locale.values.menu.albums,		address: "#/albums",	icon: "fa fa-star" },
					{ title: Locale.values.menu.status,		address: "#/status",	icon: "fa fa-heartbeat" },
					{ title: Locale.values.menu.upload,		address: "#/upload",	icon: "fa fa-upload" },
					{ title: Locale.values.menu.settings,	address: "#/settings",  icon: "fa fa-wrench" },
					{ title: Locale.values.menu.about, 		address: "#/about",		icon: "fa fa-info-circle" },
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
