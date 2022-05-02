package cm.pak.canon.controllers;

import cm.pak.canon.beans.UsersBean;
import cm.pak.canon.facades.UserFacade;
import cm.pak.canon.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/users")
public class UserCtr {

    private static final Logger LOG = LoggerFactory.getLogger(UserCtr.class);

    @Autowired
    private UserFacade userFacade;

    @GetMapping
    public String users(final Model model) {
        final UsersBean user = new UsersBean();
        model.addAttribute("criteria", user);
        model.addAttribute("datas", userFacade.getUsers(0, -1));
        return "users";
    }

    @PostMapping
    public String users(final Model model, @ModelAttribute UsersBean criteria) throws IOException {
        model.addAttribute("criteria", criteria);
        userFacade.importUsers(criteria);
        model.addAttribute("datas", userFacade.getUsers(0, -1));
        return "users";
    }
}
