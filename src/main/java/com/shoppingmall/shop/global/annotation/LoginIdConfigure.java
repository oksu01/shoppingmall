package com.shoppingmall.shop.global.annotation;

import com.shoppingmall.shop.security.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginIdConfigure implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {//현재 파라미터를 리졸버가 지원하는지에 대한 boolean

        boolean hasLoginAccountIdAnnotation = parameter.hasParameterAnnotation(LoginId.class);
        boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAccountIdAnnotation && hasLongType;
    }

    @Override //실제로 바인딩할 객체
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // 테스트 시 등록된 회원이 아무도 없을 경우 널포인터 예외 방지를 위해 추가
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            return -1L;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal == "anonymousUser") {
            return -1L;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) principal;
        return customUserDetails.getMember().getMemberId();

    }
    //HandlerMethodArgumentResolver를 상속받은 객체는 메서드 두개를 구현해주어야 함
    //컨트롤러 메서드에서 특정 조건에 맞는 파라미터가 있을 때 원하는 값을 바인딩해주는 인터페이스
    //

}

