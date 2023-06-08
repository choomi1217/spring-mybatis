package cho.me.spring.board.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Board {
    Integer id;
    String title;
    String contents;
    String writer;
    List<Comment> comments;
}
