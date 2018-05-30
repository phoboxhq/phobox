const ItemContextMenu = Vue.component(
	'itemContextMenu', {
    template: `
    <div class="contextmenu">
        <transition name="fade">
            <div class="menu" v-show="menuShow" v-click-outside="onClickOutside">
                <ul>
                    <li class="menu_element" v-on:click="closeMenu()">
                        <a class="downloadDirectory" :href="downloadPath()" target="_blank" download>
                            <i class="fa fa-download" aria-hidden="true"></i> {{ Locale.values.pictures.download }}
                        </a>
                    </li>
                    <li class="menu_element" v-on:click="onRename"><i class="fa fa-pencil" aria-hidden="true"></i> {{ Locale.values.pictures.rename }}</li>
                    <li class="menu_element" v-on:click="onDelete"><i class="fa fa-trash" aria-hidden="true"></i> {{ Locale.values.pictures.delete }}</li>
                    <li class="menu_element" v-on:click="onTags"><i class="fa fa-tags" aria-hidden="true"></i> {{ Locale.values.pictures.tags }}</li>
                </ul>
            </div>
        </transition>
	</div>
    `,
    props: ['item', 'parent'],
    data: function() {
    	return {
            contextMenuItem: undefined,
            menuShow: false,
            Locale: Locale,
        };
    },
    methods: {

        downloadPath: function() {
            if(this.item == null)
                return;

            if(this.item.type === "dir") {
                com = new ComService();
                return "api/photos/download/" + com.encodePath(this.item.path);
            
            } else {

                return this.item.path;
            }
        },
        
        toggleMenu: function(id) {
            this.menuShow = !this.menuShow; 
        },

        closeMenu: function() {
            this.menuShow = false;
        },

        onClickOutside: function(e) {
            // Hide menu if no element is clickt and the contextmenu is open
            if((e.srcElement.id === "" || e.srcElement.id === "filebrowser") && this.item.menuShow) {
                this.closeMenu();
            }
        },

        onDownload: function() {
            this.closeMenu();
        },

        onRename: function() {
            this.parent.renameItem = this.item;
            this.closeMenu();
        },
        
        onDelete: function() {
            this.parent.deleteItem = this.item;
            this.closeMenu();
        },

        onTags: function() {
            this.parent.tagsItem = this.item;
            this.closeMenu();
        },
    },
    computed: { 
        encodePath: function() {
            return (this.item.path).replace(/\//g , "_");
        },
    },

    created: function() {
    }
});