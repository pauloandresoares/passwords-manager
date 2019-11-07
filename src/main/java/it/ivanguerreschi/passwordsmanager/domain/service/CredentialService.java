/*
  passwords-manager use Quarkus for back-end and Angular for front-end.
  Copyright (C) 2019  Ivan Guerreschi

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

  Email ivanguerreschi86@gmail.com
*/

package it.ivanguerreschi.passwordsmanager.domain.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.json.Json;

import it.ivanguerreschi.passwordsmanager.domain.model.Credential;;

@ApplicationScoped
public class CredentialService implements ServiceInterface {

  @Override
  public List<Credential> credentials() {
    return Credential.listAll();
  }

  @Override
  public void save(Credential credential) {
    if (credential.id != null) {
      throw new WebApplicationException("Id was invalidly set on request.", 422);
    }
    credential.persist();
  }

  @Provider
  public static class ErrorMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
      int code = 500;
      if (exception instanceof WebApplicationException) {
        code = ((WebApplicationException) exception).getResponse().getStatus();
      }
      return Response.status(code)
          .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build()).build();
    }

  }

}
