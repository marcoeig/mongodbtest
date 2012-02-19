package at.eiglets.mongodbtest.web;

import java.beans.PropertyEditorSupport;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.eiglets.mongodbtest.domain.Person;
import at.eiglets.mongodbtest.service.PersonService;

@Controller
@RequestMapping("/persons")
public class PersonController {

	private final PersonService personService;

	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Person.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String id) throws IllegalArgumentException {
				super.setValue(personService.findById(id));
			}

		});
	}

	@Inject
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@RequestMapping
	public void persons(final Model model) {
		model.addAttribute("persons", personService.findAll());
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String create(@ModelAttribute final Person person) {
		person.setId(null);
		personService.save(person);
		return "redirect:/persons/" + person.getId();
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@ModelAttribute final Person person) {
		return "edit";
	}

	@RequestMapping(value = "/{person}", method = RequestMethod.GET)
	public String get(@PathVariable final Person person) {
		return "edit";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String update(@PathVariable("id") final String id,
			final Person person) {
		personService.save(person);
		return "edit";
	}

	@RequestMapping(value = "/{person}", method = RequestMethod.DELETE)
	public String delete(@PathVariable final Person person) {
		personService.remove(person);
		return "redirect:/persons";
	}
}
