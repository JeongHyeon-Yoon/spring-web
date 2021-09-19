package toyProject.springweb.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_file")
@EqualsAndHashCode(of = "fileSeq")
@NoArgsConstructor
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileSeq;
    private String originFileName;
    private String storeFileName;

}
