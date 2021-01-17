package jp.yoshida.photosadmin.controller;

import jp.yoshida.photosadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photosadmin.common.constant.MessagesConstants;
import jp.yoshida.photosadmin.common.constant.UrlsConstants;
import jp.yoshida.photosadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photosadmin.common.exception.PhotosSystemException;
import jp.yoshida.photosadmin.controller.form.DeleteForm;
import jp.yoshida.photosadmin.controller.form.PhotoForm;
import jp.yoshida.photosadmin.controller.form.PhotosForm;
import jp.yoshida.photosadmin.service.PhotosService;
import jp.yoshida.photosadmin.service.dto.PhotoDto;
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
