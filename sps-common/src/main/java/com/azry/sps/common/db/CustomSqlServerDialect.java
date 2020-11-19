package com.azry.sps.common.db;


import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class CustomSqlServerDialect extends SQLServer2008Dialect {
	
	private static final int MAX_LENGTH = 8000;
	
	public CustomSqlServerDialect() {
		super();
		registerColumnType(Types.BOOLEAN, "bit");
		
		registerColumnType(Types.DATE, "datetime");
		registerColumnType(Types.TIME, "datetime");
		registerColumnType(Types.TIMESTAMP, "datetime");
		
		registerColumnType(Types.BLOB, "varbinary(MAX)");
		
		registerColumnType(Types.VARBINARY, "varbinary");
		registerColumnType(Types.VARBINARY, MAX_LENGTH, "varbinary($l)");
		registerColumnType(Types.LONGVARBINARY, "varbinary");
		
		registerColumnType(Types.VARCHAR, 1000, "nvarchar($l)");
		registerColumnType(Types.VARCHAR, 2000, "nvarchar($l)");
		registerColumnType(Types.VARCHAR, 4000, "nvarchar($l)");
		registerColumnType(Types.VARCHAR, "nvarchar(MAX)");
		registerColumnType(Types.CHAR, "nchar(1)");
		registerColumnType(Types.CLOB, "nvarchar(max)");
		
		
		registerFunction("trunc_day", new VarArgsSQLFunction( StandardBasicTypes.TIMESTAMP, "dateadd(dd, datediff(dd, 0, ", ",", "), 0)" ));
		registerFunction("trunc_month", new VarArgsSQLFunction( StandardBasicTypes.TIMESTAMP, "dateadd(mm, datediff(mm, 0, ", ",", "), 0)" ));
		registerFunction("trunc_year", new VarArgsSQLFunction( StandardBasicTypes.TIMESTAMP, "dateadd(yy, datediff(yy, 0, ", ",", "), 0)" ));
		registerFunction("trunc_year", new VarArgsSQLFunction( StandardBasicTypes.BOOLEAN, "dateadd(yy, datediff(yy, 0, ", ",", "), 0)" ));
	}
}