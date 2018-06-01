"use strict";

/**
 * This class handles the communication methods to the Backend.
 */
function ComService() {

	this.getData = function(response) {
		return repsonse;
	}

	/** Encodes a given path to an URL frindly path (/ -> %252F) */
	this.encodePath = function (path) {
		if(path === undefined) 		path = "/";
		return path.replace(new RegExp("%2F", 'g'), "%252F").replace(/\//g, "%252F");
	};

	/** Decodes a given encodes path (%2F -> /) */
	this.decodePath = function (path) {
		if(path === undefined) 		path = "";
		return path.replace(new RegExp("%2F", 'g'), "/").replace(new RegExp("%252F", 'g'), "/");
	};

	/** Requests the backend for a given directory */
	this.scan = function (path, callback) {
		var p = this.encodePath(path);
		return $.get("/api/photos/scan/"+p).done(callback);
	};

	/** Requests the backend for a given directory with last item as marker for pagination */
	this.loadMore = function (path, lastItemIndex, callback) {
		var p = this.encodePath(path);
		return $.get("/api/photos/scan/"+p+"/"+lastItemIndex).done(callback);
	};

	/** Requests the backend for the current status of the system */
	this.status = function (callback) {
		return $.get("/api/storage/status").done(callback);
	};

	/** Rename item (image/directory) */
	this.rename = function(itemPath, newName, callback) {
		var p = this.encodePath(itemPath);
		return $.get("/api/photo/rename/"+p+"/"+newName).done(callback);
	};

	/** Delete item (image/directory) */
	this.delete = function(itemPath, callback) {
		var p = this.encodePath(itemPath);
		return $.get("/api/photo/delete/"+p).done(callback);
	};

	/** Save the login credentials for a secure storage access */
	this.saveCredentials = function(user, pass, callback) {
		return $.ajax({
			url: "/api/settings/credentials/",
			type: "post",
			contentType: "application/json",
			data: JSON.stringify({username: user, password: pass}),
			dataType : "json",
			success: callback,
		});
	};

	/** Deletes the current access credentials */
	this.resetCredentials = function(callback) {
		return $.ajax({
			url: '/api/settings/credentials/',
			type: 'DELETE',
			success: callback
		});
	};

	/** Get the currently configured access credentials */
	this.fetchCredentials = function(callback) {
		return $.get("/api/settings/credentials/").done(callback);
	};

	/** Get the currently configured import pattern for directories */
	this.fetchImportPattern = function(callback) {
		return $.get("/api/settings/importPattern/").done(callback);
	};

	/** Save an other import pattern for directories */
	this.saveImportPattern = function(pattern, callback) {
		return $.ajax({
			url: "/api/settings/importPattern/",
			type: "post",
			contentType: "application/json",
			data: pattern,
			success: callback,
		});
	};

	/** Get the avialable album names */
	this.getAlbums = function(callback) {
		return $.get("/api/album/").done(callback);
	};

	/** Get the content for the given album */
	this.getAlbum = function(albumNane, callback) {
		return $.get("/api/album/"+albumNane).done(callback);
	};

	/** Add element to album */
	this.addToAlbum = function(itemPath, album, callback) {
		return $.ajax({
			url: "/api/album/",
			type: "put",
			contentType: "application/json",
			data: JSON.stringify({'name':album, 'item':itemPath}),
			dataType : "json",
			success: callback,
		});
	};

	/** Check current state of all thumbnails and possible recreate is again */
	this.createThumbnails = function(callback) {
		return $.get("/api/storage/rethumb/").done(callback);
	};

	this.changePath = function(newpath, callback) {
		return $.ajax({
			url: "/api/settings/path/",
			type: "post",
			contentType: "application/json",
			data: newpath,
			success: callback,
		});
	};

	this.getExif = function(itemPath) {
		var p = this.encodePath(itemPath);
		return $.get("/api/photo/exif/"+p).done(function (response) {
			return response.data;
		});
	};

	this.getItem = function(itemPath) {
		var p = this.encodePath(itemPath);
		return $.get("/api/photo/"+p).done(function (response) {
			return response.data;
		});
	};

	this.getTags = function(itemPath, callback) {
		var p = this.encodePath(itemPath);
		return $.get("/api/tags/item/"+p).done(callback);
	};

	this.getApprovalPictures = function(callback) {
		return $.get("/api/approval/scan/").done(callback);
	};

	this.setTags = function(itemPath, tags, callback) {
		var p = this.encodePath(itemPath);
		return $.ajax({
			url: "/api/tags/",
			type: "put",
			contentType: "application/json",
			data: JSON.stringify({'tags':tags, 'item':itemPath}),
			dataType : "json",
			success: callback,
		});
	};

	this.search = function(searchString, callback) {
		return $.get("/api/search/"+searchString).done(callback);
	};
}
