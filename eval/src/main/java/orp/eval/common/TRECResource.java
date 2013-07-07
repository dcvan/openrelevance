package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface TRECResource {
	
	/***
	 * 
	 * @return an introduction of TREC evaluation.
	 */
	
	@Get
	public Representation getIntro();
}
