package com.biit.form.manager.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.entity.FormDescription;

@Repository
@Transactional
public interface IFormDescriptionRepository extends JpaRepository<FormDescription, Long> {

	List<FormDescription> findByUser(CompanyUser user);

	List<FormDescription> findByCreationTimeGreaterThanOrderByCreationTimeAsc(Timestamp creationTime);

	List<FormDescription> findByCreationTimeBetweenOrderByCreationTimeAsc(Timestamp startTime, Timestamp endTime);

	FormDescription findTopByDocumentOrderByCreationTimeDesc(String document);
	
	//findFirst1ByOrderByCreationTimeDesc

	List<FormDescription> findByStoredInNas(boolean storedInNas);
}
