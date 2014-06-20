package com.interzonedev.requestr.web.send;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interzonedev.httpagent.Method;
import com.interzonedev.httpagent.Request;

public class JsonForm {

    private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

    @NotEmpty
    private String input;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Request toRequest() throws InvalidJsonException {

        Map<String, Object> requestValues = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(Feature.ALLOW_COMMENTS, true);
            requestValues = (objectMapper).readValue(getInput(), new TypeReference<Map<String, Object>>() {
            });
        } catch (Throwable t) {
            String errorMessage = "Invalid JSON format";
            log.error("jsonToRequest: " + errorMessage, t);

            if (StringUtils.isNotBlank(t.getMessage())) {
                errorMessage += " - " + t.getMessage();
            }

            throw new InvalidJsonException(errorMessage, t);
        }

        String url = (String) requestValues.get("url");

        if (StringUtils.isBlank(url)) {
            throw new InvalidJsonException("The \"url\" key must be set");
        }

        String method = (String) requestValues.get("method");

        if (StringUtils.isBlank(method)) {
            throw new InvalidJsonException("The \"method\" key must be set");
        }

        Method requestMethod = null;
        try {
            requestMethod = Method.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException iae) {
            throw new InvalidJsonException("The \"method\" value of \"" + method + "\" is invalid");
        }

        @SuppressWarnings("unchecked")
        Map<String, String> headerValues = (Map<String, String>) requestValues.get("headers");

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        if ((null != headerValues) && !headerValues.isEmpty()) {
            for (String headerName : headerValues.keySet()) {
                String headerValue = headerValues.get(headerName);
                headers.put(headerName, Arrays.asList(new String[] { headerValue }));
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, String> parameterValues = (Map<String, String>) requestValues.get("parameters");

        Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        if ((null != parameterValues) && !parameterValues.isEmpty()) {
            for (String parameterName : parameterValues.keySet()) {
                String parameterValue = parameterValues.get(parameterName);
                parameters.put(parameterName, Arrays.asList(new String[] { parameterValue }));
            }
        }

        return new Request(url, requestMethod, headers, parameters);
    }
}
