package care.cuddliness.djcarl.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(
        name = "discord_users",
        uniqueConstraints = @UniqueConstraint(columnNames = "discord_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarlUser extends BaseEntity {
    /** Discord snowflake ID — unique, never changes, not null. */
    @Column(name = "discord_id", nullable = false, unique = true)
    private String discordId;

    @Builder.Default
    @Column(name = "dinks")
    private Integer dinks = 0;


}
