package jp.yoshida.photoadmin.controller;

import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.form.PhotoForm;
import jp.yoshida.photoadmin.form.PhotoInDetailForm;
import jp.yoshida.photoadmin.service.PhotosService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PhotosController {

    private final PhotosService photosService;

    @GetMapping("/")
    @NonNull
    public String index(
            @NonNull RedirectAttributes redirectAttributes,
            @ModelAttribute @NonNull PhotoForm photoForm) {

        List<PhotoInDetailForm> photoInDetailForms = new ArrayList<>();

        for (PhotoDto photoDto : photosService.getPhotos()) {
            PhotoInDetailForm photoInDetailForm = new PhotoInDetailForm();
            BeanUtils.copyProperties(photoDto, photoInDetailForm);
            photoInDetailForm.setThumbnail(Base64.getEncoder().encodeToString(photoDto.getThumbnail()));
            photoInDetailForms.add(photoInDetailForm);
        }

        photoForm.setPhotoInDetailForms(photoInDetailForms);
        return "photos/index";
    }

    @PostMapping("/")
    @NonNull
    public String addPhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @ModelAttribute @NonNull PhotoForm photoForm) throws IOException {

        PhotoDto photoDto = new PhotoDto();
        BeanUtils.copyProperties(photoForm, photoDto);
        photosService.addPhoto(photoDto);
        redirectAttributes.addFlashAttribute("msg", "add-success");
        return "redirect:/";
    }

}
