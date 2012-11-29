package com.interzonedev.requestr.web.send;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interzonedev.requestr.service.InvalidJsonException;
import com.interzonedev.requestr.service.RequestrMethod;
import com.interzonedev.requestr.service.RequestrRequest;
import com.interzonedev.requestr.service.RequestrResponse;
import com.interzonedev.requestr.service.RequestrService;
import com.interzonedev.requestr.web.RequestrController;

@Controller
@RequestMapping(value = "/send")
public class SendController extends RequestrController {

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

		return "send/jsonForm";
	}

	@RequestMapping(method = RequestMethod.POST, value = "json")
	public String sendJsonRequest(Model model, @Valid JsonForm jsonForm, BindingResult result) {
		log.debug("sendJsonRequest: Start");

		if (result.hasErrors()) {
			log.debug("sendJsonRequest: Form has errors");
			return "send/jsonForm";
		}

		String input = jsonForm.getInput().trim();

		RequestrRequest requestrRequest = null;
		try {
			requestrRequest = jsonToRequest(input);
		} catch (InvalidJsonException ije) {
			log.debug("sendJsonRequest: JSON has errors - " + ije.getMessage());
			result.rejectValue("input", "InvalidJson.jsonForm.input", new Object[] { ije.getMessage() }, null);
			return "send/jsonForm";
		}

		RequestrResponse requestrResponse = null;
		try {
			requestrResponse = requestrService.doRequest(requestrRequest);
		} catch (Throwable t) {
			log.error("sendJsonRequest: Error making HTTP request", t);
			String errorMessage = t.getMessage();
			String stackTrace = ExceptionUtils.getStackTrace(t);
			result.reject("error.request", new Object[] { errorMessage, stackTrace }, null);

		}

		model.addAttribute("response", requestrResponse);

		log.debug("sendJsonRequest: End");

		return "send/response";
	}

	@RequestMapping(method = RequestMethod.GET, value = "components")
	public String displayComponentsForm(Model model) {
		log.debug("displayComponentsForm: Start");

		model.addAttribute("componentsForm", new ComponentsForm());

		log.debug("displayComponentsForm: End");

		return "send/componentsForm";
	}

	@RequestMapping(method = RequestMethod.POST, value = "components")
	public String sendComponentsRequest(Model model, @Valid ComponentsForm componentsForm, BindingResult result) {
		log.debug("sendComponentsRequest: Start");

		if (result.hasErrors()) {
			log.debug("sendComponentsRequest: Form has errors");
			return "send/componentsForm";
		}

		@SuppressWarnings("unused")
		String url = componentsForm.getUrl().trim();
		@SuppressWarnings("unused")
		String method = componentsForm.getMethod().trim();

		model.addAttribute("response", "Foo");

		log.debug("sendComponentsRequest: End");

		return "send/response";
	}

	private RequestrRequest jsonToRequest(String json) throws InvalidJsonException {

		Map<String, Object> requestValues = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(Feature.ALLOW_COMMENTS, true);
			requestValues = (objectMapper).readValue(json, new TypeReference<Map<String, Object>>() {
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

		RequestrMethod requestMethod = null;
		try {
			requestMethod = RequestrMethod.valueOf(method.toUpperCase());
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

		return new RequestrRequest(url, requestMethod, headers, parameters);
	}

}
