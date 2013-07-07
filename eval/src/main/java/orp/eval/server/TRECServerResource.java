package orp.eval.server;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;

import orp.eval.common.TRECResource;

public class TRECServerResource extends WadlServerResource implements TRECResource{

	public Representation getIntro() {
		return new StringRepresentation("Show introduction of TREC evaluation.");
	}

}
