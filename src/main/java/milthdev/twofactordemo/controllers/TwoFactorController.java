package milthdev.twofactordemo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import milthdev.twofactordemo.models.TwoFactorNumber;
import milthdev.twofactordemo.models.TwoFactorRegResponse;
import milthdev.twofactordemo.services.AccountService;
import milthdev.twofactordemo.services.TwoFactorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/api/v1/two-factor")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
class TwoFactorController {

    private static final String EMAIL = "test@test.com";

    private final TwoFactorService twoFactorService;
    private final AccountService accountService;

    @PostMapping("/generate-url")
    public ResponseEntity<TwoFactorRegResponse> generateTwoFactorUrl() {
        return ResponseEntity.ok(twoFactorService.generateTwoFactorUrl(EMAIL));
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> generateTwoFactorUrl(@RequestBody @Validated TwoFactorNumber twoFactorNumber) throws NoSuchAlgorithmException, InvalidKeyException {
        twoFactorService.validateTwoFactorCode(twoFactorNumber.code(), EMAIL);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetTwoFactorSecret(@RequestBody @Validated TwoFactorNumber twoFactorNumber) throws NoSuchAlgorithmException, InvalidKeyException {
        accountService.resetTwoFactorSecret( EMAIL);
        return ResponseEntity.noContent().build();
    }

}
