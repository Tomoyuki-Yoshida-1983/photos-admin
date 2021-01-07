var PhotosJs = {

    KEY_MESSAGE: "message",
    MAX_FILE_SIZE: 10 * 1024 * 1024,
    CONFIRM_DELETE: "選択された写真を削除してよろしいですか？",
    ERROR_NOT_CHECKED: "削除する写真のチェックボックスをチェックしてください。",
    ERROR_FILE_SIZE_EXCEED: "ファイルサイズは10MB以内にしてください。",

    jumpToPhoto: function(id = 0) {

        location.href = "/photo/" + id;
    },

    initialize: function() {

        var message = document.getElementById(this.KEY_MESSAGE).value;
        if (message) {
            alert(message);
        }
    },

    preDeleteCheck: function() {

        var deleteIds = document.summaryForm.deleteIds;
        var isChecked = false
        deleteIds.forEach(id => isChecked = id.checked || isChecked);

        if (!isChecked) {
            alert(this.ERROR_NOT_CHECKED);
            return false;
        }

        if (confirm(this.CONFIRM_DELETE)) {
            return true;
        }

        return false;
    },

    preSendCheck: function() {

        var photo = document.sendingPhotoForm.sendingPhoto.files;

        if (photo.length == 0) {
            return true;
        }

        if(photo[0].size > this.MAX_FILE_SIZE) {
            alert(this.ERROR_FILE_SIZE_EXCEED);
            return false;
        }

        return true;
    }
}
