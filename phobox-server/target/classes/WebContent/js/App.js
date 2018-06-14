String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};

var app = new Vue({
	el : '#app',
	router: router,
	data : {
	},

	methods : {
	},

	mounted: function () {
	}
})