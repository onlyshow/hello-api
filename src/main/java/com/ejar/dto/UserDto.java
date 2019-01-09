package com.ejar.dto;

import com.ejar.model.User;

import javax.validation.constraints.Size;

public class UserDto {

    @Size(min = 5, max = 20)
    private String username;

    private boolean enabled;

    public UserDto() {

    }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.enabled = user.getEnabled();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
