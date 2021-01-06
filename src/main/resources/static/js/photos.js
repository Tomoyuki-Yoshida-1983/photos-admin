function jumpToPhoto(id = 0) {

    location.href = "/photo/" + id;
}

function initialize() {

    var message = document.getElementById("message").value;
    if (message) {
        alert(message);
    }
}

function preDeleteCheck() {

    var deleteIds = document.summaryForm.deleteIds;
    var isChecked = false
    deleteIds.forEach(id => isChecked = id.checked || isChecked);

    if (!isChecked) {
        alert("削除する写真のチェックボックスをチェックしてください");
        return false;
    }

    if (confirm("選択された写真を削除してよろしいですか？")) {
        return true;
    }

    return false;
}

function preSendCheck() {

    if (!document.sendingPhotoForm.sendingPhoto.value) {
        alert("アップロードする写真を選択してください");
        return false;
    }

    return true;
}
