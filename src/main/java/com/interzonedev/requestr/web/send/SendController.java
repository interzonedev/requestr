package com.interzonedev.requestr.web.send;

import com.interzonedev.httpagent.RequestService;
import com.interzonedev.httpcore.Method;
import com.interzonedev.httpcore.Request;
import com.interzonedev.httpcore.Response;
import com.interzonedev.requestr.web.RequestrController;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Controller
@RequestMapping(value = "/send")
public class SendController extends RequestrController {

    private static final Logger log = LoggerFactory.getLogger(SendController.class);

    private static final String JSON_FORM_VIEW = "send/json/jsonForm";
    private static final String COMPONENTS_FORM_VIEW = "send/components/componentsForm";
    private static final String RESPONSE_VIEW = "send/response";

    @Inject
    @Named("requestService")
    private RequestService requestService;

    private Map<String, String> requestMethods;

    @PostConstruct
    public void init() {
        Map<String, String> requestMethodsMutable = new LinkedHashMap<String, String>();

        requestMethodsMutable.put(Method.GET.toString(), Method.GET.toString());
        requestMethodsMutable.put(Method.POST.toString(), Method.POST.toString());
        requestMethodsMutable.put(Method.PUT.toString(), Method.PUT.toString());
        requestMethodsMutable.put(Method.DELETE.toString(), Method.DELETE.toString());
        requestMethodsMutable.put(Method.OPTIONS.toString(), Method.OPTIONS.toString());
        requestMethodsMutable.put(Method.HEAD.toString(), Method.HEAD.toString());
        requestMethodsMutable.put(Method.TRACE.toString(), Method.TRACE.toString());

        requestMethods = Collections.unmodifiableMap(requestMethodsMutable);
    }

    @ModelAttribute("requestMethods")
    public Map<String, String> getRequestMethods() {
        return requestMethods;
    }

    @RequestMapping(method = RequestMethod.GET, value = "json")
    public String displayJsonForm(Model model) {
        log.debug("displayJsonForm: Start");

        model.addAttribute("jsonForm", new JsonForm());

        log.debug("displayJsonForm: End");

        return JSON_FORM_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "json")
    public String sendJsonRequest(Model model, @Valid JsonForm jsonForm, BindingResult result) {
        log.debug("sendJsonRequest: Start");

        if (result.hasErrors()) {
            log.debug("sendJsonRequest: Form has errors");
            return JSON_FORM_VIEW;
        }

        Request request = null;
        try {
            request = jsonForm.toRequest();
        } catch (InvalidJsonException ije) {
            log.debug("sendJsonRequest: JSON has errors - " + ije.getMessage());
            result.rejectValue("input", "InvalidJson.jsonForm.input", new Object[] { ije.getMessage() }, null);
            return JSON_FORM_VIEW;
        }

        Response response = null;
        try {
            log.debug("sendJsonRequest: Sending request " + request);
            Future<Response> responseFuture = requestService.doRequest(request);
            log.debug("sendJsonRequest: Waiting for response");
            response = responseFuture.get();
        } catch (Throwable t) {
            log.error("sendJsonRequest: Error making HTTP request", t);
            String errorMessage = t.getMessage();
            String stackTrace = ExceptionUtils.getStackTrace(t);
            result.reject("error.request", new Object[] { errorMessage, stackTrace }, null);
            return JSON_FORM_VIEW;
        }

        model.addAttribute("response", response);

        log.debug("sendJsonRequest: End");

        return RESPONSE_VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "components")
    public String displayComponentsForm(Model model) {
        log.debug("displayComponentsForm: Start");

        model.addAttribute("componentsForm", new ComponentsForm());

        log.debug("displayComponentsForm: End");

        return COMPONENTS_FORM_VIEW;
    }

    @RequestMapping(method = RequestMethod.POST, value = "components")
    public String sendComponentsRequest(Model model, @Valid ComponentsForm componentsForm, BindingResult result) {
        log.debug("sendComponentsRequest: Start");

        if (result.hasErrors()) {
            log.debug("sendComponentsRequest: Form has errors");
            return COMPONENTS_FORM_VIEW;
        }

        Request request = componentsForm.toRequest();

        Response response = null;
        try {
            log.debug("sendComponentsRequest: Sending request " + request);
            Future<Response> responseFuture = requestService.doRequest(request);
            log.debug("sendComponentsRequest: Waiting for response");
            response = responseFuture.get();
        } catch (Throwable t) {
            log.error("sendComponentsRequest: Error making HTTP request", t);
            String errorMessage = t.getMessage();
            String stackTrace = ExceptionUtils.getStackTrace(t);
            result.reject("error.request", new Object[] { errorMessage, stackTrace }, null);
            return COMPONENTS_FORM_VIEW;
        }

        model.addAttribute("response", response);

        log.debug("sendComponentsRequest: End");

        return RESPONSE_VIEW;
    }

}
