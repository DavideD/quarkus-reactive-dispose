package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.shareddata.Counter;

@Path("/task")
@Produces(MediaType.TEXT_PLAIN)
public class TaskResource {

	@Inject
	Vertx vertx;

	@Inject
	Uni<Task> taskUni;

	@GET
	@Path("start")
	public Uni<String> start() {
		return taskUni.chain( Task::start );

	}

	@GET
	@Path("decrease")
	public Uni<String> decrease() {
		return TaskProducer.getCounter( vertx )
				.chain( Counter::decrementAndGet )
				.map( count -> "Decreased tasks to " + count );
	}

	@GET
	@Path("total")
	public Uni<Long> total() {
		return TaskProducer.getCounter( vertx ).chain( Counter::get );
	}
}