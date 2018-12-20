package com.biit.form.manager.rest;

import io.swagger.annotations.ApiOperation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest services for check that is on-line.
 */
@RestController
public class HealthCheck {

	@ApiOperation(value = "Basic method to check if the server is online.")
	@RequestMapping(value = "/healthCheck", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void healthCheck() {

	}

}