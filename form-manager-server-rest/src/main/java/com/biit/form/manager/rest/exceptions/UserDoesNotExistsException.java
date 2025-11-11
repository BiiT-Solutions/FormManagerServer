package com.biit.form.manager.rest.exceptions;

/*-
 * #%L
 * Form Manager Server (Rest)
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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "User not stored into database.")
public class UserDoesNotExistsException extends Exception {
	private static final long serialVersionUID = 9106350377910563925L;

	public UserDoesNotExistsException(String message) {
		super(message);
	}

	public UserDoesNotExistsException(String message, Throwable e) {
		super(message, e);
	}

}
