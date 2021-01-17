package jp.yoshida.photosadmin.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteForm {

    @NotEmpty
    private int[] deleteIds;
}
