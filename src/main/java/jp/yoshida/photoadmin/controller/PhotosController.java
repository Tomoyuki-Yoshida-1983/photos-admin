package jp.yoshida.photoadmin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PhotosController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("msg", "init-success");
        return "photos/index";
    }

    @PostMapping("/")
    public String addPhoto(Model model) {
        model.addAttribute("msg", "add-success");
        return "photos/index";
    }

}
