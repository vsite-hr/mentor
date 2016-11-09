package hr.vsite.mentor.db;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class JdbcModule extends AbstractModule {

	public JdbcModule() {

		Properties props = new Properties();
		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties")) {
			if (istream == null)
				throw new FileNotFoundException("Could not find JDBC properties");
			props.load(istream);
			dataSource = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			throw new RuntimeException("Unable to initialize DataSource", e);
		}

		this.connectionProvider = new TxConnectionProvider(dataSource);
		this.transactionInterceptor = new TransactionInterceptor(connectionProvider);

	}

	@Override
	protected void configure() {

		bind(DataSource.class).toInstance(dataSource);

		bind(Connection.class).toProvider(connectionProvider);

		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionInterceptor);
		bindInterceptor(Matchers.annotatedWith(Transactional.class), Matchers.any(), transactionInterceptor);

	}

	private final DataSource dataSource;
	private final TxConnectionProvider connectionProvider;
	private final TransactionInterceptor transactionInterceptor;
	
}
