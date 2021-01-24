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
            alert(ERROR_DELETE_IDS_IS_EMPTY);
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

        if(!photos[0].type.match(MIME_TYPE_ANY_IMAGE)) {
            alert(ERROR_FILE_IS_NOT_IMAGE);
            return false;
        }

        if(photos[0].name.length > MAX_FILE_NAME_LENGTH) {
            alert(ERROR_FILE_NAME_LENGTH_EXCEEDED);
            return false;
        }

        if(photos[0].size > MAX_FILE_SIZE) {
            alert(ERROR_FILE_SIZE_EXCEEDED);
            return false;
        }

        return true;
    }
}
