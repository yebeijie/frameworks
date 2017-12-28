package edu.nf.handler;

import edu.nf.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ArrayListHandler extends AbstractListHandler<Object[]> {

	@Override
	protected Object[] getRow(ResultSet rs) throws SQLException {
		return RowProcessor.toArray(rs);
	}

}
