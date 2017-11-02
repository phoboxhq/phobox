const FileBrowser = Vue.component(
    'filebrowser', {
    template: `
    <div id="filebrowser">
        <breadcrumb :path="path"></breadcrumb>

        <transition name="fade">
            <!-- List all elements as FileItem -->
            <div class="items">
                <fileitem v-for="item in items"
                    :item="item"
                    :selectedItem="selectedItem"></fileitem>
            </div>
        </transition>

        <!-- Loading image -->
        <transition name="fade">
            <img src="img/loading.gif" v-if="isLoading" class="loading" />
        </transition>

        <!-- Include the lightbox -->
        <lightbox
            :items="items"
            :selectedItem="selectedItem"></lightbox>

        <div class="not_found_center"
            v-if="isItemListEmpty">
            <i style="font-size: 20em; color: rgba(0, 0, 0, 0.27);"
                class="fa fa-frown-o" aria-hidden="true"></i>
            <h4>{{ Locale.values.pictures.not_found }}</h4>
            <a href=""
                v-on:click="goBack()">
                {{ Locale.values.pictures.go_back }}</a>
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
            selectedItem: null,
            renameItem: null,
            deleteItem: null,
            tagsItem: null,
            favoriteItem: null,
            isLoading: true,
            Locale: Locale,
        };
    },
    methods: {
        refresh () {
            this.path = this.$route.params.path;
            this.scan(this.path);
        },

        scan : function(path) {
            this.isLoading = true;
            var that = this;
            new ComService().scan(path, function(data) {
                that.items = data.items;
                that.isLoading = false;
            });
        },

        goBack: function() {
            this.$router.go(-1);
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
