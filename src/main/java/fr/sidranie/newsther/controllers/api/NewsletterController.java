package fr.sidranie.newsther.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import fr.sidranie.newsther.dtos.newsletter.CreateNewsletterDto;
import fr.sidranie.newsther.dtos.newsletter.FullNewsletterDto;
import fr.sidranie.newsther.dtos.newsletter.ShortNewsletterDto;
import fr.sidranie.newsther.entities.Newsletter;
import fr.sidranie.newsther.mappers.NewsletterMapper;
import fr.sidranie.newsther.services.NewsletterService;

@RestController
@RequestMapping("/api/newsletters")
public class NewsletterController {

    private final NewsletterService service;

    public NewsletterController(NewsletterService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Set<ShortNewsletterDto>> findAll() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(NewsletterMapper::newsletterToShortNewsletterDto)
                .collect(Collectors.toSet()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullNewsletterDto> findById(@PathVariable("id") Long id) {
        return service.findById(id)
                .map(NewsletterMapper::newsletterToFullNewsletterDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShortNewsletterDto> createNewsletter(@RequestBody CreateNewsletterDto createNewsletterDto) {
        Newsletter newsletter = NewsletterMapper.createNewsletterDtoToNewsletter(createNewsletterDto);
        service.createNewsletter(newsletter);
        return ResponseEntity.created(URI.create("/newsletters/" + newsletter.getId()))
                .body(NewsletterMapper.newsletterToShortNewsletterDto(newsletter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsletter(@PathVariable("id") Long id) {
        service.deleteNewsletter(id);
        return ResponseEntity.noContent().build();
    }
}
