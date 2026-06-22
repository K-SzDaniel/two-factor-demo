package milthdev.twofactordemo.models;

import jakarta.validation.constraints.Positive;

public record TwoFactorNumber(@Positive int code) {

}
