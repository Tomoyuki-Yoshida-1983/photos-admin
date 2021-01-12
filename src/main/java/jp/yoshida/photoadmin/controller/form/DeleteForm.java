package jp.yoshida.photoadmin.controller.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteForm {

    @NotEmpty
    private int[] deleteIds;
}
