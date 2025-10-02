package co.com.bancolombia.model.user.gateway;

public interface PublisherGateway {
  void publish(Long bootcampId, Integer peopleEnrolledCount);
}
