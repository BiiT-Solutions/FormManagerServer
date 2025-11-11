package com.biit.form.manager.repository;

/*-
 * #%L
 * Form Manager Server (Persistence)
 * %%
 * Copyright (C) 2019 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
