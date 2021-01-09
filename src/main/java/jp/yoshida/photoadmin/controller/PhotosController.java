package jp.yoshida.photoadmin.controller;

import jp.yoshida.photoadmin.common.constant.KeyWordsConstants;
import jp.yoshida.photoadmin.common.constant.MessagesConstants;
import jp.yoshida.photoadmin.common.constant.UrlsConstants;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.exception.PhotosSystemException;
import jp.yoshida.photoadmin.controller.form.DeleteForm;
import jp.yoshida.photoadmin.controller.form.PhotoForm;
import jp.yoshida.photoadmin.controller.form.PhotosForm;
import jp.yoshida.photoadmin.service.PhotosService;
import jp.yoshida.photoadmin.service.dto.PhotoDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PhotosController {

    private final PhotosService photosService;

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
            @PathVariable("id") @NonNull int id) {

        PhotoDto photoDto;
        try {
            photoDto = photosService.getPhoto(id);
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, e.getMessage());
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
            @NonNull BindingResult bindingResult) throws PhotosSystemException {

        try {
            createErrorMessages(bindingResult);
            MultipartFile sendingPhoto = photoForm.getSendingPhoto();
            photosService.addPhoto(sendingPhoto);
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, e.getMessage());
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, MessagesConstants.INFO_ADD_SUCCESS);
        return UrlsConstants.REDIRECT_GET_PHOTOS;
    }

    @PostMapping(UrlsConstants.REQUEST_DELETE_PHOTOS)
    @NonNull
    public String deletePhotos(
            @NonNull RedirectAttributes redirectAttributes,
            @Valid @NonNull DeleteForm deleteForm,
            @NonNull BindingResult bindingResult) {

        try {
            createErrorMessages(bindingResult);
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, e.getMessage());
            return UrlsConstants.REDIRECT_GET_PHOTOS;
        }

        photosService.deletePhotos(deleteForm.getDeleteIds());
        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, MessagesConstants.INFO_DELETE_SUCCESS);
        return UrlsConstants.REDIRECT_GET_PHOTOS;
    }

    @PostMapping(UrlsConstants.REQUEST_DELETE_PHOTO)
    @NonNull
    public String deletePhoto(@NonNull RedirectAttributes redirectAttributes, @PathVariable("id") @NonNull int id) {

        photosService.deletePhotos(new int[] {id});
        redirectAttributes.addFlashAttribute(KeyWordsConstants.KEY_MESSAGE, MessagesConstants.INFO_DELETE_SUCCESS);
        return UrlsConstants.REDIRECT_GET_PHOTOS;
    }

    private void createErrorMessages(@NonNull BindingResult bindingResult) throws PhotosBusinessException {

        if (!bindingResult.hasErrors()) {
            return;
        }

        List<String> messages = new ArrayList<>();

        for(FieldError fieldError: bindingResult.getFieldErrors()) {
            messages.add(fieldError.getDefaultMessage());
        }

        Collections.sort(messages);
        throw new PhotosBusinessException(String.join(KeyWordsConstants.SYMBOL_LINE_FEED, messages));
    }
}
