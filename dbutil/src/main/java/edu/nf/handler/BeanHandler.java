package edu.nf.handler;

import edu.nf.ResultSetHandler;
import edu.nf.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanHandler<T> implements ResultSetHandler<T> {
	
	private Class<T> type;
	
	public BeanHandler(Class<T> type){
		this.type = type;
	}

	public T handle(ResultSet rs) throws SQLException {
		return rs.next() ? RowProcessor.toBean(rs, type) : null;
	}

}
