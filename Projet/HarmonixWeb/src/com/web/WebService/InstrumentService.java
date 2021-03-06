package com.web.WebService;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ejb.Entity.Instrument;
import com.ejb.SessionBean.InstrumentResource;

@ManagedBean
@RequestScoped
@Path("/instruments")
public class InstrumentService {

	@Inject
	InstrumentResource ir;

	@POST
	@Consumes("application/json")
	public Instrument create(final Instrument instrument) {
		return ir.create(instrument);
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") final Long id) {
		Instrument instrument = ir.getById(id);
		if (instrument == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(instrument).build();
	}

	@GET
	@Produces("application/json")
	public List<Instrument> listAll(@QueryParam("start") final Integer startPosition,
			@QueryParam("max") final Integer maxResult, @QueryParam("categorie") final String categorie) {
		List<Instrument> instruments = ir.getAllInstruments(startPosition, maxResult, categorie);

		// TRAITEMENT BARBARE: JsonView ne marchant pas (pas encore)
		// On enl�ve les informations inutiles � l'affichage de la liste
		// c-�-d les avis, et on ne laisse que 3 caract�ristiques.
		for (Instrument i : instruments) {
			i.setAvis(null);
		}

		return instruments;
	}

	@GET
	@Path("promotions")
	@Produces("application/json")
	public List<Instrument> listAllPromotions(@QueryParam("start") final Integer startPosition,
			@QueryParam("max") final Integer maxResult) {
		List<Instrument> instruments = ir.getAllInstrumentsType(0, startPosition, maxResult);
		
		for (Instrument i : instruments) {
			i.setAvis(null);
		}
		
		return instruments;
	}

	@GET
	@Path("nouveautes")
	@Produces("application/json")
	public List<Instrument> listAllNouveautes(@QueryParam("start") final Integer startPosition,
			@QueryParam("max") final Integer maxResult) {
		List<Instrument> instruments = ir.getAllInstrumentsType(1, startPosition, maxResult);
		
		for (Instrument i : instruments) {
			i.setAvis(null);
		}
		
		return instruments;
	}
	
	@GET
	@Path("meilleures-ventes")
	@Produces("application/json")
	public List<Instrument> listAllMeilleuresVentes(@QueryParam("start") final Integer startPosition,
			@QueryParam("max") final Integer maxResult) {
		List<Instrument> instruments = ir.getAllInstrumentsType(2, startPosition, maxResult);
		
//		for (Instrument i : instruments) {
//			i.setAvis(null);
//		}
		
		return instruments;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	@Produces("text/plain")
	public Response update(@PathParam("id") Long id, final Instrument instrument) {
		Response r = null;
		try {
			ir.updateInstrument(instrument);
			r = Response.ok("OK").build();
		} catch (Exception e) {
			System.out.println("exception in create " + e);
			r = Response.ok("error").build();
		}
		return r;
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response deleteById(@PathParam("id") final Long id) {
		Response r = null;
		try {
			ir.removeInstrument(id);
			r = Response.ok("OK").build();
		} catch (Exception e) {
			System.out.println("exception in create " + e);
			r = Response.ok("error").build();
		}
		return r;
	}

}
