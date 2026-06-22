package milthdev.twofactordemo.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TwoFactorNumber(@NotBlank
                              @Pattern(regexp = "\\d{6}", message = "Code must be 6 digits")
                              String code) {

}
