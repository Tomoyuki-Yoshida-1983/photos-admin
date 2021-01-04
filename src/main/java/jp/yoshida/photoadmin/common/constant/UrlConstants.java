package jp.yoshida.photoadmin.common.constant;

public class UrlConstants {

    public static final String REQUEST_GET_PHOTOS = "/photos";

    public static final String REQUEST_GET_PHOTO = "/photo/{id}";

    public static final String REQUEST_ADD_PHOTO = "/add-photo";

    public static final String REQUEST_DELETE_PHOTOS = "/delete-photos";

    public static final String REQUEST_DELETE_PHOTO = "/delete-photo/{id}";

    public static final String RESPONSE_GET_PHOTOS = "photos/index";

    public static final String RESPONSE_GET_PHOTO = "photo/index";

    public static final String REDIRECT_GET_PHOTOS = "redirect:/photos";
}
