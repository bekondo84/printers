package cm.pak.canon.controllers;

import cm.pak.canon.services.TaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/histories")
public class TaskHistoryCtr {

    @Autowired
    private TaskHistoryService historyService;

    @GetMapping
    String getHistories(final Model model) {
        model.addAttribute("histories", historyService.getHistory());
        return "/jobTasksLog";
    }
}
