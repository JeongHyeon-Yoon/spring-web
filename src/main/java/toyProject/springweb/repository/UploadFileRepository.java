package toyProject.springweb.repository;

import org.springframework.data.repository.CrudRepository;
import toyProject.springweb.domain.UploadFile;

public interface UploadFileRepository extends CrudRepository<UploadFile, Long> {
}
