package samsamoo.ai_mockly.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.domain.member.dto.response.MemberInfoRes;
import samsamoo.ai_mockly.domain.point.domain.repository.PointRepository;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    public SuccessResponse<MemberInfoRes> getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        Integer totalPoint = pointRepository.sumActiveAmountByMember(member);

        MemberInfoRes memberInfoRes = MemberInfoRes.builder()
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .maxScore(member.getMaxScore())
                .pointAmount(totalPoint)
                .build();

        return SuccessResponse.of(memberInfoRes);
    }

    @Transactional
    public SuccessResponse<Message> updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        if(nickname != null || nickname.isBlank()) {
            boolean exists = memberRepository.existsByNickname(nickname);
            if(exists) {
                throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
            }

            member.updateNickname(nickname);
        }

        Message message = Message.builder()
                .message("닉네임이 수정되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
    public  SuccessResponse<Message> modifyProfileImage(Long memberId, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        String imageUrl = profileImage.getOriginalFilename();

        if(!profileImage.isEmpty()) {
            member.updateProfileImage(imageUrl);
        }

        Message message = Message.builder()
                .message("프로필 사진이 수정되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
