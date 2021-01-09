package jp.yoshida.photoadmin.controller.form;

import jp.yoshida.photoadmin.common.constant.MessagesConstants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteForm {

    @NotEmpty(message = MessagesConstants.ERROR_NOT_CHECKED)
    private int[] deleteIds;
}
