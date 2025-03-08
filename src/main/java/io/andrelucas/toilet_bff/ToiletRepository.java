package io.andrelucas.toilet_bff;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ToiletRepository extends CrudRepository<Toilet, UUID> {

}
