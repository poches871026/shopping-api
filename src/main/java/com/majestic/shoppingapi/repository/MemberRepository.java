package com.majestic.shoppingapi.repository;


import com.majestic.shoppingapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

}