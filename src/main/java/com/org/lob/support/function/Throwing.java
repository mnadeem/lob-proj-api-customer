package com.org.lob.support.function;

import java.util.function.Consumer;

import org.springframework.lang.NonNull;


public final class Throwing {

	private Throwing() {
	}

	@NonNull
	public static <T> Consumer<T> rethrow(@NonNull final ThrowingConsumer<T> consumer) {
		return consumer;
	}

	/**
	 * The compiler sees the signature with the throws T inferred to a
	 * RuntimeException type, so it allows the unchecked exception to propagate.
	 * 
	 * http://www.baeldung.com/java-sneaky-throws
	 */
	@SuppressWarnings("unchecked")
	@NonNull
	public static <E extends Throwable> void sneakyThrow(@NonNull Throwable ex) throws E {
		throw (E) ex;
	}

}