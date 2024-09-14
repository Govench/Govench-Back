package com.upao.govench.govench.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.upao.govench.govench.model.entity.UserCommunity;
import com.upao.govench.govench.model.entity.IdCompuestoU_C;
import com.upao.govench.govench.service.UserCommunityService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;



@AllArgsConstructor
@RestController

@RequestMapping("/admin/usercommunity")
public class UserCommunityController {
    @Autowired

    private UserCommunityService userCommunityService;
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserCommunity> getAllUserCommunities() {
        return userCommunityService.getAllUserCommunities();
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{iduser}/{idcommunity}")
    public UserCommunity getUserCommunityById(@PathVariable int iduser,@PathVariable int idcommunity) {
        IdCompuestoU_C id =new IdCompuestoU_C(iduser, idcommunity);
        return userCommunityService.searchUserCommunityById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public UserCommunity createUserCommunity(@RequestBody UserCommunity userCommunity) {
        UserCommunity detail = userCommunityService.searchUserCommunityById(userCommunity.getId());
        return userCommunityService.addUserCommunity(userCommunity);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{iduser}/{idcommunity}")
    public void deleteUserCommunity(@PathVariable int iduser,@PathVariable int idcommunity) {
        IdCompuestoU_C id =new IdCompuestoU_C(iduser, idcommunity);
        userCommunityService.removeUserCommunityById(id);
    }
}

