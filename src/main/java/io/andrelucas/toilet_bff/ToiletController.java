package io.andrelucas.toilet_bff;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toilet")
public class ToiletController {

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

    return ResponseEntity.status(201).build();
  }

  @GetMapping
  public ResponseEntity<Iterable<Toilet>> getToilets() {
    final var toilets = toiletRepository.findAll();
    return ResponseEntity.ok(toilets);
  }

}
