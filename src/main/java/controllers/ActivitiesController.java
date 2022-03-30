package controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import demo.Message;

@RestController
public class ActivitiesController {
	@RequestMapping(
			path = "/hello",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
		public Message hello () {
			return new Message("Hello World!");
		}

}
