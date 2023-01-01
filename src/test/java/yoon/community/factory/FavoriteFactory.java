package yoon.community.factory;

import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.member.Member;

public class FavoriteFactory {
    public static Favorite createFavoriteWithNotFavorite(Board board, Member member) {
        return new Favorite(1L, board, member, false);
    }

    public static Favorite createFavoriteWithFavorite(Board board, Member member) {
        return new Favorite(1L, board, member, true);
    }
}
