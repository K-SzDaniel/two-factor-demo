package milthdev.twofactordemo.services;

import lombok.RequiredArgsConstructor;
import milthdev.twofactordemo.models.Account;
import milthdev.twofactordemo.repository.AccountRepository;
import org.springframework.stereotype.Service;

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
}
