package com.twitterassistant.web.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/error")
public class ErrorController {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final transient Logger LOG = LoggerFactory.getLogger(ErrorController.class);

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	@Value("#{twitProperties['project.environment']}")
	String environment;

	// Constructors ---------------------------------------------------------------------------------------- Constructors

	// Public Methods ------------------------------------------------------------------------------------ Public Methods

	// @ Fatal error
	@RequestMapping(method = RequestMethod.GET, value = "/fatal")
	public String fatal(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
		if (servletName == null) {
			servletName = "Unknown";
		}
		String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null) {
			requestUri = "Unknown";
		}

		String errorDetails = "<br>Status code: " + statusCode + "<br>Servlet: " + servletName + "<br>Request: " + requestUri + "<br><br>Stacktrace:<br>";
		if (throwable != null) {
			errorDetails += (throwable.getMessage() != null ? throwable.getMessage() : "No cause") + "<br>";
			for (StackTraceElement e : throwable.getStackTrace())
				errorDetails += e.toString() + "<br>";
		}
		model.put("message", "fatal");

		// in live environment lets send a message
		if (environment.equals("prod")) {
			try {
				// lets disable it
				// sendMail.message("Homeadnet an error occurred!", errorDetails);
			} catch (Exception e) {
			}
		} else
			// for stage and dev output on the page
			model.put("errorDetails", errorDetails);

		LOG.error("Fatal error occurred! Error Details: " + errorDetails, throwable);

		return "/error/error";
	}

	// @ 404 Error
	@RequestMapping(method = RequestMethod.GET, value = "/404")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String message(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.put("message", "404");
		return "/error/404";
	}

	// @ Messaged Error
	@RequestMapping(method = RequestMethod.GET, value = "/{message}")
	public String message(HttpServletRequest request, HttpServletResponse response, @PathVariable("message") String message) {
		return "/error/error";
	}

	// ERROR DIALOG
	@RequestMapping(method = RequestMethod.POST, value = "/dialog/error")
	public String dialogError(HttpServletRequest request, HttpServletResponse response) {
		return "/dialog/error";
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

} // end of class