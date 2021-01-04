function jumpToPhoto(id = 0) {

    location.href = "/photo/" + id;
}

function infoMessage(msg = "") {

    if (msg) {
        alert(msg);
    }
}
