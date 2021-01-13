var PhotoJs = {

    JS_CONFIRM_DELETE_THIS_PHOTO: document.getElementById("JS_CONFIRM_DELETE_THIS_PHOTO").value,

    preDeleteCheck: function() {

        if (confirm(this.JS_CONFIRM_DELETE_THIS_PHOTO)) {
            return true;
        }

        return false;
    }
}
