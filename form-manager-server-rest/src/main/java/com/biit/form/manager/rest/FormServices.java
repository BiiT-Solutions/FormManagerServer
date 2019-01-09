package com.biit.form.manager.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.biit.rest.client.RestGenericClient;
import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.UnprocessableEntityException;

import io.swagger.annotations.ApiOperation;

@RestController
public class FormServices {


	@ApiOperation(value = "Basic method to get the answers of a form giving an UUID.", notes = "")
	@RequestMapping(value = "/forms/{formId}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String getForm(@PathVariable("formId") String formId) throws UnprocessableEntityException, EmptyResultException {

		// TODO Move to a config file
		String target = "https://testing.biit-solutions.com/formrunner";
		String path = "/forms/" + formId;
		String messageType = "application/json";
		return RestGenericClient.get(false, target, path, messageType, false, null);

	}
}
