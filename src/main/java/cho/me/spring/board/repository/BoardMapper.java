package cho.me.spring.board.repository;

import cho.me.spring.board.dto.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<Board> getBoard();
}
