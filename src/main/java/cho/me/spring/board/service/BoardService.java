package cho.me.spring.board.service;

import cho.me.spring.board.dto.Board;
import cho.me.spring.board.repository.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    public List<Board> getBoard() {
        return boardMapper.getBoard();
    }
}
