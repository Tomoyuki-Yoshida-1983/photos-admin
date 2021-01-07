package jp.yoshida.photoadmin.controller;

import jp.yoshida.photoadmin.PhotoEntity;
import jp.yoshida.photoadmin.common.constant.MessageConstants;
import jp.yoshida.photoadmin.common.constant.NameConstants;
import jp.yoshida.photoadmin.common.constant.UrlConstants;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.exception.PhotosSystemException;
import jp.yoshida.photoadmin.form.DeleteForm;
import jp.yoshida.photoadmin.form.PhotoForm;
import jp.yoshida.photoadmin.form.PhotosForm;
import jp.yoshida.photoadmin.service.PhotosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @GetMapping(UrlConstants.REQUEST_GET_PHOTOS)
    @NonNull
    public String getPhotos(@NonNull RedirectAttributes redirectAttributes, @NonNull PhotosForm photosForm) {

        List<PhotoForm> photoForms = new ArrayList<>();

        for (PhotoEntity photoEntity : photosService.getPhotos()) {
            PhotoForm tempPhotoForm = new PhotoForm();
            BeanUtils.copyProperties(photoEntity, tempPhotoForm);
            photoForms.add(tempPhotoForm);
        }

        photosForm.setPhotoForms(photoForms);
        return UrlConstants.RESPONSE_GET_PHOTOS;
    }

    @GetMapping(UrlConstants.REQUEST_GET_PHOTO)
    @NonNull
    public String getPhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @NonNull PhotoForm photoForm,
            @PathVariable("id") @NonNull int id) {

        PhotoEntity photoEntity;
        try {
            photoEntity = photosService.getPhoto(id);
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(NameConstants.KEY_MESSAGE, e.getMessage());
            return UrlConstants.REDIRECT_GET_PHOTOS;
        }

        BeanUtils.copyProperties(photoEntity, photoForm);
        return UrlConstants.RESPONSE_GET_PHOTO;
    }

    @PostMapping(UrlConstants.REQUEST_ADD_PHOTO)
    @NonNull
    public String addPhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @NonNull PhotoForm photoForm) throws PhotosSystemException {

        MultipartFile sendingPhoto = photoForm.getSendingPhoto();

        try {
            if (Objects.isNull(sendingPhoto)) {
                throw new PhotosBusinessException(MessageConstants.ERROR_NO_FILE);
            }

            String mimeType = sendingPhoto.getContentType();

            if (Objects.isNull(mimeType) || !mimeType.matches(NameConstants.MIME_TYPE_ANY_IMAGE)) {
                throw new PhotosBusinessException(MessageConstants.ERROR_NOT_IMAGE);
            }

            photosService.addPhoto(sendingPhoto);
            redirectAttributes.addFlashAttribute(NameConstants.KEY_MESSAGE, MessageConstants.INFO_ADD_SUCCESS);
        } catch (PhotosBusinessException e) {
            redirectAttributes.addFlashAttribute(NameConstants.KEY_MESSAGE, e.getMessage());
        }

        return UrlConstants.REDIRECT_GET_PHOTOS;
    }

    @PostMapping(UrlConstants.REQUEST_DELETE_PHOTOS)
    @NonNull
    public String deletePhotos(@NonNull RedirectAttributes redirectAttributes, @NonNull DeleteForm deleteForm) {

        photosService.deletePhotos(deleteForm.getDeleteIds());
        redirectAttributes.addFlashAttribute(NameConstants.KEY_MESSAGE, MessageConstants.INFO_DELETE_SUCCESS);
        return UrlConstants.REDIRECT_GET_PHOTOS;
    }

    @PostMapping(UrlConstants.REQUEST_DELETE_PHOTO)
    @NonNull
    public String deletePhoto(@NonNull RedirectAttributes redirectAttributes, @PathVariable("id") @NonNull int id) {

        photosService.deletePhotos(new int[] {id});
        redirectAttributes.addFlashAttribute(NameConstants.KEY_MESSAGE, MessageConstants.INFO_DELETE_SUCCESS);
        return UrlConstants.REDIRECT_GET_PHOTOS;
    }
}
