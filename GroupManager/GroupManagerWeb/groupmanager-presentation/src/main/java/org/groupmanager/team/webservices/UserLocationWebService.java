package org.groupmanager.team.webservices;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gropumanager.team.comunications.CoordonatesComunication;


/**
 * 
 * @author Cristi This class is used to get position from client.
 */

@Path("/services")
public class UserLocationWebService {
	@Inject
	private CoordonatesComunication coordCom;

	@POST
	@Path("/sendLocation")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocationREST(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + crunchifyBuilder.toString());
		System.out.println("Date de la EJB: " + coordCom.getMessage());

		// return HTTP response 200 in case of success
		return Response.status(200).entity(crunchifyBuilder.toString()).build();
	}
}
