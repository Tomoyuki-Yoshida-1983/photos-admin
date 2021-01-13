var PhotosJs = {

    DOM_ID_MESSAGE: "message",
    DOM_NAME_DELETE_IDS: "deleteIds",
    DOM_NAME_SENDING_PHOTO: "sendingPhoto",
    MAX_FILE_NAME_LENGTH: 50,
    MAX_FILE_SIZE: 1 * 1024 * 1024,
    JS_NOT_EMPTY_DELETE_IDS: document.getElementById("JS_NOT_EMPTY_DELETE_IDS").value,
    JS_MAX_FILE_NAME_LENGTH_SENDING_PHOTO: document.getElementById("JS_MAX_FILE_NAME_LENGTH_SENDING_PHOTO").value,
    JS_MAX_FILE_SIZE_SENDING_PHOTO: document.getElementById("JS_MAX_FILE_SIZE_SENDING_PHOTO").value,
    JS_CONFIRM_DELETE_SELECTED_PHOTOS: document.getElementById("JS_CONFIRM_DELETE_SELECTED_PHOTOS").value,

    initialize: function() {

        var message = document.getElementById(this.DOM_ID_MESSAGE).value;
        if (message) {
            alert(message);
        }
    },

    preDeleteCheck: function() {

        var deleteIds = document.getElementsByName(this.DOM_NAME_DELETE_IDS);
        var isChecked = false
        deleteIds.forEach(id => isChecked = id.checked || isChecked);

        if (!isChecked) {
            alert(this.JS_NOT_EMPTY_DELETE_IDS);
            return false;
        }

        if (confirm(this.JS_CONFIRM_DELETE_SELECTED_PHOTOS)) {
            return true;
        }

        return false;
    },

    preSendCheck: function() {

        var photos = document.getElementById(this.DOM_NAME_SENDING_PHOTO).files;

        if (photos.length == 0) {
            return true;
        }

        if(photos[0].name.length > this.MAX_FILE_NAME_LENGTH) {
            alert(this.JS_MAX_FILE_NAME_LENGTH_SENDING_PHOTO);
            return false;
        }

        if(photos[0].size > this.MAX_FILE_SIZE) {
            alert(this.JS_MAX_FILE_SIZE_SENDING_PHOTO);
            return false;
        }

        return true;
    }
}
