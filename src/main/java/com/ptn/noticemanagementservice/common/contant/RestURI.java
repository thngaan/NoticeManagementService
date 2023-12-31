package com.ptn.noticemanagementservice.common.contant;

public final class RestURI {

    public static final String API = "/api";
    public static final String ID = "/{id}";
    public static final String LOGIN = "/login";
    public static final String REGISTRATION = "/registration";
    private static final String DOCUMENT = "/documents";
    public static final String DOCUMENT_API = API + DOCUMENT;
    private static final String NOTICE = "/notices";
    public static final String NOTICE_API = API + NOTICE;

    private RestURI() {
    }

}
