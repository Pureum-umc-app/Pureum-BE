package com.umc.pureum.global.config.Response;

import lombok.Getter;

/* 에러 코드 관리 */
@Getter
public enum BaseResponseStatus {
    /*
        code는 순차적으로 작성
        2010 ~ 2019 유저 관련 형식적 validation
        2020 ~ 2029 유저 관련 논리적 validation
        2030 ~ 2039 홈 관련 형식적 validation
        2040 ~ 2049 홈 관련 논리적 validation
        등등
     */

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    NOT_ALLOW_METHOD(false, 405, "잘못된 메소드입니다."),
    INTERNAL_SERVER_ERROR(false, 500, "서버에러 입니다."),
    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003, "권한이 없는 유저의 접근입니다."),
    INVALID_USER(false, 2004, "존재하지 않는 유저입니다."),
    INVALID_IMAGE_FILE(false, 2005, "이미지파일이 아닙니다."),
    INVALID_KAKAO_TOKEN(false, 2006, "유효한 카카오토큰이 아닙니다."),
    // users
    POST_USERS_EMPTY_NICKNAME(false, 2010, "닉네임을 입력해주세요."),
    POST_USERS_EMPTY_AGE(false, 2012, "나이를 입력해주세요."),
    POST_USERS_EMPTY_EMAIL(false, 2013, "이메일을 입력해주세요."),
    POST_USERS_EMPTY_PUSH(false, 2014, "푸시 아이디를 입력해주세요."),
    POST_USERS_EMPTY_ACCESS_TOKEN(false, 2015, "소셜 아이디를 입력해주세요."),

    POST_USERS_INVAlID_GENDER(false, 2020, "성별을 확인해주세요."),
    POST_USERS_INVAlID_AGE(false, 2021, "나이를 확인해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2022, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false, 2030, "중복된 이메일입니다."),
    POST_USERS_EXISTS_NICKNAME(false, 2031, "중복된 닉네임입니다."),
    POST_USERS_NO_EXISTS_EMAIL(false, 2032, "존재하지 않는 이메일입니다."),
    POST_USERS_EXISTS(false, 2033, "이미 가입된 회원입니다."),
    POST_USERS_NO_EXISTS_USER(false, 2034, "존재하지 않는 회원입니다."),
    POST_USE_EXISTS_USAGE_TIME(false, 2051, "이미 목표 시간을 설정하였습니다."),
    POST_BADGE_EXITS(false, 2060, "이미 존재하는 배지입니다."),

    //sentence
    POST_SENTENCE_EMPTY(false, 2040, "문장을 입력해주세요."),
    POST_SENTENCE_NO_EXISTS_KEYWORD(false, 2041, "키워드가 포함되어있지 않습니다."),
    GET_SENTENCE_INVALID_SORT_METHOD(false, 2042, "정렬 방식이 잘못되었습니다."),
    POST_SENTENCE_EXISTS(false, 2043, "이미 존재하는 대결 문장입니다."),
    GET_SENTENCE_EMPTY(false, 2044, "없는 문장입니다."),

    // battle
    POST_BATTLE_EMPTY_SENTENCE(false, 2050, "대결 문장을 입력해야 합니다."),
    POST_BATTLE_NO_EXIST_KEYWORD(false, 2051,"존재하지 않는 키워드입니다."),
    POST_BATTLE_ALREADY_EXIST_KEYWORD(false, 2052,"이미 대결에 사용한 단어입니다."),
    GET_BATTLE_INVALID_STATUS(false, 2053,"대기 중 혹은 종료된 대결만 반환 가능합니다."),
    GET_BATTLE_FINISH_STATUS(false, 2054,"종료된 대결 입니다."),
    Get_BATTLE_RUN_STATUS(false, 2055,"종료된 대결 아닙니다."),
    Get_BATTLE_NO_ACCEPT_STATUS(false, 2056,"수락되지 않은 대결입니다."),
    POST_BATTLE_ALREADY_EXIST_SENTENCE(false, 2057,"이미 대결에 사용한 문장입니다."),
    POST_INVALID_DURATION(false, 2058, "유효하지 않은 기간입니다."),
    NOT_FOUND_BATTLE_SENTENCE(false, 2059, "배틀 문장을 찾을 수 없습니다."),
    NOT_FOUND_SENTENCE(false,2060,"문장을 찾을 수 없습니다."),
    INVALID_BATTLE(false,2061,"존재하지 않는 대결입니다."),



    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false, 3014, "없는 아이디거나 비밀번호가 틀렸습니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false, 4014, "유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    FCM_ERROR(false, 4020, "FCM 에러"), NOT_FOUND_WAIT_BATTLE(false,4013 ,"대기중인 대결을 찾을 수 없습니다." );
    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
