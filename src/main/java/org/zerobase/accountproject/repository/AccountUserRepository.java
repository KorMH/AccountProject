package org.zerobase.accountproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.accountproject.domain.AccountUser;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
}
