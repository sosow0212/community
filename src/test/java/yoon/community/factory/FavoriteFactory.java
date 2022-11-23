package yoon.community.factory;

import yoon.community.entity.board.Board;
import yoon.community.entity.board.Favorite;
import yoon.community.entity.user.User;

public class FavoriteFactory {
    public static Favorite createFavoriteWithNotFavorite(Board board, User user) {
        return new Favorite(1, board, user, false, null);
    }

    public static Favorite createFavoriteWithFavorite(Board board, User user) {
        return new Favorite(1, board, user, true, null);
    }
}
