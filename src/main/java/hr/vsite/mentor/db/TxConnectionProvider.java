package hr.vsite.mentor.db;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

class TxConnectionProvider implements Provider<Connection> {

	TxConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection get() {
		if (!hasActiveTransaction())
			throw new IllegalStateException("Connection unavailable outside of transaction. Start tx first (or annotate service method with @Transactional)");
		return currentTxConnection.get().get();
	}

	@Inject
	public void setRequestProvider(Provider<HttpServletRequest> requestProvider) {
		this.requestProvider = requestProvider;
	}
	
	Boolean hasActiveTransaction() {
		return currentTxConnection.get().isPresent();
	}

	void startNewTransaction() {
//		try {
//			requestProvider.get();
//			System.err.println("DO have request!");
//		} catch (Exception e) {
//			System.err.println("DON'T have request! " + e.getClass().getName());
//		}
		try {
			Connection connection = dataSource.getConnection();
			currentTxConnection.set(Optional.of(connection));
		} catch (SQLException e) {
			throw new RuntimeException("Error initializing transaction", e);
		}
	}

	void commitActiveTransaction() {
		try {
			currentTxConnection.get().get().commit();
		} catch (SQLException e) {
			throw new RuntimeException("Error committing transaction", e);
		}
	}

	void rollbackActiveTransaction() {
		try {
			currentTxConnection.get().get().rollback();
		} catch (SQLException e) {
			throw new RuntimeException("Error rolling back transaction", e);
		}
	}

	void removeActiveTransactionConnection() {
		if (currentTxConnection.get().isPresent()) {
			try {
				currentTxConnection.get().get().close();
			} catch (SQLException e) {
				throw new RuntimeException("Error closing connection after transaction commit.", e);
			}
			currentTxConnection.set(Optional.empty());
		}
	}

	private final ThreadLocal<Optional<Connection>> currentTxConnection = new ThreadLocal<Optional<Connection>>() {
		@Override
		protected Optional<Connection> initialValue() {
			return Optional.empty();
		}
	};

	private final DataSource dataSource;
	private Provider<HttpServletRequest> requestProvider;
	
}
