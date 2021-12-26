package com.github.onblog.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by Martin 2018/9/4/004 10:29
 */
public class CustomException extends RuntimeException {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public CustomException(String msg){
        super(msg);
        logger.error(msg);
    }


}
