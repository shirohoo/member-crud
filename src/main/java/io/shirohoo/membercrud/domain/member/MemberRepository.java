package io.shirohoo.membercrud.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(final String username);

    boolean existsByUsername(final String username);
}
