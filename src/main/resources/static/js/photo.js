var PhotoJs = {

    CONFIRM_DELETE: "この写真を削除してよろしいですか？",

    preDeleteCheck: function() {

        if (confirm(this.CONFIRM_DELETE)) {
            return true;
        }

        return false;
    }
}
