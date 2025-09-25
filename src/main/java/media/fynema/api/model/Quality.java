package media.fynema.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Quality {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
