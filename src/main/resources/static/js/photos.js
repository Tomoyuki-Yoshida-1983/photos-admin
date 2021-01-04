function jumpToPhoto(id = 0) {

    location.href = "/photo/" + id;
}

function infoMessage(msg = "") {

    if (msg) {
        alert(msg);
    }
}

function preDeleteCheck() {

    if (confirm("選択された写真を削除してよろしいですか？")) {
        return true;
    }

    return false;
}
