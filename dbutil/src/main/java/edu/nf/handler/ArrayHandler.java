package edu.nf.handler;

import edu.nf.ResultSetHandler;
import edu.nf.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArrayHandler implements ResultSetHandler<Object[]> {

	
	public Object[] handle(ResultSet rs) throws SQLException {
		return rs.next() ? RowProcessor.toArray(rs) : null;
	}

}
