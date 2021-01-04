function preDeleteCheck() {

    if (confirm("この写真を削除してよろしいですか？")) {
        return true;
    }

    return false;
}
