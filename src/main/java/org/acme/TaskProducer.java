package org.acme;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Produces;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.Counter;

@Singleton
public class TaskProducer {

	public static final String COUNTER_NAME = "Counter.Task";

	@Inject
	Vertx vertx;

	@Produces
	@RequestScoped
	public Uni<Task> createTask() {
		Log.info( "Creating a new Task" );
		return getCounter( vertx )
				.map( Counter::incrementAndGet )
				.map( Future::toCompletionStage )
				.chain( Uni.createFrom()::completionStage )
				.map( Task::new );
	}

	public static Uni<Counter> getCounter(Vertx vertx) {
		return Uni.createFrom()
				.completionStage( vertx.sharedData().getCounter( COUNTER_NAME ).toCompletionStage() );
	}

	public Uni<String> dispose(@Disposes Uni<Task> taskUni) {
		Log.info( "Finishing the task" );
		return taskUni.chain( Task::done )
				.call( s -> getCounter( vertx )
						.map( Counter::decrementAndGet )
						.map( Future::toCompletionStage )
						.chain( Uni.createFrom()::completionStage )
				);
	}
}
