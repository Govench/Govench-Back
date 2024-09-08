package com.upao.govench.govench.api;

import com.upao.govench.govench.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/utility") //Esta clase me sirve para encryptar un id
public class UtilityController {

    @Autowired
    private EncryptionService encryptionService;

    @GetMapping("/encrypt/{userId}") //Este metodo recibe un id y me devuelve encryptado
    public String getEncodedUserId(@PathVariable int userId) throws Exception {
        return encryptionService.encrypt(Integer.toString(userId));
    }
}