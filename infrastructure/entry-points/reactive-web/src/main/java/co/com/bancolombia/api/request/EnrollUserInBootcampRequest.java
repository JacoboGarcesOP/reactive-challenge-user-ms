package co.com.bancolombia.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollUserInBootcampRequest {
  
  @NotNull(message = "User ID is required")
  @Positive(message = "User ID must be a positive number")
  private Long userId;
  
  @NotNull(message = "Bootcamp ID is required")
  @Positive(message = "Bootcamp ID must be a positive number")
  private Long bootcampId;
}
