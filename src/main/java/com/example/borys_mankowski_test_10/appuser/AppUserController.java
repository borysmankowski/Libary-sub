package com.example.borys_mankowski_test_10.appuser;


import com.example.borys_mankowski_test_10.appuser.model.AppUserDto;
import com.example.borys_mankowski_test_10.appuser.model.CreateAppUserCommand;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/user")
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping
    public ResponseEntity<AppUserDto> registerUser(@RequestBody @Valid CreateAppUserCommand createAppUserCommand) {
        AppUserDto createdUser = appUserService.registerAppUser(createAppUserCommand);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        appUserService.confirmToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<AppUserDto>> getAllCustomers(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<AppUserDto> customers = appUserService.getAllUsers(pageable);
        return ResponseEntity.ok(customers);
    }
}
