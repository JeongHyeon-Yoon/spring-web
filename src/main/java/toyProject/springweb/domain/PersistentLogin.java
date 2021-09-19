package toyProject.springweb.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Entity
@Table(name = "persistent_logins")
@EqualsAndHashCode(of = "series")
public class PersistentLogin {

    @Id
    private String series;

    @Column(nullable = false)
    private String username;


    @Column(nullable = false)
    private String token;

    @UpdateTimestamp
    private Timestamp last_used;

}
