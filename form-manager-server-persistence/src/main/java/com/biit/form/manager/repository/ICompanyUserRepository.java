package com.biit.form.manager.repository;

import com.biit.form.manager.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ICompanyUserRepository extends JpaRepository<CompanyUser, Long> {

    CompanyUser findByLoginName(String loginName);

    CompanyUser findByLoginNameAndPassword(String loginName, String password);

    CompanyUser findByEmailAddressAndPassword(String email, String password);

    CompanyUser findByEmailAddress(String email);
}
