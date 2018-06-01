const FileBrowser = Vue.component(
    'filebrowser', {
    template: `
    <div id="filebrowser">
        <breadcrumb :path="path"></breadcrumb>

        <!-- Contextmenu for open directory -->
        <div class="directoryOptions" v-if="currentPath != '/'" >
            <div class="menu_button" v-on:click="onToggleDirectoryMenu()">
                <i class="fa fa-caret-square-o-down" aria-hidden="true" id="currentItemContextButton"></i>
            </div>
            <itemContextMenu ref="dirContextMenu" :item="currentItem" :parent="this"></itemContextMenu>
        </div>

        <transition name="fade">
            <!-- List all elements as FileItem -->
            <div class="items">
                <fileitem v-for="item in items"
                    :item="item"
                    :selectedItem="selectedItem"></fileitem>
            </div>
        </transition>

        <!-- Show a button to load more images -->
        <div v-if="isFragment" class="moreImagesContainer">
            <button type="button" class="btn btn-secondary moreImagesBtn"
                v-on:click="onLoadMoreItems()">
                {{ Locale.values.pictures.load_more }}
            </button>
        </div>

        <!-- Loading image -->
        <transition name="fade">
            <img src="img/loading.gif" v-if="isLoading" class="loading" />
        </transition>

        <!-- Include the lightbox -->
        <lightbox
            :items="items"
            :selectedItem="selectedItem"></lightbox>

        <div class="not_found_center"
            v-if="isItemListEmpty && !isProcessing">
            <i style="font-size: 20em; color: rgba(0, 0, 0, 0.27);"
                class="fa fa-frown-o" aria-hidden="true"></i>
            <h4>{{ Locale.values.pictures.not_found }}</h4>
            <a href=""
                v-on:click="goBack()">
                {{ Locale.values.pictures.go_back }}</a>
        </div>

        <div class="not_found_center"
            v-if="isItemListEmpty && isProcessing">
            <i style="font-size: 20em; color: rgba(0, 0, 0, 0.27);"
                class="fa fa-spinner" aria-hidden="true"></i>
            <h4>{{ Locale.values.pictures.not_found }}</h4>
            <a v-on:click="refresh()" style="cursor:pointer;">
                {{ Locale.values.pictures.reload }}</a>
        </div>

        <renameDialog :item="renameItem"></renameDialog>

        <deleteConfirmDialog :item="deleteItem"></deleteConfirmDialog>

        <favoriteDialog :item="favoriteItem"></favoriteDialog>

        <tagsDialog :item="tagsItem"></tagsDialog>

    </div>
    `,
    props: [],
    data: function() {
        return {
            items: null,
            path: null,
            currentPath: null,
            selectedItem: null,
            renameItem: null,
            deleteItem: null,
            tagsItem: null,
            favoriteItem: null,
            currentItem: null,
            isLoading: true,
            isFragment: false,
            isProcessing: false,
            Locale: Locale,
        };
    },
    methods: {
        refresh: function() {
            this.path = this.$route.params.path;
            this.scan(this.path);
        },

        scan: function(path) {
            this.isLoading = true;
            var that = this;
            new ComService().scan(path, function(data) {
                that.items = data.items;
                that.isLoading = false;
                that.isFragment = data.fragment;
                that.currentPath = data.path;
                that.isProcessing = data.processing;
                that.currentItem = data;
            });
        },

        onLoadMoreItems: function() {
            this.isLoading = true;
            var that = this;
            new ComService().loadMore(this.currentPath, this.items.length, function(data) {
                that.items = that.items.concat(data.items);
                that.isLoading = false;
                that.isFragment = data.fragment;
                that.currentPath = data.path;
                that.currentItem = data;
            });
        },

        goBack: function() {
            this.$router.go(-1);
        },

        onToggleDirectoryMenu: function() {
            let contextMenu = this.$refs.dirContextMenu; 
            contextMenu.toggleMenu();
        },
    },
    computed: {
        isItemListEmpty: function() {
            return this.items === null || this.items.length === 0;
        }
    },
    watch: {
        '$route': 'refresh',
    },
    created () {
        this.refresh();
    }
});
