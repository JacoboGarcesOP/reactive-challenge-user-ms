package co.com.bancolombia.model.user;

import co.com.bancolombia.model.bootcamp.Capacity;
import co.com.bancolombia.model.bootcamp.Technology;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BootcampTest {

  @Test
  void shouldHoldCapacitiesAndTechnologies() {
    Bootcamp bootcamp = new Bootcamp(5L, "Java", "Desc", LocalDate.now().plusDays(3), 12);
    Capacity capacity = new Capacity(1L, "Backend", "Servicios",
        List.of(new Technology(10L, "Spring", "Spring FW")));
    bootcamp.setCapacities(List.of(capacity));

    assertEquals(1, bootcamp.getCapacities().size());
    assertEquals("Backend", bootcamp.getCapacities().get(0).getName().getValue());
    assertEquals(1, bootcamp.getCapacities().get(0).getTechnologies().size());
    assertEquals("Spring", bootcamp.getCapacities().get(0).getTechnologies().get(0).getName().getValue());
  }
}


