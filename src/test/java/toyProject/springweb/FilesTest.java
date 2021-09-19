package toyProject.springweb;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import toyProject.springweb.domain.Board;
import toyProject.springweb.domain.UploadFile;
import toyProject.springweb.file.FileStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class FilesTest {

    /**
     *  createStoreFileName()
     *  파일명을 UUID로 뱐걍한다.
     *
     */
    @Test
    public void createStoreFileName() {

        //UUID 생성
        String uuid = UUID.randomUUID().toString();

        //test파일명 생성
        UploadFile uploadFile = new UploadFile();
        uploadFile.setOriginFileName("testFile.txt");

        //파일 확장자 추출
        int pos = uploadFile.getOriginFileName().lastIndexOf(".");
        String ext = uploadFile.getOriginFileName().substring(pos + 1);

        String storeFileNm =  uuid + "." + ext;

        Assertions.assertThat(storeFileNm).isEqualTo(uuid+".txt");

    }

    /**
     *  extractExt()
     *  파일 확장자를 추출한다.
     *
     */
    @Test
    public void extractExt() {
        UploadFile uploadFile = new UploadFile();
        uploadFile.setOriginFileName("testFile.txt");

        int pos = uploadFile.getOriginFileName().lastIndexOf(".");
        String fileExt = uploadFile.getOriginFileName().substring(pos + 1);

        Assertions.assertThat(fileExt).isEqualTo("txt");
    }
}
