package bezbednosttim6.service;

import java.io.IOException;
import java.util.*;

import bezbednosttim6.dto.CodeAndRenewPasswordsDTO;
import bezbednosttim6.model.PasswordRenew;
import bezbednosttim6.repository.PasswordRenewRepository;
import bezbednosttim6.security.LogIdUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(PasswordRenewService.class);
    private LogIdUtil util = new LogIdUtil();


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
            util.getNewLogId();
            logger.error("Problem with email sending!");
            throw new ConditionNotMetException("Problem with email sending!");
        }
        util.getNewLogId();
        logger.info("Successfully sent email!");
        return new SuccessDTO("Successfully sent email!");

    }

    public SuccessDTO putPasswordRenew(CodeAndRenewPasswordsDTO dto) {
        Optional<PasswordRenew> prOpt = passwordRenewRepo.findByToken(dto.getCode());
        if(prOpt.isEmpty()) {
            util.getNewLogId();
            logger.error("Code is incorrect!");
            throw new ObjectNotFoundException("Code is incorrect!");
        }
        PasswordRenew actual = prOpt.get();
        if(actual.getExpires().before(new Date(System.currentTimeMillis()))) {
            util.getNewLogId();
            logger.error("Code has expired!");
            throw new ConditionNotMetException("Code has expired!");
        }
        User user = userRepo.findUserByEmail(actual.getEmail());
        if(user == null) {
            util.getNewLogId();
            logger.error("User doesn't exist");
            throw new ObjectNotFoundException("User does not exist!");
        }

        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            util.getNewLogId();
            logger.error("Old password not matching for user: " + user.getId().toString());
            throw new ConditionNotMetException("Old password not matching!");
        }

        if(!Objects.equals(dto.getNewPassword(), dto.getRepeatPassword())) {
            util.getNewLogId();
            logger.error("Passwords not matching for user: " + user.getId().toString());
            throw new ConditionNotMetException("Passwords not matching!");
        }

        if (Objects.equals(dto.getNewPassword(), dto.getOldPassword())) {
            util.getNewLogId();
            logger.error("Enter new password (old password entered) for user: " + user.getId().toString());
            throw new ConditionNotMetException("Enter new password!");
        }

        if (!isValid(dto.getNewPassword(), user.getEmail())) {
            util.getNewLogId();
            logger.error("Enter new password (old password entered) for user: " + user.getId().toString());
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
        util.getNewLogId();
        logger.info("Password successfully renewed!");
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
