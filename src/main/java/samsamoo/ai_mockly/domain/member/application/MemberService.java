package samsamoo.ai_mockly.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

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
}
