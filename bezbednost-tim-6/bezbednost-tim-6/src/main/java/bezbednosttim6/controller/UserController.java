package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.exception.ActionExpiredException;
import bezbednosttim6.exception.ConditionNotMetException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.ResourceConflictException;
import bezbednosttim6.mapper.UserDTOwithPasswordMapper;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.PasswordRenew;
import bezbednosttim6.model.User;
import bezbednosttim6.security.LogIdUtil;
import bezbednosttim6.security.TokenUtils;
import bezbednosttim6.service.*;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
@RequestMapping("api/user/")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils jwtTokenUtil;

    @Autowired
    private ActivationService activationService;
    @Autowired
    private PasswordRenewService passwordRenewService;

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private LogIdUtil util = new LogIdUtil();

    @PostMapping("login/first")
    public ResponseEntity<?> postLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            util.getNewLogId();
            logger.info("Somebody is trying to log in");
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(authReq);

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);

//            System.out.println("lmaoo");

            String email = loginRequestDTO.getEmail();
            User user = userService.findUserByEmail(email);
            Optional<PasswordRenew> lastRenewOpt = passwordRenewService.findByLatestTimestamp(email);
            if (lastRenewOpt.isEmpty()) {
                util.getNewLogId();
                logger.warn("Password should be renewed before logging in for user: " + user.getId().toString());
                passwordRenewService.postPasswordRenew(email);
                util.getNewLogId();
                logger.info("Password renewal email was successful sent to user: " + user.getId().toString());
                return new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
            } else {
                util.getNewLogId();
                logger.warn("Two-factor authentication is required for user: " + user.getId());
                LoginCreateCodeDTO response = userService.loginStepOne(loginRequestDTO);
                util.getNewLogId();
                logger.info("TFA email was successfully sent to user: " + user.getId().toString());
                return new ResponseEntity<LoginCreateCodeDTO>(response, HttpStatus.OK);
            }
        } catch (AuthenticationException e) {
            util.getNewLogId();
            logger.error("Login failed: " + e.getMessage());
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            util.getNewLogId();
            logger.error("Login failed: " + e.getMessage());
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("login/second")
    public ResponseEntity<?> postLoginStepTwo(@RequestBody LoginSecondStepRequestDTO loginSecondStepRequestDTO) {
        try {
            util.getNewLogId();
            logger.info("User is required to enter valid code and thus confirm their identity");
            LoginResponseDTO response = userService.loginStepTwo(loginSecondStepRequestDTO);
            util.getNewLogId();
            logger.info("User successfully confirmed their identity");
            return new ResponseEntity<LoginResponseDTO>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("User failed to confirm identity");
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("loginWithGoogle")
    public ResponseEntity<?> postGoogleLogin(@RequestBody String credential) {
        try {
            util.getNewLogId();
            logger.info("User is trying to login using google services");
            String token = credential.replace("\"", "");
            LoginResponseDTO response = userService.loginWithGoogle(token);
            util.getNewLogId();
            logger.info("Logging using google services was successful");
            return new ResponseEntity<LoginResponseDTO>(response, HttpStatus.OK);
        } catch (Exception e) {
            util.getNewLogId();
            logger.error("Logging in using google failed");
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("refreshToken")
    public ResponseEntity<?> postRefresh(@RequestBody LoginResponseDTO dto) {
        try {
            util.getNewLogId();
            logger.info("User session has expired and is requested to be refreshed");
            SuccessDTO new_token = new SuccessDTO(userService.refreshToken(dto));
            util.getNewLogId();
            logger.info("Successfully refreshed session");
            return new ResponseEntity<>(new_token, HttpStatus.OK);
        } catch (RuntimeException e) {
            util.getNewLogId();
            logger.error("Failed to automatically give user a new session");
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error, HttpStatus.NOT_EXTENDED);
        }
    }

    //@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("proba")
//	public ResponseEntity<?> proba (Principal principal)
//	{
//		return new ResponseEntity<>(passwordEncoder.encode("admin"),HttpStatus.OK);
//	}


    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO userRequest) throws UnsupportedEncodingException {
        try {
            util.getNewLogId();
            logger.info("Somebody tried to register to the system");
            System.out.println(userRequest.getValidationType());
            User newUser = userService.registerUser(userRequest);
//            logger.info("New user with id: " + userRequest.getId().toString() + " successfully registered to the system");
            return new ResponseEntity<>(new RegisterResponseDTO(newUser), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            util.getNewLogId();
            logger.error("User failed to register " + e.getMessage());
            ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(dto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activate/{activationId}")
    public ResponseEntity<?> activatePassenger(@PathVariable("activationId") String id) {
        try {
            util.getNewLogId();
            logger.info("User is trying to activate account through email or phone number");
            SuccessDTO dto = activationService.activatePassenger(id);
            util.getNewLogId();
            logger.info("User successfully activated account");
            return new ResponseEntity<SuccessDTO>(dto, HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
            util.getNewLogId();
            logger.info("Account activation " + id + "filed activated account");
            ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(dto, HttpStatus.NOT_FOUND);
        } catch (ActionExpiredException e) {
            ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(dto, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/activate/resend/{activationId}")
    public ResponseEntity<?> activatePassengerResend(@PathVariable("activationId") String id) {
        try {
            SuccessDTO dto = userService.resendActivation(id);
            return new ResponseEntity<SuccessDTO>(dto, HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
            ErrorDTO dto = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(dto, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createRoot(Principal principal) {
        return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
    }

    @PostMapping("recaptcha")
    public ResponseEntity<?> captcha(@RequestBody RecaptchaToken dto) {
        try {
            util.getNewLogId();
            logger.warn("Recaptcha toke is being read");
            if (userService.isValidCaptcha(dto.getToken())) {
                util.getNewLogId();
                logger.info("Recaptcha token is successful");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                util.getNewLogId();
                logger.error("Recaptcha token is ivalid");
                ErrorDTO error = new ErrorDTO("BACK OFF ROBOT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                return new ResponseEntity<ErrorDTO>(error, HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            util.getNewLogId();
            logger.error("Recaptcha token is ivalid");
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error, HttpStatus.NOT_EXTENDED);
        }
    }


	@GetMapping("hello")
	public String hello() {
		util.getNewLogId();
		logger.info("bla");
		return "prosao";
	}

}