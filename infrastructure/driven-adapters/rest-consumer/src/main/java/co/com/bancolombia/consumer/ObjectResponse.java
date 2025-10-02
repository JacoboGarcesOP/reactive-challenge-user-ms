package co.com.bancolombia.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectResponse {
  private Long bootcampId;
  private String name;
  private String description;
  private LocalDate launchDate;
  private Integer duration;
  private List<CapacityResponse> capacities;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class CapacityResponse {
    private Long capacityId;
    private String name;
    private String description;
    private List<TechnologyResponse> technologies;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder(toBuilder = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TechnologyResponse {
    private Long technologyId;
    private String name;
    private String description;
  }
}