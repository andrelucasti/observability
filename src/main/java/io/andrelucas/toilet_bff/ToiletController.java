package io.andrelucas.toilet_bff;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/toilet")
public class ToiletController {



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

}
