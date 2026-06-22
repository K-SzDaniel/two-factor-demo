package milthdev.twofactordemo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import milthdev.twofactordemo.models.Account;
import milthdev.twofactordemo.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private static final String EMAIL = "test@test.com";

    private final AccountRepository repository;

    public Account getAccount(String email) {
        return repository.findByEmail(email)
                .orElseGet(this::init);
    }

    public void save(Account account) {
        repository.save(account);
    }

    private Account init() {
        return repository.save(Account.builder()
                .email(EMAIL)
                .build());
    }

    public void resetTwoFactorSecret(String email) {
        Account account = getAccount(email);
        account.setSecret(null);
        save(account);
        log.info("Two factor secret reset for email: {}", email);
    }
}
