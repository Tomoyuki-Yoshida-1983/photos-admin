package jp.yoshida.photos_admin.controller.form;

import lombok.Data;

import java.util.List;

/**
 * 写真一覧フォーム
 */
@Data
public class PhotosForm {

    /**
     * 写真フォームの一覧
     */
    private List<PhotoForm> photoForms;
}
