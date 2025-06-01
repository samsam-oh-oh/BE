package samsamoo.ai_mockly.global.resolver;

import org.springframework.security.core.Authentication;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.annotation.LoginMember;

import java.util.Optional;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class) && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isGuest = (authentication == null || "anonymousUser".equals(authentication.getPrincipal()));

        if (parameter.getParameterType().equals(Optional.class)) {
            return isGuest ? Optional.empty() : Optional.of((Member) authentication.getPrincipal());
        }

        if(isGuest) {
            throw new IllegalArgumentException("게스트는 접근 불가합니다.");
        }

        return (Member) authentication.getPrincipal();
    }
}
