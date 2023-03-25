package yoon.community.service.member;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoon.community.domain.board.Favorite;
import yoon.community.domain.member.Member;
import yoon.community.dto.board.BoardSimpleDto;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.dto.member.MemberSimpleResponseDto;
import yoon.community.exception.MemberNotFoundException;
import yoon.community.repository.board.FavoriteRepository;
import yoon.community.repository.member.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    public List<MemberSimpleResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberSimpleResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberSimpleResponseDto findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberSimpleResponseDto.toDto(member);
    }


    @Transactional
    public Member editMemberInfo(Member member, MemberEditRequestDto memberEditRequestDto) {
        // refreshToken 처리는 어떻게 할지 추후에 고민해보기
        member.editUser(memberEditRequestDto);
        return member;
    }

    @Transactional
    public void deleteMemberInfo(Member member) {
        // jwt 토큰 만료 처리는 어떻게 할지 추후에 고민해보기
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findFavorites(Member member) {
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(favorite -> BoardSimpleDto.toDto(favorite.getBoard()))
                .collect(Collectors.toList());
    }
}
