package co.com.bancolombia.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BootcampMessage {
  private Long bootcampId;
  private Integer peopleEnrolledCount;
}
