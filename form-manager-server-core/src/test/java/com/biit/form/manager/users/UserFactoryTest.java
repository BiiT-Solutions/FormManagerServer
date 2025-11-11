package com.biit.form.manager.users;

/*-
 * #%L
 * Form Manager Server (Core)
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

import com.biit.form.manager.entity.CompanyUser;
import com.biit.form.manager.repository.ICompanyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
@Rollback
@Test(groups = "userFactory")
public class UserFactoryTest extends AbstractTransactionalTestNGSpringContextTests {
    private static final String CSV_FILE = "usuarios.csv";
    private static final int TOTAL_USERS = 178;

    @Autowired
    private ICompanyUserRepository userRepository;

    @Test
    public void insertCsvFile() throws IOException, URISyntaxException, InvalidCsvFile {
        // Load form from json file in resources.
        String csvContent = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(CSV_FILE).toURI())));

        long totalStoredUsers = userRepository.count();

        List<CompanyUser> companyUsers = UserFactory.createUsersFromCSV(csvContent);
        Assert.assertEquals(companyUsers.size(), TOTAL_USERS);
        companyUsers = userRepository.saveAll(companyUsers);
        Assert.assertEquals(companyUsers.size(), TOTAL_USERS);
        Assert.assertEquals(userRepository.count(), totalStoredUsers + TOTAL_USERS);

    }
}
