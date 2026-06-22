package milthdev.twofactordemo.controllers;

import lombok.RequiredArgsConstructor;
import milthdev.twofactordemo.exceptions.BadRequestException;
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
@SuppressWarnings("java:S5122")
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
    public ResponseEntity<Void> validateTwoFactorCode(@RequestBody @Validated TwoFactorNumber twoFactorNumber) throws NoSuchAlgorithmException, InvalidKeyException {
        twoFactorService.validateTwoFactorCode(parseNumbers(twoFactorNumber.code()), EMAIL);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetTwoFactorSecret() {
        accountService.resetTwoFactorSecret(EMAIL);
        return ResponseEntity.noContent().build();
    }

    private int parseNumbers(String code) {
        try {
            return Integer.parseInt(code);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Code must be 6 digits");
        }
    }

}
