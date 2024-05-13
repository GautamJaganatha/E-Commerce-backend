package com.example.ecom.controllers;

import com.example.ecom.dto.AuthenticationReuest;
import com.example.ecom.dto.SignupRequest;
import com.example.ecom.dto.UserDto;
import com.example.ecom.entity.User;
import com.example.ecom.repository.UserRespository;
import com.example.ecom.services.jwt.auth.AuthService;
import com.example.ecom.utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin("*")
public class AuthController {

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRespository userRespository;

    private final JWTUtils jwtUtils;

    private final AuthService authService;

    @PostMapping("/authenticate")
    private void createAuthenticationToken(@RequestBody AuthenticationReuest authenticationReuest,
                                           HttpServletResponse response) throws IOException, JSONException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationReuest.getUsername(),authenticationReuest.getPassword()));

        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect Username");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationReuest.getUsername());
        Optional<User> optionalUser = userRespository.findFirstByEmail(userDetails.getUsername());

        final String jwt= jwtUtils.generateToken(userDetails.getUsername());

        if (optionalUser.isPresent()){
            response.getWriter().write(new JSONObject()
                    .put("userID",optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .toString()
            );



            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Access-Control-Allow","Authorization, X-PINGOTHER, Origin,"+
                    "X-Requested-Width, Content-Type, Accept, X-Custom-header");
            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        if(authService.hasUserWithEmail(signupRequest.getEmail())){
            return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

}
