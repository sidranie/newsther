package fr.sidranie.newsther.people;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.sidranie.newsther.newsletters.NewsletterMapper;
import fr.sidranie.newsther.newsletters.dtos.ShortNewsletterDto;
import fr.sidranie.newsther.people.dtos.CreatePersonDto;
import fr.sidranie.newsther.people.dtos.FullPersonDto;
import fr.sidranie.newsther.people.dtos.ShortPersonDto;
import fr.sidranie.newsther.subscriptions.SubscriptionMapper;
import fr.sidranie.newsther.subscriptions.dtos.ShortNewsletterSubscriptionDto;

public class PersonMapper {

    public static Person createPersonDtoToPerson(CreatePersonDto createPersonDto) {
        Person person = new Person();
        person.setUsername(createPersonDto.getUsername());
        person.setEmail(createPersonDto.getEmail());
        person.setPassword(createPersonDto.getPassword());
        person.setGivenName(createPersonDto.getGivenName());
        person.setFamilyName(createPersonDto.getFamilyName());
        person.setSubscriptions(Collections.emptySet());
        return person;
    }

    public static ShortPersonDto personToShortPersonDto(Person person) {
        ShortPersonDto shortPersonDto = new ShortPersonDto();
        shortPersonDto.setId(person.getId());
        shortPersonDto.setUsername(person.getUsername());
        shortPersonDto.setEmail(person.getEmail());
        shortPersonDto.setPassword(person.getPassword());
        shortPersonDto.setGivenName(person.getGivenName());
        shortPersonDto.setFamilyName(person.getFamilyName());
        return shortPersonDto;
    }

    public static FullPersonDto personToFullPersonDto(Person person) {
        FullPersonDto fullPersonDto = new FullPersonDto();
        fullPersonDto.setId(person.getId());
        fullPersonDto.setUsername(person.getUsername());
        fullPersonDto.setEmail(person.getEmail());
        fullPersonDto.setPassword(person.getPassword());
        fullPersonDto.setGivenName(person.getGivenName());
        fullPersonDto.setFamilyName(person.getFamilyName());

        List<ShortNewsletterDto> newsletters = person.getNewsletters()
                .stream()
                .map(NewsletterMapper::newsletterToShortNewsletterDto)
                .sorted(Comparator.comparing(ShortNewsletterDto::getTitle))
                .toList();
        fullPersonDto.setNewsletters(newsletters);

        List<ShortNewsletterSubscriptionDto> subscriptions = person.getSubscriptions()
                .stream()
                .map(SubscriptionMapper::subscriptionToShortNewsletterSubscriptionDto)
                .sorted(Comparator.comparing(ShortNewsletterSubscriptionDto::getSince).reversed())
                .toList();
        fullPersonDto.setSubscriptions(subscriptions);

        return fullPersonDto;
    }
}
