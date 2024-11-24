package com.hoaxify.ws.user.dto;

import com.hoaxify.ws.user.User;

public class UserDTO {

    long id;

    String username;

    String image;

    String email;

    public UserDTO(User user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setImage(user.getImage());
        setEmail(user.getEmail());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
