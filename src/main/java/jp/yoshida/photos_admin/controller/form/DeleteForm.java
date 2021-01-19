package jp.yoshida.photos_admin.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 写真削除フォーム
 */
@Data
public class DeleteForm {

    /**
     * 削除する写真のIDの配列
     */
    @NotEmpty
    private int[] deleteIds;
}
