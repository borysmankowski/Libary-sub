package com.example.borys_mankowski_test_10.appuser;


import com.example.borys_mankowski_test_10.appuser.model.AppUserDto;
import com.example.borys_mankowski_test_10.appuser.model.CreateAppUserCommand;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/user")
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AppUserDto registerUser(@RequestBody CreateAppUserCommand createAppUserCommand) {
        return appUserService.registerAppUser(createAppUserCommand);
    }

    @GetMapping("confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        appUserService.confirmToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<AppUserDto>> getAllCustomers(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AppUserDto> customers = appUserService.getAllUsers(pageRequest);
        return ResponseEntity.ok(customers);
    }
}
