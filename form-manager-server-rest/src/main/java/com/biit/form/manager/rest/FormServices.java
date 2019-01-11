package com.biit.form.manager.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.biit.form.configuration.FormManagerConfigurationReader;
import com.biit.form.manager.logger.FormManagerLogger;
import com.biit.rest.client.RestGenericClient;
import com.biit.rest.exceptions.EmptyResultException;
import com.biit.rest.exceptions.UnprocessableEntityException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class FormServices {

	@ApiOperation(value = "Basic method to get the answers of a form giving an UUID.", notes = "")
	@RequestMapping(value = "/forms/{formId}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String getForm(@PathVariable("formId") String formId)
			throws UnprocessableEntityException, EmptyResultException {

		String target = FormManagerConfigurationReader.getInstance().getMachineDomain();
		target += "/formrunner";
		String path = "/forms/" + formId;
		String messageType = "application/json";
		return RestGenericClient.get(false, target, path, messageType, false, null);

	}

	@ApiOperation(value = "Basic method to get save a form result from the formrunner.", notes = "")
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/forms", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String postForm(
			@ApiParam(value = "Form result", required = true) @RequestBody(required = true) String formResult) {
		FormManagerLogger.info(this.getClass().getName(), "Post form");
		return formResult;
	}
	
	@PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            // redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            FormManagerLogger.info(this.getClass().getName(), "File "+ file.getOriginalFilename());
            // FormManagerLogger.info(this.getClass().getName(), "Files recieved"+ bytes);
            // Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            // Files.write(path, bytes);

           // redirectAttributes.addFlashAttribute("message",
             //       "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
        	FormManagerLogger.errorMessage(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }
}
