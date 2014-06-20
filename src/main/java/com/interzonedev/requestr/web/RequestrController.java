package com.interzonedev.requestr.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import ch.qos.logback.classic.Logger;

public abstract class RequestrController {

    protected final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    /**
     * Handles any {@code java.lang.Throwable} that may be thrown during any form processing.
     * 
     * @param t The {@code java.lang.Throwable} that was thrown.
     * @param request The current {@code javax.servlet.http.HttpServletRequest}.
     * 
     * @return Returns a forward to the view path to the error view with the "errorMessage" attribute set to the message
     *         of the exception.
     */
    @ExceptionHandler(Throwable.class)
    public ModelAndView handleException(Throwable t, HttpServletRequest request) {
        log.error("handleException", t);

        String stackTrace = ExceptionUtils.getStackTrace(t);

        ModelAndView mav = new ModelAndView("redirect:/error");
        mav.addObject("errorMessage", stackTrace);

        return mav;
    }

}
