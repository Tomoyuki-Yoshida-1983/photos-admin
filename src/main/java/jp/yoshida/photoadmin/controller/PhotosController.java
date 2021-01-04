package jp.yoshida.photoadmin.controller;

import com.drew.imaging.ImageProcessingException;
import jp.yoshida.photoadmin.PhotoEntity;
import jp.yoshida.photoadmin.common.constant.MessageConstants;
import jp.yoshida.photoadmin.common.constant.NameConstants;
import jp.yoshida.photoadmin.common.constant.UrlConstants;
import jp.yoshida.photoadmin.form.DeleteForm;
import jp.yoshida.photoadmin.form.PhotoForm;
import jp.yoshida.photoadmin.form.PhotosForm;
import jp.yoshida.photoadmin.service.PhotosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
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
    public String getPhoto(@NonNull PhotoForm photoForm, @PathVariable("id") @NonNull int id) {

        PhotoEntity photoEntity = photosService.getPhoto(id);
        BeanUtils.copyProperties(photoEntity, photoForm);
        return UrlConstants.RESPONSE_GET_PHOTO;
    }

    @PostMapping(UrlConstants.REQUEST_ADD_PHOTO)
    @NonNull
    public String addPhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @NonNull PhotoForm photoForm) throws IOException, ImageProcessingException {

        photosService.addPhoto(photoForm.getSendingPhoto());
        redirectAttributes.addFlashAttribute(NameConstants.KEY_MESSAGE, MessageConstants.INFO_ADD_SUCCESS);
        return UrlConstants.REDIRECT_GET_PHOTOS;
    }

    @PostMapping(UrlConstants.REQUEST_DELETE_PHOTOS)
    @NonNull
    public String deletePhoto(@NonNull RedirectAttributes redirectAttributes, @NonNull DeleteForm deleteForm) {

        photosService.deletePhotos(deleteForm.getDeleteIds());
        redirectAttributes.addFlashAttribute(NameConstants.KEY_MESSAGE, MessageConstants.INFO_DELETE_SUCCESS);
        return UrlConstants.REDIRECT_GET_PHOTOS;
    }
}
