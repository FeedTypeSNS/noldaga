package com.noldaga.domain.userdto.security;



import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Getter
public class KakaoOAuth2Response {

    private Long id;
    private LocalDateTime connectedAt;
    Map<String,Object> properties;
    KakaoAccount kakaoAccount;

    private KakaoOAuth2Response(Long id, LocalDateTime connectedAt, Map<String, Object> properties, KakaoAccount kakaoAccount) {
        this.id = id;
        this.connectedAt = connectedAt;
        this.properties = properties;
        this.kakaoAccount = kakaoAccount;
    }

    public static class KakaoAccount {
        private Boolean profileNicknameNeedsAgreement;
        private Profile profile;
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private Boolean isEmailValid;
        private Boolean isEmailVerified;
        private String email;

        private KakaoAccount(Boolean profileNicknameNeedsAgreement, Profile profile, Boolean hasEmail, Boolean emailNeedsAgreement, Boolean isEmailValid, Boolean isEmailVerified, String email) {
            this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
            this.profile = profile;
            this.hasEmail = hasEmail;
            this.emailNeedsAgreement = emailNeedsAgreement;
            this.isEmailValid = isEmailValid;
            this.isEmailVerified = isEmailVerified;
            this.email = email;
        }

        public static class Profile {
            private String nickname;

            private Profile(String nickname) {
                this.nickname = nickname;
            }
            public static Profile from(Map<String, Object> attributes){
                return new Profile(String.valueOf(attributes.get("nickname")));
            }
        }

        public static KakaoAccount from(Map<String, Object> attributes){
            return new KakaoAccount(
                    Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement"))),
                    Profile.from((Map<String, Object>) attributes.get("profile")),
                    Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                    Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                    String.valueOf(attributes.get("email"))
            );
        }

        public String nickname() {return this.profile.nickname;}


    }

    public static KakaoOAuth2Response from(Map<String,Object> attributes){
        return new KakaoOAuth2Response(
                Long.valueOf(String.valueOf(attributes.get("id"))),
                LocalDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ),
                (Map<String, Object>) attributes.get("properties"),
                KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account"))
        );
    }

    public String email() {return this.kakaoAccount.email;}
    public String nickname() {return this.kakaoAccount.nickname();}

}



//{
//        "id": 1234567890,
//        "connected_at": "2022-01-02T00:12:34Z",
//        "properties": {
//        "nickname": "홍길동"
//        },
//        "kakao_account": {
//        "profile_nickname_needs_agreement": false,
//        "profile": {
//        "nickname": "홍길동"
//        },
//        "has_email": true,
//        "email_needs_agreement": false,
//        "is_email_valid": true,
//        "is_email_verified": true,
//        "email": "test@gmail.com"
//        }
//}


