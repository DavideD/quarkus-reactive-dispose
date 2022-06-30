package org.acme;

import java.util.concurrent.atomic.AtomicLong;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Produces;

import org.jboss.logging.Logger;

@Singleton
public class TaskProducer {

	public static final AtomicLong COUNTER = new AtomicLong();

	@Inject
	Logger log;

	@Produces
	@RequestScoped
	public Task createTask() {
		log.info( "Creating a new Task" );
		return new Task( COUNTER.incrementAndGet() );
	}

	// How it should work:
//	public Uni<Void> dispose(@Disposes Task task) {
//		Log.info( "Finishing the task" );
//		return task.done()
//				.invoke( () -> COUNTER.decrementAndGet() )
//				.replaceWithVoid();
//	}

	// Workaround we use now
	public void disposeWithWorkaround(@Disposes Task task) {
		log.info( "Finishing the task" );
		task.done()
				.invoke( () -> COUNTER.decrementAndGet() )
				.invoke( log::info )
				.subscribeAsCompletionStage();
	}
}
