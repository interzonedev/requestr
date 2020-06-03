package com.interzonedev.requestr.web;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


public abstract class RequestrController {

    private static final Logger log = LoggerFactory.getLogger(RequestrController.class);

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
