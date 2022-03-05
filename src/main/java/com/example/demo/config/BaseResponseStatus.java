package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),
    //accounts
    ACCOUNT_DOESNT_EXISTS(false, 2011, "계정이 존재하지 않습니다."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    // [POST] /accounts
    POST_ACCOUNTS_EMPTY_ID(false, 2018, "ID를 입력해주세요."),
    POST_ACCOUNTS_INVALID_ID(false, 2019, "ID 형식을 확인해주세요."),
    POST_ACCOUNTS_EXISTS_ID(false,2020,"중복된 ID입니다."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    // /accounts/{userNum}
    MODIFY_FAIL_USER_ID(false,4015,"유저ID 수정 실패"),
    MODIFY_FAIL_USER_PHONE_NUM(false,4016,"유저 전화번호 수정 실패"),
    MODIFY_FAIL_USER_PAYMENT(false,4017,"유저 결제수단 수정 실패"),
    MODIFY_FAIL_USER_MEMBERSHIP(false,4018,"유저 멤버쉽 수정 실패"),
    MODIFY_ACCOUNTS_INVALID_ID(false, 4019, "ID 형식을 확인해주세요."),
    MODIFY_FAIL_USER_PW(false,4020,"유저PW 수정 실패"),
    // /settings
    PROFILE_ID_DOESNT_EXISTS(false,4021,"해당 프로필은 존재하지 않습니다."),
    MODIFY_FAIL_PROFILE_LANGUAGE(false,4022,"프로필 언어 설정 실패"),
    MODIFY_FAIL_PROFILE_RESTRICTION(false,4023,"프로필 시청 제한 수정 실패"),
    MODIFY_FAIL_PROFILE_LOCK(false,4024,"프로필 잠금 설정 실패"),
    MODIFY_FAIL_PROFILE_PLAYBACK(false,4025,"프로필 재생 설정 실패"),
    MODIFY_FAIL_PROFILE_COMMUNICATION(false,4026,"프로필 광고 수신 설정 실패"),
    MODIFY_FAIL_PROFILE_RATED_LIST(false,4027,"프로필이 평가한 리스트 수정 실패"),
    MODIFY_FAIL_PROFILE_WATCHED_LIST(false,4028,"프로필이 시청한 리스트 수정 실패"),

    CREATE_FAIL_PROFILE_RATED_LIST(false,4050,"프로필이 평가한 리스트 추가 실패"),
    CREATE_FAIL_PROFILE_WATCHED_LIST(false,4051,"프로필이 시청한 리스트 추가 실패"),
    PROFILE_WATCHED_RECORD_DOESNT_EXISTS(false,4052,"해당 영상 시청 기록이 없습니다. 생성후에 시도하세요."),
    PROFILE_RATED_RECORD_DOESNT_EXISTS(false,4053,"해당 영상을 평가한 기록이 없습니다. 생성 후에 시도하세요."),
    PROFILE_WATCHED_RECORD_ALREADY_EXISTS(false,4053,"해당 영상을 시청한 기록이 존재합니다. 수정을 시도하세요."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    // 5000 (video 관련)
    GET_VIDEO_LIST_FAIL(false,5001,"영상 리스트 조회에 실패했습니다.");
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
