package fr.sidranie.newsther.newsletters;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fr.sidranie.newsther.newsletters.dtos.CreateNewsletterDto;
import fr.sidranie.newsther.newsletters.dtos.FullNewsletterDto;
import fr.sidranie.newsther.newsletters.dtos.ShortNewsletterDto;

@RestController
@RequestMapping("/api/newsletters")
public class NewsletterController {

    private final NewsletterService service;
    private final NewsletterRepository repository;

    public NewsletterController(NewsletterService service, NewsletterRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<Set<ShortNewsletterDto>> findAll() {
        return ResponseEntity.ok(repository.findAll().stream()
                .map(NewsletterMapper::newsletterToShortNewsletterDto)
                .collect(Collectors.toSet()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<FullNewsletterDto> findById(@PathVariable("slug") String slug) {
        return repository.findBySlug(slug)
                .map(NewsletterMapper::newsletterToFullNewsletterDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ShortNewsletterDto> createNewsletter(@RequestBody CreateNewsletterDto createNewsletterDto) {
        Newsletter newsletter = NewsletterMapper.createNewsletterDtoToNewsletter(createNewsletterDto);
        service.createNewsletter(newsletter, null); // TODO Get principal
        return ResponseEntity.created(URI.create("/newsletters/" + newsletter.getId()))
                .body(NewsletterMapper.newsletterToShortNewsletterDto(newsletter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsletter(@PathVariable("id") Long id) {
        service.deleteNewsletter(id);
        return ResponseEntity.noContent().build();
    }
}
