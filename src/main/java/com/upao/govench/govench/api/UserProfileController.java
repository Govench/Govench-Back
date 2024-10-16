package com.upao.govench.govench.api;

import com.upao.govench.govench.model.dto.UserProfileDTO;
import com.upao.govench.govench.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
public class UserProfileController {
    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    private ResponseEntity <UserProfileDTO> updateProfile(@PathVariable Integer id,@Valid @RequestBody UserProfileDTO userProfileDTO)
    {
        UserProfileDTO updatedProfile = userService.uptadteUserProfile(id, userProfileDTO);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserProfileDTO> getById(@PathVariable Integer id)
    {
        UserProfileDTO userProfileDTO = userService.getUserProfilebyId(id);
        return new ResponseEntity<>(userProfileDTO, HttpStatus.OK);
    }

}
