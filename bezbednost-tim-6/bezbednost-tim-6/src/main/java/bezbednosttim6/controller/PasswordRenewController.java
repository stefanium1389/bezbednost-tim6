package bezbednosttim6.controller;

import bezbednosttim6.dto.*;
import bezbednosttim6.service.PasswordRenewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bezbednosttim6.exception.ConditionNotMetException;
import bezbednosttim6.exception.ObjectNotFoundException;

@RestController
@RequestMapping("api/user/")
public class PasswordRenewController {

    @Autowired
    private PasswordRenewService passwordRenewService;

    @PostMapping("renewPassword")
    private ResponseEntity<?> sendRenewPasswordEmail(@RequestBody PasswordResetRequestDTO dto){

        try {
            SuccessDTO success = passwordRenewService.postPasswordRenew(dto.getEmail());
            return new ResponseEntity<SuccessDTO>(success,HttpStatus.NO_CONTENT);
        }
        catch(ObjectNotFoundException e){
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error,HttpStatus.NOT_FOUND);
        }
        catch(ConditionNotMetException e) {
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("renewPassword")
    private ResponseEntity<?> renewPassword(@RequestBody CodeAndRenewPasswordsDTO dto){
        try {
            SuccessDTO success = passwordRenewService.putPasswordRenew(dto);
            return new ResponseEntity<SuccessDTO>(success,HttpStatus.NO_CONTENT);
        }
        catch(ObjectNotFoundException e){
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error,HttpStatus.NOT_FOUND);
        }
        catch(ConditionNotMetException e) {
            ErrorDTO error = new ErrorDTO(e.getMessage());
            return new ResponseEntity<ErrorDTO>(error,HttpStatus.BAD_REQUEST);
        }
    }
}
