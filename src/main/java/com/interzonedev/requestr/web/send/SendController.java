package com.interzonedev.requestr.web.send;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.interzonedev.requestr.service.InvalidJsonException;
import com.interzonedev.requestr.service.RequestrMethod;
import com.interzonedev.requestr.service.RequestrRequest;
import com.interzonedev.requestr.service.RequestrResponse;
import com.interzonedev.requestr.service.RequestrService;
import com.interzonedev.requestr.web.RequestrController;

@Controller
@RequestMapping(value = "/send")
public class SendController extends RequestrController {

	private static final String JSON_FORM_VIEW = "send/jsonForm";
	private static final String COMPONENTS_FORM_VIEW = "send/componentsForm";
	private static final String RESPONSE_VIEW = "send/response";

	@Inject
	@Named("requestrService")
	private RequestrService requestrService;

	private Map<String, String> requestMethods;

	@PostConstruct
	public void init() {
		Map<String, String> requestMethodsMutable = new LinkedHashMap<String, String>();

		requestMethodsMutable.put(RequestrMethod.GET.toString(), RequestrMethod.GET.toString());
		requestMethodsMutable.put(RequestrMethod.POST.toString(), RequestrMethod.POST.toString());
		requestMethodsMutable.put(RequestrMethod.PUT.toString(), RequestrMethod.PUT.toString());
		requestMethodsMutable.put(RequestrMethod.DELETE.toString(), RequestrMethod.DELETE.toString());
		requestMethodsMutable.put(RequestrMethod.OPTIONS.toString(), RequestrMethod.OPTIONS.toString());
		requestMethodsMutable.put(RequestrMethod.HEAD.toString(), RequestrMethod.HEAD.toString());
		requestMethodsMutable.put(RequestrMethod.TRACE.toString(), RequestrMethod.TRACE.toString());

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

		RequestrRequest requestrRequest = null;
		try {
			requestrRequest = jsonForm.toRequest();
		} catch (InvalidJsonException ije) {
			log.debug("sendJsonRequest: JSON has errors - " + ije.getMessage());
			result.rejectValue("input", "InvalidJson.jsonForm.input", new Object[] { ije.getMessage() }, null);
			return JSON_FORM_VIEW;
		}

		RequestrResponse requestrResponse = null;
		try {
			requestrResponse = requestrService.doRequest(requestrRequest);
		} catch (Throwable t) {
			log.error("sendJsonRequest: Error making HTTP request", t);
			String errorMessage = t.getMessage();
			String stackTrace = ExceptionUtils.getStackTrace(t);
			result.reject("error.request", new Object[] { errorMessage, stackTrace }, null);
			return JSON_FORM_VIEW;
		}

		model.addAttribute("response", requestrResponse);

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

		@SuppressWarnings("unused")
		String url = componentsForm.getUrl().trim();
		@SuppressWarnings("unused")
		String method = componentsForm.getMethod().trim();

		model.addAttribute("response", "Foo");

		log.debug("sendComponentsRequest: End");

		return RESPONSE_VIEW;
	}

}
