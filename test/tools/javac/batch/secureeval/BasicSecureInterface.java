package secureeval;

import java.io.IOException;

import batch.IncludeInBatch;
import batch.security.User;

public abstract class BasicSecureInterface {

	@IncludeInBatch
	public boolean fooAllow(User user, int x) {
		return user.username.equals("bob");
	}
	
	@IncludeInBatch
	public boolean allow(User user) {
		return user.username.equals("bob");
	}
	
//	@IncludeInBatch() public @batch.util.BatchProcessed() static  <E extends java.lang.Object> E allow$getRemote(batch.util.Forest s$, batch.util.BatchFactory<E> f$, E user) {
//		E e$ = f$.Call(f$.Prop(f$.Var("user"), "username"), "equals", f$.Data("bob"));
//		e$ = f$.Let("user", user, e$);
//		return e$;
//	}
//	@IncludeInBatch() public @batch.util.BatchProcessed() static  <E extends java.lang.Object> E fooAllow$getRemote(batch.util.Forest s$, batch.util.BatchFactory<E> f$, E user, E x) {
//		E e$ = f$.Call(f$.Prop(f$.Var("user"), "username"), "equals", f$.Data("bob"));
//		e$ = f$.Let("user", user, e$);
//		e$ = f$.Let("x", x, e$);
//		return e$;
//	}

	
	public abstract int foo(int x);

	public abstract BasicSecureInterface bar(int x);

	public abstract byte[] getImage(String name) throws IOException;

}