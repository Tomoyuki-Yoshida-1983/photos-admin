package jp.yoshida.photoadmin.controller;

import jp.yoshida.photoadmin.dto.PhotoDto;
import jp.yoshida.photoadmin.form.PhotoForm;
import jp.yoshida.photoadmin.service.impl.PhotosServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PhotosController {

    private final PhotosServiceImpl photosServiceImpl;

    @GetMapping("/")
    @NonNull
    public String index(@NonNull RedirectAttributes redirectAttributes, @ModelAttribute @NonNull PhotoForm photoForm) {

        return "photos/index";
    }

    @PostMapping("/")
    @NonNull
    public String addPhoto(
            @NonNull RedirectAttributes redirectAttributes,
            @ModelAttribute @NonNull PhotoForm photoForm) throws IOException {

        PhotoDto photoDto = new PhotoDto();
        photoDto.setRawPhoto(photoForm.getSendingPhoto());
        photosServiceImpl.addPhoto(photoDto);
        redirectAttributes.addFlashAttribute("msg", "add-success");
        return "redirect:/";
    }

}
