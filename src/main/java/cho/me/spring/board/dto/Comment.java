package cho.me.spring.board.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Comment {
    Integer id;
    Integer boardId;
    String contents;
    String writer;

}
