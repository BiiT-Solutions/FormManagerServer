package com.biit.form.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.form.manager.entity.FormDescription;
import com.biit.form.manager.entity.UploadedFile;

@Repository
@Transactional
public interface IUploadedFileRepository extends JpaRepository<UploadedFile, Long> {

	List<UploadedFile> findByFormDescription(FormDescription formDescription);
}
