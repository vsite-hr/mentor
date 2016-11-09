package hr.vsite.mentor.db;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

class TransactionInterceptor implements MethodInterceptor {

	TransactionInterceptor(TxConnectionProvider standaloneTxConnectionProvider) {
		this.standaloneTxConnectionProvider = standaloneTxConnectionProvider;
	}

	public Object invoke(MethodInvocation methodInvocation) throws Throwable {

		final Boolean newTransactionStarted;
		if (!standaloneTxConnectionProvider.hasActiveTransaction()) {
			standaloneTxConnectionProvider.startNewTransaction();
			newTransactionStarted = true;
		} else {
			newTransactionStarted = false;
		}

		try {
			Object result = methodInvocation.proceed();
			if (newTransactionStarted)
				standaloneTxConnectionProvider.commitActiveTransaction();
			return result;
		} catch (Exception e) {
			if (rollbackNecessary(e, transactional(methodInvocation)))
				standaloneTxConnectionProvider.rollbackActiveTransaction();
			else
				standaloneTxConnectionProvider.commitActiveTransaction();
			throw e;
		} finally {
			if (newTransactionStarted)
				standaloneTxConnectionProvider.removeActiveTransactionConnection();
		}

	}

	private Transactional transactional(MethodInvocation methodInvocation) {
		return suppliers(
			() -> methodInvocation.getMethod().getAnnotation(Transactional.class),
			() -> methodInvocation.getThis().getClass().getAnnotation(Transactional.class),
			this::defaultTransactional
		).stream()
			.map(Supplier::get)
			.filter(transactional -> transactional != null)
			.findFirst()
			.get();
	}

	@SafeVarargs
	private static <T> List<Supplier<T>> suppliers(Supplier<T>... suppliers) {
		return Arrays.asList(suppliers);
	}

	private boolean rollbackNecessary(Exception cause, Transactional transactional) {
		return has(transactional.rollbackOn(), cause) && !has(transactional.dontRollbackOn(), cause);
	}

	private Boolean has(Class<?>[] exceptions, Exception cause) {
		return Arrays.asList(exceptions).stream().filter(e -> e.isInstance(cause)).findAny().isPresent();
	}

	private Transactional defaultTransactional() {
		return DefaultTransactionalDummy.class.getAnnotation(Transactional.class);
	}

	@Transactional
	private static class DefaultTransactionalDummy {
		private DefaultTransactionalDummy() {
		}
	}

	private final TxConnectionProvider standaloneTxConnectionProvider;

}

