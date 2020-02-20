package com.example.photo.controllers;

import com.example.photo.repository.Photo;
import com.example.photo.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping(path = "/photos")
public class PhotosController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("")
    public String showHomePage(ModelMap model) {
        model.put("userNameLong", photoService.getLoginID() + (photoService.isAdmin() ? " (Administrator)" : " (Regular user)"));
        model.put("userNameShort", photoService.getLoginID());

        model.put("photo_list", photoService.getPhotoList());
        return "photo_list"; // photo_list.html
    }

    @GetMapping("/add")
    public String showAddPhotoPage(ModelMap model) {
        Photo photo = new Photo(photoService.getLoginID(), "default gallery");
        model.put("photo", photo);
        return "add_photo"; // add_photo.html
    }

    @PostMapping("/add")
    public String addNewPhoto(ModelMap model, @Valid Photo photo, BindingResult result, @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (result.hasErrors() || fileName.contains("..") || !photoService.fileIsImage(file)) {
            return "add_photo"; // add_photo.html
        }

        Photo newPhoto = null;
        if (photoService.isAdmin()) {
            newPhoto = new Photo(photo.getUserID(), photo.getGalleryName());
        } else {
            newPhoto = new Photo(photoService.getLoginID(), photo.getGalleryName());
        }
        try {
            newPhoto.setBytesData(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoService.savePhoto(newPhoto);
        return "redirect:/";
    }

    @PostMapping("/filterByGalleryName")
    public String filter(@RequestParam String filterByGalleryName, ModelMap model) {
        if (filterByGalleryName != null && !filterByGalleryName.isEmpty()) {
            model.put("photo_list", photoService.findByGalleryName(filterByGalleryName));
        } else {
            model.put("photo_list", photoService.getPhotoList());
        }
        return "photo_list"; // photo_list.html
    }

    @GetMapping("/{id}")
    public String findPhotoById(@PathVariable String id, ModelMap model) {
        model.put("photo_list", photoService.getPhotoById(Long.parseLong(id)));
        return "photo_list";
    }

    @GetMapping("/delete")
    public String deletePhotoById(@RequestParam long id) {
        photoService.deletePhotoById(id);
        return "redirect:/photos"; // photo_list.html
    }
}
