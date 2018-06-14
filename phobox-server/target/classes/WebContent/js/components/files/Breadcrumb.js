const Breadcrumb = Vue.component(
	'breadcrumb', {
    template: `
    <div class="pbreadcrumb">
        <div class="bc_element" 
            v-for="e in generateElements" 
            v-on:click="open(e.path)">
            
            {{ e.name }}
        </div>
	</div>
    `,
    props: ['path'],
    data: function() {
    	return {
    	};
    },
    methods: {
    	open : function(path) {
            var com = new ComService();
            var encodedUrl = com.encodePath(this.$route.params.path);
            var encodedPath = com.encodePath(path);
            
            if(encodedUrl === encodedPath) {
                // Reload item list for the current url
                this.$parent.scan(path);

            } else {
                // Navigate to the requested path
                router.push({ path: "/photos/"+encodedPath });
            }
        }
    },
    computed: {
        generateElements : function() {
			path = new ComService()
                .decodePath(this.path)
                .split('/')
                .slice(0);

            path = path.filter(function(n) { return n != ""});

            elements = [];
            elements.push({'path': "%252F", 'name': "/"});

            if(path.length) {
                var tmpPath = "";
                path.forEach(function(f) {
                    tmpPath = tmpPath + "%252F" + f;
                    if(f.length > 15) {
                        f = f.substr(0, 15) + " ...";
                    }
                    elements.push({'path': tmpPath, 'name': f});
                });
            }
            return elements;
        }
    },
});