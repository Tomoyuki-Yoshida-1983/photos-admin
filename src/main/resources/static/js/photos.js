var PhotosJs = {

    initialize: function() {

        if (MESSAGE) {
            alert(MESSAGE);
        }
    },

    preDeleteCheck: function() {

        var deleteIds = document.getElementsByName(DOM_NAME_DELETE_IDS);
        var isChecked = false
        deleteIds.forEach(id => isChecked = id.checked || isChecked);

        if (!isChecked) {
            alert(JS_NOT_EMPTY_DELETE_IDS);
            return false;
        }

        if (confirm(JS_CONFIRM_DELETE_SELECTED_PHOTOS)) {
            return true;
        }

        return false;
    },

    preSendCheck: function() {

        var photos = document.getElementById(DOM_ID_SENDING_PHOTO).files;

        if (photos.length == 0) {
            return true;
        }

        if(photos[0].name.length > MAX_FILE_NAME_LENGTH) {
            alert(JS_MAX_FILE_NAME_LENGTH_SENDING_PHOTO);
            return false;
        }

        if(photos[0].size > MAX_FILE_SIZE) {
            alert(JS_MAX_FILE_SIZE_SENDING_PHOTO);
            return false;
        }

        return true;
    }
}
