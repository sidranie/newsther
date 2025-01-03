package fr.sidranie.newsther.controllers.renderers;

import fr.sidranie.newsther.dtos.subscription.IdsIdentifiedSubscriptionDto;
import fr.sidranie.newsther.services.SubscriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SubscriptionRenderer {

    private SubscriptionService service;

    public SubscriptionRenderer(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping("/subscribe")
    public String subscribe(IdsIdentifiedSubscriptionDto idsIdentifiedSubscriptionDto) {
        service.subscribePersonToNewsletter(idsIdentifiedSubscriptionDto.getPersonId(),
                idsIdentifiedSubscriptionDto.getNewsletterId());
        return "redirect:/people/" + idsIdentifiedSubscriptionDto.getPersonId();
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(IdsIdentifiedSubscriptionDto idsIdentifiedSubscriptionDto) {
        service.unsubscribePersonFromNewsletter(idsIdentifiedSubscriptionDto.getPersonId(),
                idsIdentifiedSubscriptionDto.getNewsletterId());
        return "redirect:/people/" + idsIdentifiedSubscriptionDto.getPersonId();
    }
}
