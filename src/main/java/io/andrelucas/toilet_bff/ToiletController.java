package io.andrelucas.toilet_bff;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/toilet")
public class ToiletController {

  private static final Logger log = LoggerFactory.getLogger(ToiletController.class);
  private final ToiletRepository toiletRepository;

  public ToiletController(ToiletRepository toiletRepository) {
    this.toiletRepository = toiletRepository;
  }


  @GetMapping("/slow")
  public ResponseEntity<String> slow() {
    try {
     final var random = (int) (Math.random() * 5000);
      Thread.sleep(random);
      final var result = String.format("slow - %d", random);
      return ResponseEntity.ok(result);

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/fast")
  public ResponseEntity<String> fast() {
    return ResponseEntity.ok("fast");
  }

  @PostMapping
  public ResponseEntity<Void> createToilet(@RequestBody ToiletRequest toiletRequest) {
    final var toilet = new Toilet();
    toilet.setId(UUID.randomUUID());
    toilet.setName(toiletRequest.name());
    toilet.setLatitude(toiletRequest.latitude());
    toilet.setLongitude(toiletRequest.longitude());

    toiletRepository.save(toilet);

    log.info("Toilet created: {}", toilet);

    return ResponseEntity.status(201).build();
  }

  @GetMapping
  public ResponseEntity<Iterable<Toilet>> getToilets() throws InterruptedException {
    final var random = (int) (Math.random() * 5000);
    final var result = String.format("slow - %d", random);

    log.info("Toilets found: {}", result);
    final var toilets = toiletRepository.findAll();
    Thread.sleep(random);

    return ResponseEntity.ok(toilets);
  }

  @GetMapping("/error")
  public ResponseEntity<String> getErrors() {
    throw new RuntimeException("error");
  }

  @GetMapping("/exception")
  public ResponseEntity<String> getExceptions() {
    throw new IllegalArgumentException("exception");
  }

  @GetMapping("/badrequest")
  public ResponseEntity<String> getBadRequest() {
   return ResponseEntity.badRequest().build();
  }

  @GetMapping("/unauthorized")
  public ResponseEntity<String> getUnauthorized() {
    return ResponseEntity.status(401).build();
  }

}
