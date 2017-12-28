package edu.nf.handler;

import edu.nf.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanListHandler<T> extends AbstractListHandler<T> {

	private Class<T> type;

	public BeanListHandler(Class<T> type) {
		this.type = type;
	}

	@Override
	protected T getRow(ResultSet rs) throws SQLException {
		return RowProcessor.toBean(rs, type);
	}

}
