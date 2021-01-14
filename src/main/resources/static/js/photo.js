var PhotoJs = {

    preDeleteCheck: function() {

        if (confirm(JS_CONFIRM_DELETE_THIS_PHOTO)) {
            return true;
        }

        return false;
    }
}
