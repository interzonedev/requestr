package com.interzonedev.requestr.web.send;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.interzonedev.requestr.web.RequestrController;

@Controller
@RequestMapping(value = "/send")
public class SendController extends RequestrController {

	@RequestMapping(method = RequestMethod.GET, value = "json")
	public String displayJsonForm(Model model) {
		log.debug("displayJsonForm");

		model.addAttribute("jsonForm", new JsonForm());

		return "send/jsonForm";
	}

	@RequestMapping(method = RequestMethod.POST, value = "json")
	public String sendJsonRequest(Model model, @Valid JsonForm jsonForm, BindingResult result) {
		log.debug("sendJsonRequest: Start");

		if (result.hasErrors()) {
			log.debug("Form has errors");
			return "send/jsonForm";
		}

		@SuppressWarnings("unused")
		String input = jsonForm.getInput().trim();

		model.addAttribute("response", "Foo");

		log.debug("sendJsonRequest: End");

		return "send/response";
	}

	@RequestMapping(method = RequestMethod.GET, value = "components")
	public String displayComponentsForm(Model model) {
		log.debug("displayComponentsForm");

		model.addAttribute("componentsForm", new ComponentsForm());

		return "send/componentsForm";
	}

	@RequestMapping(method = RequestMethod.POST, value = "components")
	public String sendComponentsRequest(Model model, @Valid ComponentsForm componentsForm, BindingResult result) {
		log.debug("sendJsonRequest: Start");

		if (result.hasErrors()) {
			log.debug("Form has errors");
			return "send/componentsForm";
		}

		@SuppressWarnings("unused")
		String url = componentsForm.getUrl().trim();
		@SuppressWarnings("unused")
		String method = componentsForm.getMethod().trim();

		model.addAttribute("response", "Foo");

		log.debug("sendJsonRequest: End");

		return "send/response";
	}
}
