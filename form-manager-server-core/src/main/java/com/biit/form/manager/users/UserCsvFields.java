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

public enum UserCsvFields {
	COMPANY(0), FOLDER(1), USERNAME(2), PASSWORD(3);

	private final int csvIndex;

	private UserCsvFields(int csvIndex) {
		this.csvIndex = csvIndex;
	}

	public int getCsvIndex() {
		return csvIndex;
	}

	public static int length() {
		return UserCsvFields.values().length;
	}
}
