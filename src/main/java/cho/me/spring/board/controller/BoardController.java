package cho.me.spring.board.controller;

import cho.me.spring.board.dto.Board;
import cho.me.spring.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public void getBoard() {
        List<Board> board = boardService.getBoard();
        board.forEach(System.out::println);
    }

}
