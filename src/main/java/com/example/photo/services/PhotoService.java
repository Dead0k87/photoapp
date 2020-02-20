package com.example.photo.services;


import com.example.photo.repository.Photo;
import com.example.photo.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Transactional
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Iterable<Photo> getPhotoList() {
        Iterable<Photo> photo_list = new ArrayList<>();
        if (isAdmin()) {
            photo_list = photoRepository.findAll().stream().sorted(Comparator.comparing(Photo::getId).reversed()).collect(Collectors.toList());
        } else {
            photo_list = photoRepository.findByUserIDOrderByIdDesc(getLoginID());
        }
        return photo_list;
    }

    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public void deletePhotoById(long id) {
        photoRepository.deleteById(id);
    }

    public Photo getPhotoById(long id) {
        return photoRepository.findById(id).get();
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        return hasAdminRole;
    }

    public String getLoginID() {
        Object authentication =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authentication instanceof UserDetails) {
            return ((UserDetails) authentication).getUsername();
        }
        return authentication.toString();
    }

    public boolean fileIsImage(MultipartFile file) {
        try {
            if (ImageIO.read(new ByteArrayInputStream(file.getBytes())) == null) {
                return false; // not an image
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Iterable<Photo> findByGalleryName(String galleryName) {
        if(isAdmin()){
            return photoRepository.findByGalleryNameOrderById(galleryName);
        } else{
            return photoRepository.findByUserIDAndGalleryNameOrderById(getLoginID(),galleryName);
        }


    }
}
