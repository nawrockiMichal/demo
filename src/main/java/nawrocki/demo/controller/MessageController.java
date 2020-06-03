package nawrocki.demo.controller;

import nawrocki.demo.model.ContactType;
import nawrocki.demo.model.Message;
import nawrocki.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Controller
public class MessageController implements WebMvcConfigurer {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "/")
    public String redirect() {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/thank-you.html")
    public String thankYou() {
        return "thank-you";
    }

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public ModelAndView load() {
        ModelAndView model = new ModelAndView("index");
        model.addObject("message", new Message());
        return model;
    }

    @RequestMapping(value = "/index.html", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("message") Message message,
                       final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        messageService.create(message);
        return "redirect:/thank-you.html";

    }

    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public ModelAndView list(Pageable pageable, ContactType type) {
        ModelAndView model = new ModelAndView("list");
        model.addObject("selectedType", type);
        model.addObject("selectedSortOrder", pageable.getSort().getOrderFor("date") != null
                ? pageable.getSort().getOrderFor("date").getDirection().name() : null);

        model.addObject("messages", messageService.findAll(pageable, type));
        return model;

    }

}
