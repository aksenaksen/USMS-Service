package io.aksenaksen.demo.usms.member.security.oauth2;

import io.aksenaksen.demo.usms.member.application.required.MemberPort;
import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.OAuthProvider;
import io.aksenaksen.demo.usms.member.security.CustomUserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberPort memberRepository;

    public CustomOAuth2UserService(MemberPort memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Info userInfo = switch (registrationId) {
            case "naver" -> new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            //case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            //case "kakao" -> new KakaoUserInfo(oAuth2User.getAttributes());
            default -> throw new OAuth2AuthenticationException("지원하지 않는 OAuth 공급자입니다: " + registrationId);
        };
//String email, String nickname,String phoneNum,  OAuthProvider provider, String oauthId) {
        // 3. 회원 존재 여부 확인 후 없으면 생성
        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> memberRepository.save(
                        Member.createOfOAuth2(
                                userInfo.getEmail(),
                                userInfo.getName(),
                                userInfo.getMobNo(),
                                OAuthProvider.from(registrationId),
                                userInfo.getEmail()
                        )
                ));

        return new CustomUserDetails(member, oAuth2User.getAttributes());
    }
}
