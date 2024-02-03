package dev.shept.snippets.backend.api;

import dev.shept.snippets.backend.data.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class RedirectController {

    private final RedirectService redirectService;

    @Autowired
    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @GetMapping("/{uuid}")
    public RedirectView redirect(@PathVariable String uuid) {
        return redirectService.redirect(uuid);
    }

}
