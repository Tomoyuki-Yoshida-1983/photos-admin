package jp.yoshida.photos_admin.common.exception;

/**
 * 写真例外
 */
public class PhotosException extends Exception {

    private static final long serialVersionUID = 1L;

    public PhotosException(String message) {
        super(message);
    }
}
