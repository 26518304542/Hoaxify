package com.hoaxify.ws.user.dto;

import org.springframework.beans.factory.annotation.Value;

public interface UserProjection {

    long getId();
    String getUserName();
    String getEmail();

    @Value("#{target.image != null ? target.image : default.png}")
    String getImage();

    @Value("#{target.firstname + '' + target.lastname}")
    String getFullName();

}
