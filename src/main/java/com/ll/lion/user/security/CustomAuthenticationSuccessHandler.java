package com.ll.lion.user.security;

import com.ll.lion.user.entity.User;
import com.ll.lion.user.repository.RefreshTokenRepository;
import com.ll.lion.user.service.AuthService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EntityManager entityManager;
    private final AuthService authService;

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
            Long userId = ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            User user = entityManager.getReference(User.class,userId);
            String providerId = user.getProviderId();

            String accessToken = jwtTokenUtil.createAccessToken(providerId, List.of("USER"));
            String refreshToken = user.getRefreshToken().getKeyValue();

            Cookie cookie = optCookie.get();
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            authService.setTokenInCookie(accessToken,refreshToken,response);

            response.sendRedirect(redirectUrlAfterSocialLogin);
            return;
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}