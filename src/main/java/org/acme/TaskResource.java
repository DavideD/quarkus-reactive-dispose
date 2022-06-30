package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;

@Path("/task")
@Produces(MediaType.TEXT_PLAIN)
public class TaskResource {

	@Inject
	Task task;

	@GET
	@Path("start")
	public Uni<String> start() {
		return task.start();

	}

	@GET
	@Path("total")
	public Long total() {
		return TaskProducer.COUNTER.get();
	}
}