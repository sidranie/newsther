package fr.sidranie.newsther.mappers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.dtos.person.CreatePersonDto;
import fr.sidranie.newsther.dtos.person.FullPersonDto;
import fr.sidranie.newsther.dtos.person.ShortPersonDto;
import fr.sidranie.newsther.dtos.subscription.ShortNewsletterSubscriptionDto;
import fr.sidranie.newsther.entities.Person;

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
