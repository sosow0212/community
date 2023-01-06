package yoon.community.factory;

import yoon.community.domain.board.Board;
import yoon.community.domain.board.Favorite;
import yoon.community.domain.member.Member;

public class FavoriteFactory {
    public static Favorite createFavoriteWithNotFavorite(Board board, Member member) {
        return new Favorite(1L, board, member, false);
    }

    public static Favorite createFavoriteWithFavorite(Board board, Member member) {
        return new Favorite(1L, board, member, true);
    }
}
