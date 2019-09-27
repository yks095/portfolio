package com.dblab.controller;

import com.dblab.config.jwtConfig.JwtTokenUtil;
import com.dblab.domain.JwtToken;
import com.dblab.domain.User;
import com.dblab.dto.UserDto;
import com.dblab.repository.UserRepository;
import com.dblab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginView() {
        return "/login";
    }

    @PostMapping("/login/success")
    public String loginSuccess() {
        return "redirect:/main";
    }

    @PostMapping("/login/authenticate")
    public ResponseEntity<?> createJwtToken(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);

        else {
            authenticate(userDto.getUsername(), userDto.getPassword());

            User currentUser = userService.findUserByUsername(userDto);

            if (currentUser == null) return new ResponseEntity<>(userDto.getUsername() + " 유저를 찾을 수 없습니다."
                    , HttpStatus.BAD_REQUEST);
            else {
                final String token = jwtTokenUtil.generateToken(currentUser);
                return ResponseEntity.ok(new JwtToken(token));
            }

        }
    }

//    @PostMapping("/logouting")
//    public ResponseEntity<?> logout(@Valid @RequestBody UserDto userDto, BindingResult bindingResult){
//        if (bindingResult.hasErrors()){
//            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//        else{
//            String jwtToken = response.getHeader("Authorization");
//            jwtToken = jwtToken.substring(7);
//            jwtTokenUtil.deleteToken(jwtToken);
//            return new ResponseEntity<>("{}", HttpStatus.OK);
//        }
//    }

    @GetMapping("/logouting")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = request.getHeader("Authorization");
        jwtToken = jwtToken.substring(7);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException d) {
            throw new Exception("USER_DISABLE", d);
        } catch (BadCredentialsException b) {
            throw new Exception("INVALID_CREDENTIALS", b);
        }
    }
}
