package milthdev.twofactordemo.controller;

import lombok.RequiredArgsConstructor;
import milthdev.twofactordemo.model.TwoFactorNumber;
import milthdev.twofactordemo.model.TwoFactorRegResponse;
import milthdev.twofactordemo.service.TwoFactorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/generate-url")
    public ResponseEntity<TwoFactorRegResponse> generateTwoFactorUrl() {
        return ResponseEntity.ok(twoFactorService.generateTwoFactorUrl(EMAIL));
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> generateTwoFactorUrl(@RequestBody TwoFactorNumber twoFactorNumber) throws NoSuchAlgorithmException, InvalidKeyException {
            twoFactorService.validateTwoFactorCode(twoFactorNumber.code(), EMAIL);
        return ResponseEntity.ok(null);
    }

}
