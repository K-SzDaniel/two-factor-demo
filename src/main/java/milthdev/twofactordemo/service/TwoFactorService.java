package milthdev.twofactordemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import milthdev.twofactordemo.model.Account;
import milthdev.twofactordemo.model.TwoFactorRegResponse;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwoFactorService {

    private static final String ALGORITHM = "HmacSHA1";
    private static final int SECRET_KEY_LENGTH = 20;
    private static final int TOTP_MODULUS = 1000000;
    private static final int TOTP_INTERVAL = 30;

    private final SecureRandom secureRandom;
    private final Base32 base32;
    private final AccountService accountService;

    public TwoFactorRegResponse generateTwoFactorUrl(String email) {
        Account account = accountService.getAccount(email);
        if (account.getSecret() != null) {
            return TwoFactorRegResponse.builder()
                    .url(generateOtpAuthUrl(account, account.getSecret()))
                    .build();
        }
        byte[] secret = new byte[SECRET_KEY_LENGTH];
        secureRandom.nextBytes(secret);
        String secretKey = base32.encodeToString(secret);
         account = account.toBuilder()
                .secret(secretKey)
                .build();
        TwoFactorRegResponse twoFactorResponse = TwoFactorRegResponse.builder()
                .url(generateOtpAuthUrl(account, secretKey))
                .build();
        log.info("Generated two factor response: {}", twoFactorResponse);
        accountService.save(account);
        return twoFactorResponse;
    }

    public void validateTwoFactorCode(int code, String email) throws NoSuchAlgorithmException,
            InvalidKeyException {
        Account account = accountService.getAccount(email);
        log.info("Account: {}", account);
        SecretKeySpec secretKeySpec = new SecretKeySpec(base32.decode(account.getSecret()), ALGORITHM);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(secretKeySpec);
        List<Integer> totpCodes = getCurrentTOTP(mac);
        log.info("Current TOTPs: {}, user TOTP: {}", totpCodes, code);
        if (totpCodes.stream().noneMatch(i -> i == code)) {
            throw new RuntimeException("Invalid two factor code");
        }
    }

    private String generateOtpAuthUrl(Account account, String secretKey) {
        return String.format("otpauth://totp/two-factor-demo:%s?secret=%s&issuer=MilthDev",
                account.getEmail(), secretKey);
    }

    private List<Integer> getCurrentTOTP(Mac mac) {
        List<Integer> totps = new ArrayList<>();
        long currentTimeStamp = getCurrentSecond() / TOTP_INTERVAL;
        totps.add(getTOTPByTimeStamp(mac, currentTimeStamp - 1));
        totps.add(getTOTPByTimeStamp(mac, currentTimeStamp));
        totps.add(getTOTPByTimeStamp(mac, currentTimeStamp + 1));
       return totps;
    }

    private int getTOTPByTimeStamp(Mac mac, long currentTimeStamp) {
        byte[] hmacBytes = mac.doFinal(ByteBuffer.allocate(8).putLong(currentTimeStamp).array());
        int offset = hmacBytes[hmacBytes.length - 1] & 0xF;
        int value = (hmacBytes[offset] & 0x7F) << 24
                | (hmacBytes[offset + 1] & 0xFF) << 16
                | (hmacBytes[offset + 2] & 0xFF) << 8
                | (hmacBytes[offset + 3] & 0xFF);
        return value % TOTP_MODULUS;
    }

    private long getCurrentSecond() {
        return System.currentTimeMillis() / 1000;
    }
}
