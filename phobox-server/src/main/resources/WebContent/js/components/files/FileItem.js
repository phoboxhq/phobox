const FileItem = Vue.component(
	'fileitem', {
    template: `
    <div class="item_area">
        
        <div class="menu_button" v-on:click="toggleMenu()">
            <i class="fa fa-caret-square-o-down" aria-hidden="true" :id="item.name"></i>
        </div>

        <div class="item" v-on:click="open(item)" :id="encodePath">
            <!-- File: Show-->
            <img class="item_thumb" 
                :src="item.thumb"
                v-if="item.type === 'file'" />

            <p v-if="item.thumb==='img/stopwatch.png'" class="reloadInfo">
                {{ Locale.values.pictures.waitforthumb }}
            </p>

            <!-- Directory: Show preview if exists-->
            <img class="item_thumb" 
                :src="item.preview"
                v-on:click="open(item)"
                v-if="item.type === 'dir' && item.preview != null" />
        
            <!-- Directory: Show static image if preview not exists-->
            <img class="item_thumb_static" 
                src="img/directory.png"
                v-on:click="open(item)"                
                v-if="item.type === 'dir' && item.preview == null" />

            <!-- Directory: Show name -->
            <div class="item_name" v-if="item.type === 'dir'" v-on:click="open(item)">
                <i class="fa fa-folder"></i>
                <span>{{ item.name }}</span>
            </div>
        </div>

        <itemContextMenu ref="dirContextMenu" :item="item" :parent="$parent"></itemContextMenu>
	</div>
    `,
    props: ['item'],
    data: function() {
    	return {
            menuShow: false,
            contextMenuItem: undefined,
            Locale: Locale,
        };
    },
    methods: {
        open : function(item) {
            // Set selected item to parent component
            // if it's a file, otherwise open directory
            if(item.type === 'file') {
                this.$parent.selectedItem = item;
            } else {
                path = new ComService().encodePath(item.path);
                router.push({ path: "/photos/"+path });
                window.scrollTo(0, 0);
            }
        },

        downloadPath: function() {
            
            if(this.item.type === "dir") {
                com = new ComService();
                return "api/photos/download/" + com.encodePath(this.item.path);
            
            } else {

                return this.item.path;
            }
        },
        
        toggleMenu: function(id) {
            let contextMenu = this.$refs.dirContextMenu; 
            contextMenu.toggleMenu();
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