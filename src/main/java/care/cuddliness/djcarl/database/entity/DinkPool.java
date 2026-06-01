package care.cuddliness.djcarl.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

@Entity
@Table(
        name = "dink_pool",
        uniqueConstraints = @UniqueConstraint(columnNames = "discord_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DinkPool extends BaseEntity {

    @Column(name = "discord_id", nullable = false, unique = true)
    private Long discordId;

    @Builder.Default
    @Column(name = "timeout", nullable = false, unique = true)
    private int timeout = 0;
}
