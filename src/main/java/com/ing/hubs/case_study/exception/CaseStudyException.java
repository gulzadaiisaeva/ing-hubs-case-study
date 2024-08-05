package com.ing.hubs.case_study.exception;


public class CaseStudyException extends Exception {
    public CaseStudyException(String message) {
        super(message);
    }

    public CaseStudyException(String message, Throwable cause) {
        super(message, cause);
    }

}
