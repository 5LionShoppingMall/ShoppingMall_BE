package com.ll.lion.user.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        String redirectUrlAfterSocialLogin = null;

        Optional<Cookie> optCookie = Arrays
                .stream(request.getCookies())
                .filter(c -> c.getName().equals("redirectUrlAfterSocialLogin"))
                .findFirst();

        if (optCookie.isPresent()) {
            redirectUrlAfterSocialLogin = optCookie.get().getValue();
        }

        if (redirectUrlAfterSocialLogin.startsWith("http://localhost:3000")) {

            String email = authentication.getName();
            response.sendRedirect("/api/oauth/socialLogin/Success?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));

            return;
        }


        super.onAuthenticationSuccess(request, response, authentication);
    }


}