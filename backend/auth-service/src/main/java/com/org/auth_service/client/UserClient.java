package com.org.auth_service.client;

import com.org.auth_service.dto.UserPrincipal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/users")
public interface UserClient {

    @GetMapping("/email/{email}")
    UserPrincipal getByEmail(@PathVariable("email") String email);
}
