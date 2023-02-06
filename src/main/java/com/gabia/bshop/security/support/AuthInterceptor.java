package com.gabia.bshop.security.support;

import com.gabia.bshop.exception.UnAuthorizedException;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.security.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler) {
        if (!(handler instanceof HandlerMethod) || getLoginAnnotation(handler) == null) {
            return true;
        }
        if (hasAuthorization(request)) {
            validateAuthorization(request);
            validateAdminRequired(request, handler);
            return true;
        }
        validateTokenRequired(handler);
        return true;
    }

    private boolean hasAuthorization(final HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION) != null;
    }

    private void validateAuthorization(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtProvider.isValidToken(authorizationHeader)) {
            throw new UnAuthorizedException("토큰이 만료됐습니다.");
        }
    }

    private void validateAdminRequired(final HttpServletRequest request, final Object handler) {
        Login auth = getLoginAnnotation(handler);
        if (auth != null && auth.admin() && isNotAdmin(request)) {
            throw new UnAuthorizedException("관리자가 아닙니다.");
        }
    }

    private boolean isNotAdmin(final HttpServletRequest request) {
        final MemberPayload payload = jwtProvider.getPayload(
                request.getHeader(HttpHeaders.AUTHORIZATION));
        return !payload.isAdmin();
    }

    private void validateTokenRequired(final Object handler) {
        Login auth = getLoginAnnotation(handler);
        if (auth != null && auth.required()) {
            throw new UnAuthorizedException("토큰이 존재하지 않습니다.");
        }
    }

    private Login getLoginAnnotation(final Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.getMethodAnnotation(Login.class);
    }
}
