package org.acme;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Produces;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.shareddata.Counter;

@Singleton
public class TaskProducer {

	public static final String COUNTER_NAME = "Counter.Current";

	@Inject
	Vertx vertx;

	@Produces
	@RequestScoped
	public Uni<Task> createTask() {
		Log.info( "Creating a new Task" );
		return getCounter( vertx )
				.chain( Counter::incrementAndGet )
				.map( Task::new );
	}

	public static Uni<Counter> getCounter(Vertx vertx) {
		return vertx.sharedData().getCounter( COUNTER_NAME );
	}

	public Uni<String> dispose(@Disposes Uni<Task> taskUni) {
		Log.info( "Finishing the task" );
		return taskUni.chain( Task::done )
				.call( s -> getCounter( vertx ).chain( Counter::decrementAndGet ) );
	}
}
