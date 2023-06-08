# MyBatis로 객체지향적인 코드를 짜기 위한 테스트용 프로젝트

## 클래스 하나에 다 넣고 `resultType`으로 땡치는게 과연 유지보수 적으로 옳은 일일까?
이게 무슨 소리인가?

```java
// Board
@Data
@ToString
public class Board {
    Integer id;
    String title;
    String contents;
    String writer;
    List<Comment> comments;
}

// Comment
@Data
@ToString
public class Comment {
    Integer id;
    Integer boardId;
    String contents;
    String writer;

}
```
Mapper를 이용해 join 쿼리의 결과가 위의 두 객체에 담기면 좋을텐데 실무에서는 사실 이런 코드를 더 많이 봤다.
나는 아래 코드가 굉장히 문제가 많다고 생각한다.
1. 컬럼이 추가되고 조인할 대상이 늘어나면 클래스가 너무 무거워진다.
2. 반대로 컬럼이 삭제되어도 문제다.
3. 어떤 데이터가 쓰이고 있는지 한 클래스 안에 있으므로 구분이 되지 않는다.

```java
@Data
@ToString
public class BoardComment {
    // Board
    Integer id;
    String title;
    String contents;
    String writer;
    // Comment
    Integer commentId;
    Integer boardId;
    String commentContents;
    String commentWriter;
}
```

그래서 나는 객체에 객체를 맵핑하는 방법을 찾다가 좋은 글을 봤다.
[참고한 블로그](https://seungdols.tistory.com/948)
[마이바티스 공식 문서](https://mybatis.org/mybatis-3/ko/sqlmap-xml.html#collection-%EC%9D%84-%EC%9C%84%ED%95%9C-%EB%82%B4%ED%8F%AC%EB%90%9C-nested-select)

### myBatis가 지원하는 객체와 객체의 조인
1. nested select
2. nested result

# nested select

```xml
    <resultMap id="boardResult" type="cho.me.spring.board.dto.Board">
        <id property="id" column="id"/>
        <result property="title" column="title"/>
        <result property="contents" column="contents"/>
        <result property="writer" column="writer"/>
        <collection property="comments"
                    column="{boardId=id}"
                    select="getComment"
                    javaType="List"
                    ofType="cho.me.spring.board.dto.Comment"/>
    </resultMap>

    <select id="getBoard" resultMap="boardResult">
        SELECT
            b.id,
            b.title,
            b.contents,
            b.writer
        FROM
            board b
    </select>

    <select id="getComment" resultType="cho.me.spring.board.dto.Comment">
        select id,
               board_id,
               contents,
               writer
        from comment
        where board_id = #{boardId}
    </select>
```

```text
Board(id=1, title=데미안, contents=새는 알을 깨고 나오기 위해 자신의 세상을 부순다, writer=헤르만 헤세, comments=[Comment(id=1, boardId=1, contents=demian, writer=denis)])
Board(id=2, title=미녀와 야수, contents=누군가를 사랑하려면 너 자신을 먼저 사랑해, writer=린다 울버튼, comments=[Comment(id=2, boardId=2, contents=beauty and the beast, writer=miya)])
Board(id=3, title=미움 받을 용기, contents=트라우마를 부정하라, writer=고가 후미타케, comments=[])
Board(id=4, title=40살에 읽는 니체, contents=니체는 기꺼이 이생을 다시 살겠다고 말했다, writer=프레드리히 니체, comments=[])
```

# nested result

```xml
    <resultMap id="boardCommentResult" type="cho.me.spring.board.dto.Board">
        <id property="id" column="board_id"/>
        <result property="title" column="board_title"/>
        <result property="contents" column="board_contents"/>
        <result property="writer" column="board_writer"/>
        <collection property="comments" column="boardId" resultMap="commentResult" />
    </resultMap>

    <resultMap id="commentResult" type="cho.me.spring.board.dto.Comment">
        <id property="id" column="comment_id"/>
        <result property="boardId" column="comment_board_id"/>
        <result property="writer" column="comment_writer"/>
        <result property="contents" column="comment_content"/>
    </resultMap>

    <select id="getBoard" resultMap="boardCommentResult">
        SELECT
            b.id as board_id,
            b.title as board_title,
            b.contents as board_contents,
            b.writer as board_writer,
            c.id as comment_id,
            c.board_id as comment_board_id,
            c.contents as comment_contents,
            c.writer as comment_writer
        FROM
            board b join comment c on b.id = c.board_id
    </select>
```

```text
Board(id=1, title=데미안, contents=새는 알을 깨고 나오기 위해 자신의 세상을 부순다, writer=헤르만 헤세, comments=[Comment(id=1, boardId=1, contents=null, writer=denis)])
Board(id=2, title=미녀와 야수, contents=누군가를 사랑하려면 너 자신을 먼저 사랑해, writer=린다 울버튼, comments=[Comment(id=2, boardId=2, contents=null, writer=miya)])
```
