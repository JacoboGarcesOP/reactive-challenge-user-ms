package co.com.bancolombia.api;

import co.com.bancolombia.api.request.CreateUserRequest;
import co.com.bancolombia.api.request.EnrollUserInBootcampRequest;
import co.com.bancolombia.api.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
  private final String BASE_URL = "/v1/api";

  @Bean
  @RouterOperation(
    path = "/v1/api/users",
    method = org.springframework.web.bind.annotation.RequestMethod.POST,
    operation = @Operation(
      operationId = "createUser",
      summary = "Crear nuevo usuario",
      description = "Endpoint para registrar un nuevo usuario.",
      tags = {"User Management"},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del usuario a crear.",
        required = true,
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = CreateUserRequest.class),
          examples = @ExampleObject(
            name = "Ejemplo de usuario",
            summary = "Ejemplo de request para crear un usuario",
            value = "{\n  \"name\": \"Alice\",\n  \"email\": \"alice@example.com\"\n}"
          )
        )
      ),
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Usuario creado exitosamente",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(
              description = "Respuesta exitosa al crear un nuevo usuario"
            ),
            examples = @ExampleObject(
              name = "Success Response",
              summary = "Usuario creado correctamente",
              value = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Alice\",\n" +
                "  \"email\": \"alice@example.com\",\n" +
                "  \"bootcampIds\": []\n" +
                "}"
            )
          )
        ),
        @ApiResponse(
          responseCode = "400",
          description = "Error de validación, dominio o negocio",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
          )
        )
      }
    )
  )
  public RouterFunction<ServerResponse> createUserRouter(Handler handler) {
    return route(POST(BASE_URL + "/users"), handler::createUser);
  }

  @Bean
  @RouterOperation(
    path = "/v1/api/users/enroll",
    method = org.springframework.web.bind.annotation.RequestMethod.POST,
    operation = @Operation(
      operationId = "enrollUserInBootcamp",
      summary = "Inscribir usuario en bootcamp",
      description = "Endpoint para inscribir un usuario existente en un bootcamp específico. " +
                   "Valida que el usuario y bootcamp existan, que el usuario no esté ya inscrito " +
                   "y que no haya alcanzado el límite máximo de 5 bootcamps.",
      tags = {"User Management"},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos para inscribir un usuario en un bootcamp.",
        required = true,
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = EnrollUserInBootcampRequest.class),
          examples = @ExampleObject(
            name = "Ejemplo de inscripción",
            summary = "Ejemplo de request para inscribir usuario en bootcamp",
            value = "{\n  \"userId\": 1,\n  \"bootcampId\": 5\n}"
          )
        )
      ),
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Usuario inscrito exitosamente en el bootcamp",
          content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
              name = "Success Response",
              summary = "Inscripción exitosa",
              value = "{\n" +
                "  \"user\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Alice\",\n" +
                "    \"email\": \"alice@example.com\"\n" +
                "  },\n" +
                "  \"bootcamp\": {\n" +
                "    \"id\": 5,\n" +
                "    \"name\": \"Java Bootcamp\",\n" +
                "    \"description\": \"Aprende Java desde cero\",\n" +
                "    \"launchDate\": \"2024-01-15\",\n" +
                "    \"duration\": 12\n" +
                "  }\n" +
                "}"
            )
          )
        ),
        @ApiResponse(
          responseCode = "400",
          description = "Error de validación, dominio o negocio",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
              @ExampleObject(
                name = "Validation Error - User ID Required",
                summary = "User ID es requerido",
                value = "{\n  \"error\": \"VALIDATION_ERROR\",\n  \"message\": \"User ID is required\"\n}"
              ),
              @ExampleObject(
                name = "Validation Error - Bootcamp ID Required",
                summary = "Bootcamp ID es requerido",
                value = "{\n  \"error\": \"VALIDATION_ERROR\",\n  \"message\": \"Bootcamp ID is required\"\n}"
              ),
              @ExampleObject(
                name = "Validation Error - User ID Must Be Positive",
                summary = "User ID debe ser positivo",
                value = "{\n  \"error\": \"VALIDATION_ERROR\",\n  \"message\": \"User ID must be a positive number\"\n}"
              ),
              @ExampleObject(
                name = "Validation Error - Bootcamp ID Must Be Positive",
                summary = "Bootcamp ID debe ser positivo",
                value = "{\n  \"error\": \"VALIDATION_ERROR\",\n  \"message\": \"Bootcamp ID must be a positive number\"\n}"
              ),
              @ExampleObject(
                name = "Business Error - User Not Found",
                summary = "Usuario no encontrado",
                value = "{\n  \"error\": \"BUSINESS_ERROR\",\n  \"message\": \"User with id 999 not found\"\n}"
              ),
              @ExampleObject(
                name = "Business Error - Bootcamp Not Found",
                summary = "Bootcamp no encontrado",
                value = "{\n  \"error\": \"BUSINESS_ERROR\",\n  \"message\": \"Bootcamp with id 999 not found\"\n}"
              ),
              @ExampleObject(
                name = "Business Error - Already Enrolled",
                summary = "Usuario ya inscrito en el bootcamp",
                value = "{\n  \"error\": \"BUSINESS_ERROR\",\n  \"message\": \"User is already enrolled in bootcamp with id 5\"\n}"
              ),
              @ExampleObject(
                name = "Business Error - Max Bootcamps Reached",
                summary = "Límite máximo de bootcamps alcanzado",
                value = "{\n  \"error\": \"BUSINESS_ERROR\",\n  \"message\": \"User has reached the maximum limit of 5 bootcamps\"\n}"
              ),
              @ExampleObject(
                name = "Business Error - Schedule Conflict",
                summary = "Conflicto de horarios con otro bootcamp",
                value = "{\n  \"error\": \"BUSINESS_ERROR\",\n  \"message\": \"User cannot be enrolled in bootcamp with id 5 because it conflicts with another bootcamp schedule\"\n}"
              )
            }
          )
        ),
        @ApiResponse(
          responseCode = "422",
          description = "Error de dominio",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(
              name = "Domain Error",
              summary = "Error de dominio",
              value = "{\n  \"error\": \"DOMAIN_ERROR\",\n  \"message\": \"Invalid domain constraint\"\n}"
            )
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(
              name = "Internal Server Error",
              summary = "Error interno del servidor",
              value = "{\n  \"error\": \"INTERNAL_ERROR\",\n  \"message\": \"An unexpected error occurred\"\n}"
            )
          )
        )
      }
    )
  )
  public RouterFunction<ServerResponse> enrollUserInBootcampRouter(Handler handler) {
    return route(POST(BASE_URL + "/users/enroll"), handler::enrollUserInBootcamp);
  }
}
