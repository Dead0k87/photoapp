package com.example.photo.repository;

import lombok.Data;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
public class Photo {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @NotEmpty(message = "user name cannot be null")
    private String userID;

    @NotEmpty(message = "Gallery cannot be empty")
    private String galleryName;

    @Lob
    private byte[] bytesData;

    public Photo() {
    }

    public Photo(String userID, String galleryName) {
        this.userID = userID;
        this.galleryName = galleryName;
    }

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.getBytesData());
    }
}
