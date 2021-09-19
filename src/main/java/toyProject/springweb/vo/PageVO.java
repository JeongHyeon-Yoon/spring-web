package toyProject.springweb.vo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageVO {
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_MAX_SIZE = 50;

    private int page;
    private int size;

    private String keyword;
    private String type;

    public PageVO(){
        this.page = 1;
        this.size = DEFAULT_SIZE;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page){
        if( page < 0 ){
            this.page = 0;
        }else{
            this.page = page;
        }
    }

    public int getSize(){
        return size;
    }

    public void setSize(int size) {
        if (size < DEFAULT_SIZE || size > DEFAULT_MAX_SIZE) {
            this.size = DEFAULT_SIZE;
        } else {
            this.size = size;
        }
    }

    public Pageable makePageable(int direction, String... props){
        Sort.Direction sortDir;
        if(direction == 0){
            sortDir = Sort.Direction.DESC;
        } else{
            sortDir = Sort.Direction.ASC;
        }
        return PageRequest.of(this.page - 1, this.size, sortDir, props);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
