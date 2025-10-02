package co.com.bancolombia.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_schema.user")
public class UserEntity {

  @Id
  @Column("user_id")
  private Long userId;

  @Column("name")
  private String name;

  @Column("email")
  private String email;
}
