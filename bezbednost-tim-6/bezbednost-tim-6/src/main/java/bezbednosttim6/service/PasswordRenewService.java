package bezbednosttim6.service;

import java.io.IOException;
import java.util.*;

import bezbednosttim6.dto.CodeAndRenewPasswordsDTO;
import bezbednosttim6.model.PasswordRenew;
import bezbednosttim6.repository.PasswordRenewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bezbednosttim6.dto.ChangePasswordRequestDTO;
import bezbednosttim6.dto.CodeAndPasswordDTO;
import bezbednosttim6.dto.SuccessDTO;
import bezbednosttim6.exception.ConditionNotMetException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.model.PasswordReset;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.PasswordResetRepository;
import bezbednosttim6.repository.UserRepository;
import jakarta.mail.MessagingException;


@Service
public class PasswordRenewService {

    @Autowired
    private PasswordRenewRepository passwordRenewRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MailingService mailService;
    @Autowired
    private PasswordEncoder encoder;

    private static int EXPIRES = 60*10; //10 minuta


    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        return token;
    }

    private String generatePasswordRenewRequest(String email) {
        User user = userRepo.findUserByEmail(email);
        if(user == null) {
            throw new ObjectNotFoundException("User does not exist, please register!");
        }
        PasswordRenew renew = new PasswordRenew();
        renew.setEmail(email);
        String token = this.generateToken();
        renew.setToken(token);
        renew.setTimestamp(new Date(System.currentTimeMillis()));
        renew.setExpires(new Date(System.currentTimeMillis()+(EXPIRES*1000)));
        passwordRenewRepo.save(renew);
        passwordRenewRepo.flush();
        return token;
    }

    public SuccessDTO postPasswordRenew(String email) {

        String token = generatePasswordRenewRequest(email);
        try {
            mailService.sendPasswordRenewMail(email, token);
        }
        catch (IOException e) {
            throw new ConditionNotMetException("Problem with email sending!");
        }
        return new SuccessDTO("Successfully sent email!");

    }

    public SuccessDTO putPasswordRenew(CodeAndRenewPasswordsDTO dto) {
        Optional<PasswordRenew> prOpt = passwordRenewRepo.findByToken(dto.getCode());
        if(prOpt.isEmpty()) {
            throw new ObjectNotFoundException("Code is incorrect!");
        }
        PasswordRenew actual = prOpt.get();
        if(actual.getExpires().before(new Date(System.currentTimeMillis()))) {
            throw new ConditionNotMetException("Code has expired!");
        }
        User user = userRepo.findUserByEmail(actual.getEmail());
        if(user == null) {
            throw new ObjectNotFoundException("User does not exist!");
        }

        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ConditionNotMetException("Old password not matching!");
        }

        if(!Objects.equals(dto.getNewPassword(), dto.getRepeatPassword())) {
            throw new ConditionNotMetException("Passwords not matching!");
        }

        if (Objects.equals(dto.getNewPassword(), dto.getOldPassword())) {
            throw new ConditionNotMetException("Enter new password!");
        }

        if (!isValid(dto.getNewPassword(), user.getEmail())) {
            throw new ConditionNotMetException("Passwords already used!");
        }

        actual.setSuccessful(true);
        actual.setOldPassword(encoder.encode(dto.getOldPassword()));
        passwordRenewRepo.save(actual);
        passwordRenewRepo.flush();

        String newEnc = encoder.encode(dto.getNewPassword());
        user.setPassword(newEnc);
        userRepo.save(user);
        userRepo.flush();

        return new SuccessDTO("Password successfully renewed!");
    }

    public Optional<PasswordRenew> findByLatestTimestamp(String email) {
        return passwordRenewRepo.findByLatestTimestamp(email);
    }

    private boolean isValid(String newPassword, String email) {
        List<String> oldPasswords = passwordRenewRepo.findOldPasswords(email);
        for (String password : oldPasswords) {
            if (encoder.matches(newPassword, password)) {
                return false;
            }
        }
        return true;
    }
}
