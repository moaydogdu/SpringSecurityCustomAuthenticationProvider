package com.moaydogdu.ssexample4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This is where we are implementing authentication logic.
     *
     * If the request is authentication you should return here an
     * fully authenticated Authentication instance.
     *
     * If the request is not authenticated you should throw
     * Any kind of AuthenticationException.
     *
     * The Authentication isn't supported by this authentication provider
     * then you return null.
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(
            Authentication authentication
    ) throws AuthenticationException {
        System.out.println("CustomAuthenticationProvider worked.");
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        UserDetails user = userDetailsService.loadUserByUsername(username);

        //It's actually unnecessary, inmemoryuserdetails doing it.
        if(user != null){
            if(passwordEncoder.matches(password,user.getPassword())){
                var authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,password,user.getAuthorities()
                );

                return authenticationToken;
            }
        }

        throw  new BadCredentialsException("Error!");
    }

    /**
     * This method called before authenticate metod by the authentication manager.
     * Paramater is should be type of Authentication we accept.
     *
     * @param authenticationType
     * @return
     */
    @Override
    public boolean supports(
            Class<?> authenticationType
    ) {
        return UsernamePasswordAuthenticationToken.class.equals(authenticationType);
    }
}
