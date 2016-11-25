package hr.vsite.mentor.db;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class JdbcUtils {

	private JdbcUtils() {}
	
	public static Integer getOptionalInteger(ResultSet resultSet, String columnLabel) throws SQLException {
		Integer result = resultSet.getInt(columnLabel);
		return !resultSet.wasNull() ? result : null;
	}
	
	public static Double getOptionalDouble(ResultSet resultSet, String columnLabel) throws SQLException {
		Double result = resultSet.getDouble(columnLabel);
		return !resultSet.wasNull() ? result : null;
	}
	
	public static <T> List<T> array2List(Array sqlArray, Class<T[]> clazz) throws SQLException {

		if (sqlArray == null)
			return null;
		
		T[] array = clazz.cast(sqlArray.getArray());
		if (array == null)
			throw new RuntimeException("Unable to map SQL array to Java array");
		
		sqlArray.free();
		
		return Arrays.asList(array);

	}
	
	
}
