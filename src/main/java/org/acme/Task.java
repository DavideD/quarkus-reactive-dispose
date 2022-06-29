package org.acme;

import io.smallrye.mutiny.Uni;

public class Task {

	private final Long id;

	public Task(Long id) {
		this.id = id;
	}

	public Uni<String> start() {
		return Uni.createFrom().item( "Started task " + id );
	}

	public Uni<String> done() {
		return Uni.createFrom().item( "Finished task " + id );
	}

	@Override
	public String toString() {
		return "Task:" + id;
	}
}
