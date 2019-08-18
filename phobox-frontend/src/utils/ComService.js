import $ from 'jquery';

export default function () {

  this.getData = function (response) {
    return repsonse;
  }

  /** Encodes a given path to an URL frindly path (/ -> %252F) */
  this.encodePath = function (path) {
    if (path === undefined) path = "/";
    return path.replace(new RegExp("%2F", 'g'), "%252F").replace(/\//g, "%252F");
  };

  /** Decodes a given encodes path (%2F -> /) */
  this.decodePath = function (path) {
    if (path === undefined) path = "";
    return path.replace(new RegExp("%2F", 'g'), "/").replace(new RegExp("%252F", 'g'), "/");
  };

  /** Requests the backend for a given directory */
  this.scan = function (path, callback) {
    var p = this.encodePath(path);
    return $.get(process.env.SERVER_PATH+"api/photos/scan/" + p + 
      "/?page=0&size=30&sort=creation&creation.dir=asc")
      .done(callback);
  };

  /** Requests the backend for a given directory */
  this.getDirectories = function (path, callback) {
    var p = this.encodePath(path);
    return $.get(process.env.SERVER_PATH+"api/photos/directories/" + p)
      .done(callback);
  };

  /** Requests the backend for a given directory with last item as marker for pagination */
  this.loadMore = function (path, lastItemIndex, amountOfItems, callback) {
    var p = this.encodePath(path);
    return $.get(
      process.env.SERVER_PATH+"api/photos/scan/" + p + 
        "/?page=" + lastItemIndex+"&size="+amountOfItems+
        "&sort=creation&creation.dir=asc")
        .done(callback);
  };

  /** Requests the backend for the current status of the system */
  this.status = function (callback) {
    return $.get(process.env.SERVER_PATH+"api/storage/status").done(callback);
  };

  /** Rename item (image/directory) */
  this.rename = function (itemPath, newName, callback) {
    var p = this.encodePath(itemPath);
    return $.get(process.env.SERVER_PATH+"api/photo/rename/" + p + "/" + newName).done(callback);
  };

  /** Delete item (image/directory) */
  this.delete = function (itemPath, callback) {
    var p = this.encodePath(itemPath);
    return $.get(process.env.SERVER_PATH+"api/photo/delete/" + p).done(callback);
  };

  /** Save the login credentials for a secure storage access */
  this.saveCredentials = function (user, pass, callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/settings/credentials/",
      type: "post",
      contentType: "application/json",
      data: JSON.stringify({ username: user, password: pass }),
      dataType: "json",
      success: callback,
    });
  };

  /** Deletes the current access credentials */
  this.resetCredentials = function (callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+'/api/settings/credentials/',
      type: 'DELETE',
      success: callback
    });
  };

  /** Get the currently configured access credentials */
  this.fetchCredentials = function (callback) {
    return $.get(process.env.SERVER_PATH+"api/settings/credentials/").done(callback);
  };

  /** Get the currently configured import pattern for directories */
  this.fetchImportPattern = function (callback) {
    return $.get(process.env.SERVER_PATH+"api/settings/importPattern/").done(callback);
  };

  /** Save an other import pattern for directories */
  this.saveImportPattern = function (pattern, callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/settings/importPattern/",
      type: "post",
      contentType: "application/json",
      data: pattern,
      success: callback,
    });
  };

  /** Get the avialable album names */
  this.getAlbums = function (callback) {
    return $.get(process.env.SERVER_PATH+"api/album/").done(callback);
  };

  /** Get the content for the given album */
  this.getAlbum = function (albumNane, callback) {
    return $.get(process.env.SERVER_PATH+"api/album/" + albumNane).done(callback);
  };
  
  this.deleteAlbum = function(albumName, callback, errorCallback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/album/" + albumName,
      type: "delete",
    })
    .done((data) => callback(data))
    .fail((data) => errorCallback(data));
  }
  
  this.renameAlbum = function(albumName, newAlbumName, callback, errorCallback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/album/" + albumName + "/" + newAlbumName,
      type: "post",
    })
    .done((data) => callback(data))
    .fail((data) => errorCallback(data));
  }

  /** Add element to album */
  this.addToAlbum = function (itemPath, album, callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/album",
      type: "put",
      contentType: "application/json",
      data: JSON.stringify({ 'albumName': album, 'itemPath': itemPath }),
      dataType: "json",
      success: callback,
    });
  };

  /** Remove element from album */
  this.removeFromAlbum = function (itemPath, album, callback, errorCallback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/album/"+album+"/"+this.encodePath(itemPath),
      type: "delete",
    })
    .done((data) => callback(data))
    .fail((data) => errorCallback(data));
  };

  /** Check current state of all thumbnails and possible recreate is again */
  this.createThumbnails = function (callback) {
    return $.get(process.env.SERVER_PATH+"api/storage/rethumb/").done(callback);
  };

  this.getPath = function (callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/settings/path",
      type: "get",
      success: callback,
    });
  };

  this.changePath = function (newpath, callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/settings/path/",
      type: "post",
      contentType: "application/json",
      data: newpath,
      success: callback,
    });
  };

  this.getExif = function (itemPath, callback) {
    var p = this.encodePath(itemPath);
    return $.get(process.env.SERVER_PATH+"api/photo/exif/" + p).done(callback);
  };

  this.getItem = function (itemPath) {
    var p = this.encodePath(itemPath);
    return $.get(process.env.SERVER_PATH+"api/photo/" + p).done(function (response) {
      return response.data;
    });
  };

  this.getTags = function (itemPath, callback) {
    var p = this.encodePath(itemPath);
    return $.get(process.env.SERVER_PATH+"api/tags/item/" + p).done(callback);
  };

  this.getApprovalPictures = function (callback) {
    return $.get(process.env.SERVER_PATH+"api/approval/scan/").done(callback);
  };

  this.acceptApprovalPicture = function (picture, callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/approval/accept/",
      type: "post",
      contentType: "application/json",
      data: picture,
      success: callback,
    });
  };

  this.declineApprovalPicture = function (picture, callback) {
    return $.ajax({
      url: process.env.SERVER_PATH+"api/approval/decline/",
      type: "post",
      contentType: "application/json",
      data: picture,
      success: callback,
    });
  };

  this.setTags = function (itemPath, tags, callback) {
    var p = this.encodePath(itemPath);
    return $.ajax({
      url: process.env.SERVER_PATH+"api/tags/",
      type: "put",
      contentType: "application/json",
      data: JSON.stringify({ 'tags': tags, 'item': itemPath }),
      dataType: "json",
      success: callback,
    });
  };

  this.search = function (searchString, lastItemIndex, amountOfItems, callback) {

    // "&sort=creation&creation.dir=asc")

    return $.get(
      process.env.SERVER_PATH+"api/search/" + searchString + 
      "/?page=" + lastItemIndex+"&size="+amountOfItems).done(callback);
  };

  this.getCountedItemsByYear = function (year, callback) {
    return $.get(process.env.SERVER_PATH+"api/statistics/year/" + year).done(callback);
  };
}