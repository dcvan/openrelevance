package orp.eval.common;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface EvaluationResource {
	/***
	 * 
	 * @return a summary of each evaluation, including id, time stamp(id?), 
	 * URL of evaluated search engine, schema access, test collection access,
	 * result access and scores(if any).  
	 */
	
	@Get
	public Representation getSummary();
	
	/***
	 * 
	 * @param accept a JSON/XML document from client containing:<br>
	 * <ul>
	 * 	<li>URL of the evaluated search engine</li>
	 * 	<li>id of test collection</li>
	 * 	<li>measurement(OPTIONAL)</li>  
	 * </ul>
	 * Evaluation will fail if the test collection 
	 * is not available in the repository.
	 * 
	 * @return schema access, test collection access, result access and score by
	 * the measurement specified in the request. If metric is not specified, returns
	 * scores by all the measurements. 
	 */
	
	@Post
	public Representation run(Representation data);
	
}
