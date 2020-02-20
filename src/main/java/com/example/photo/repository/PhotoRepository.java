package com.example.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Iterable<Photo> findByUserIDOrderByIdDesc(String userID);

    Iterable<Photo> findByGalleryNameOrderById(String galleryName);
    Iterable<Photo> findByUserIDAndGalleryNameOrderById(String userID, String galleryName);
}
