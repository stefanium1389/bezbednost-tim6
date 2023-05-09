package bezbednosttim6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bezbednosttim6.dto.CodeAndPasswordDTO;
import bezbednosttim6.dto.ErrorDTO;
import bezbednosttim6.dto.PasswordResetRequestDTO;
import bezbednosttim6.dto.SuccessDTO;
import bezbednosttim6.exception.ConditionNotMetException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.service.PasswordResetService;

@RestController
@RequestMapping("api/user/")
public class PasswordResetController {
	
	@Autowired
	private PasswordResetService passwordResetService;
	
	@PostMapping("resetPassword")
	private ResponseEntity<?> sendResetPasswordEmail(@RequestBody PasswordResetRequestDTO dto){
		
		try {
			SuccessDTO success = passwordResetService.postPasswordReset(dto.getEmail());
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
	
	@PutMapping("resetPassword")
	private ResponseEntity<?> resetPassword(@RequestBody CodeAndPasswordDTO dto){
		try {
			SuccessDTO success = passwordResetService.putPasswordReset(dto);
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
