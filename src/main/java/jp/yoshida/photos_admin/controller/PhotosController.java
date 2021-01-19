package jp.yoshida.photos_admin.controller;

import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import jp.yoshida.photos_admin.common.constant.MessagesConstants;
import jp.yoshida.photos_admin.common.constant.UrlsConstants;
import jp.yoshida.photos_admin.common.exception.PhotosBusinessException;
import jp.yoshida.photos_admin.common.exception.PhotosSystemException;
import jp.yoshida.photos_admin.controller.form.DeleteForm;
import jp.yoshida.photos_admin.controller.form.PhotoForm;
import jp.yoshida.photos_admin.controller.form.PhotosForm;
import jp.yoshida.photos_admin.service.PhotosService;
import jp.yoshida.photos_admin.service.dto.PhotoDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 写真の登録・検索・削除をするコントローラークラス
 */
@Controller
@RequiredArgsConstructor
public class PhotosController {

    private final PhotosService photosService;

    private final MessageSource messageSource;

    @ModelAttribute
    PhotosForm initPhotosForm() {
        return new PhotosForm();
    }

    @ModelAttribute
    PhotoForm initPhotoForm() {
        return new PhotoForm();
    }

    @ModelAttribute
    DeleteForm initDeleteForm() {
        return new DeleteForm();
    }

    /**
     * <p>写真一覧取得</p>
     * <p>写真テーブルの写真を全件取得し、写真一覧画面に遷移する。</p>
     * @param redirectAttributes リダイレクトの際に情報を引き継ぐモデル
     * @param photosForm 写真一覧フォーム
     * @return 遷移先画面のパス
     */
    @GetMapping(UrlsConstants.REQUEST_GET_PHOTOS)
    @NonNull
    public String getPhotos(@NonNull RedirectAttributes redirectAttributes, @NonNull PhotosForm photosForm) {

        List<PhotoForm> photoForms = new ArrayList<>();

        for (PhotoDto photoDto : photosService.getPhotos()) {
            PhotoForm tempPhotoForm = new PhotoForm();
            BeanUtils.copyProperties(photoDto, tempPhotoForm);
            photoForms.add(tempPhotoForm);
        }

        photosForm.setPhotoForms(photoForms);
        return UrlsConstants.RESPONSE_GET_PHOTOS;
    }

    /**
     * <p>写真詳細取得</p>
     * <p>リクエストパスのIDに一致するIDの写真を写真テーブルから検索し、写真詳細画面に遷移する。</p>
     * <p>写真の検索で業務例外が発生した場合、例外メッセージと情報レベルをリダイレクト属性に保持して写真一覧画面にリダイレクトする。</p>
     * @param redirectAttributes リダイレクトの際に情報を引き継ぐモデル
     * @param photoForm 写真フォーム
     * @param id 検索する写真のID
     * @param httpServletRequest リクエストオブジェクト
     * @return 遷移先画面のパス
     */
    @GetMapping(UrlsConstants.REQUEST_GET_PHOTO)
    @NonNull
    public String getPhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @NonNull PhotoForm photoForm,
            @PathVariable("id") @NonNull int id,
            @NonNull HttpServletRequest httpServletRequest) {

        PhotoDto photoDto;
        try {
            photoDto = photosService.getPhoto(id);
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, messageSource.getMessage(
                    e.getMessage(),null, httpServletRequest.getLocale()));
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, e.getInfoLevel());
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        BeanUtils.copyProperties(photoDto, photoForm);
        return UrlsConstants.RESPONSE_GET_PHOTO;
    }

    /**
     * <p>写真登録</p>
     * <p>画面からアップロードされた写真を写真テーブルに登録し、写真一覧画面にリダイレクトする。</p>
     * <p>画面の入力項目に不正がある場合、例外メッセージと情報レベルをリダイレクト属性に保持して写真一覧画面にリダイレクトする。</p>
     * <p>写真の登録で業務例外が発生した場合、例外メッセージと情報レベルをリダイレクト属性に保持して写真一覧画面にリダイレクトする。</p>
     * <p>システム例外が発生した場合、共通エラー画面に遷移する。</p>
     * @param redirectAttributes リダイレクトの際に情報を引き継ぐモデル
     * @param photoForm 写真フォーム
     * @param bindingResult 入力項目の不正チェック結果
     * @param httpServletRequest リクエストオブジェクト
     * @return 遷移先画面のパス
     */
    @PostMapping(UrlsConstants.REQUEST_ADD_PHOTO)
    @NonNull
    public String addPhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @Valid @NonNull PhotoForm photoForm,
            @NonNull BindingResult bindingResult,
            @NonNull HttpServletRequest httpServletRequest) throws PhotosSystemException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.NAME_BINDING_RESULT_PHOTO_FORM, bindingResult);
            redirectAttributes.addFlashAttribute(KeyWordsConstants.NAME_PHOTO_FORM, photoForm);
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        MultipartFile sendingPhoto = photoForm.getSendingPhoto();

        try {
            photosService.addPhoto(sendingPhoto);
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, messageSource.getMessage(
                    e.getMessage(),null, httpServletRequest.getLocale()));
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, e.getInfoLevel());
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, messageSource.getMessage(
                MessagesConstants.INFO_ADD_SUCCESS,null, httpServletRequest.getLocale()));
        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, MessagesConstants.INFO_LEVEL_INFO);
        return UrlsConstants.REDIRECT_GET_PHOTOS;
    }

    /**
     * <p>写真複数削除</p>
     * <p>画面でチェックボックスにチェックされたIDに一致するIDの写真を写真テーブルから削除し、写真一覧画面にリダイレクトする。</p>
     * <p>画面の入力項目に不正がある場合、例外メッセージと情報レベルをリダイレクト属性に保持して写真一覧画面にリダイレクトする。</p>
     * <p>写真の削除で業務例外が発生した場合、例外メッセージと情報レベルをリダイレクト属性に保持して写真一覧画面にリダイレクトする。</p>
     * @param redirectAttributes リダイレクトの際に情報を引き継ぐモデル
     * @param deleteForm 写真削除フォーム
     * @param bindingResult 入力項目の不正チェック結果
     * @param httpServletRequest リクエストオブジェクト
     * @return 遷移先画面のパス
     */
    @PostMapping(UrlsConstants.REQUEST_DELETE_PHOTOS)
    @NonNull
    public String deletePhotos(
            @NonNull RedirectAttributes redirectAttributes,
            @Valid @NonNull DeleteForm deleteForm,
            @NonNull BindingResult bindingResult,
            @NonNull HttpServletRequest httpServletRequest) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.NAME_BINDING_RESULT_DELETE_FORM, bindingResult);
            redirectAttributes.addFlashAttribute(KeyWordsConstants.NAME_DELETE_FORM, deleteForm);
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        try {
            photosService.deletePhotos(deleteForm.getDeleteIds());
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, messageSource.getMessage(
                    e.getMessage(),null, httpServletRequest.getLocale()));
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, e.getInfoLevel());
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, messageSource.getMessage(
                MessagesConstants.INFO_DELETE_SUCCESS,null, httpServletRequest.getLocale()));
        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, MessagesConstants.INFO_LEVEL_INFO);
        return UrlsConstants.REDIRECT_GET_PHOTOS;
    }

    /**
     * <p>写真削除</p>
     * <p>リクエストパスのIDに一致するIDの写真を写真テーブルから削除し、写真一覧画面にリダイレクトする。</p>
     * <p>写真の削除で業務例外が発生した場合、例外メッセージと情報レベルをリダイレクト属性に保持して写真一覧画面にリダイレクトする。</p>
     * @param redirectAttributes リダイレクトの際に情報を引き継ぐモデル
     * @param id 削除する写真のID
     * @param httpServletRequest リクエストオブジェクト
     * @return 遷移先画面のパス
     */
    @PostMapping(UrlsConstants.REQUEST_DELETE_PHOTO)
    @NonNull
    public String deletePhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @PathVariable("id") @NonNull int id,
            @NonNull HttpServletRequest httpServletRequest) {

        try {
            photosService.deletePhotos(new int[]{id});
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, messageSource.getMessage(
                    e.getMessage(),null, httpServletRequest.getLocale()));
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, e.getInfoLevel());
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, messageSource.getMessage(
                MessagesConstants.INFO_DELETE_SUCCESS,null, httpServletRequest.getLocale()));
        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_INFO_LEVEL, MessagesConstants.INFO_LEVEL_INFO);
        return UrlsConstants.REDIRECT_GET_PHOTOS;
    }
}
