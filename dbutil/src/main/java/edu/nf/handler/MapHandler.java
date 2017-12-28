package edu.nf.handler;

import edu.nf.ResultSetHandler;
import edu.nf.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MapHandler implements ResultSetHandler<Map<String, Object>> {

	public Map<String, Object> handle(ResultSet rs) throws SQLException {
		return rs.next() ? RowProcessor.toMap(rs) : null;
	}

}
