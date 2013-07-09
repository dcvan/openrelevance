package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface EvaluationResource {
	/***
	 * 
	 * @return a summary of each evaluation, including id, time stamp(id?), 
	 * URL of evaluated search engine, schema access, test collection access,
	 * result access and scores(if any).  
	 */
	
	@Get
	public Representation getSummary();
	
}
