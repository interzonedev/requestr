package com.interzonedev.requestr.web.send;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.interzonedev.httpcore.Method;
import com.interzonedev.httpcore.Request;

public class ComponentsForm {

    @NotEmpty
    private String url;

    @NotEmpty
    private String method;

    // @Pattern(regexp = "^\\s*([^=]+=)*\\s*$")
    private String headerValues;

    // @Pattern(regexp = "^\\s*([^=]+=)*\\s*$")
    private String parameterValues;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeaderValues() {
        return headerValues;
    }

    public void setHeaderValues(String headerValues) {
        this.headerValues = headerValues;
    }

    public String getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(String parameterValues) {
        this.parameterValues = parameterValues;
    }

    public Request toRequest() {
        Method requestMethod = Method.valueOf(getMethod());

        Map<String, List<String>> headers = new HashMap<String, List<String>>();

        translateInputToComponents(getHeaderValues(), headers);

        Map<String, List<String>> parameters = new HashMap<String, List<String>>();

        translateInputToComponents(getParameterValues(), parameters);

        return Request.newBuilder().setUrl(getUrl()).setMethod(requestMethod).setHeaders(headers)
                .setParameters(parameters).build();
    }

    private void translateInputToComponents(String input, Map<String, List<String>> components) {
        if (StringUtils.isNotBlank(input)) {
            String[] nameValuePairs = input.split("&");
            for (String nameValuePair : nameValuePairs) {
                String[] nameValueParts = nameValuePair.split("=");
                String name = nameValueParts[0];
                String value = "";
                if (nameValueParts.length > 1) {
                    value = nameValueParts[1];
                }

                List<String> values = components.get(name);
                if (null == values) {
                    values = new ArrayList<String>();
                    components.put(name, values);
                }
                values.add(value);
            }
        }
    }
}
