package com.maan.insurance.jpa.dialect;

import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class Oracle12cCustomDialect extends Oracle12cDialect {
	
	public Oracle12cCustomDialect() {
	    super();
	    registerFunction("FN_GET_NAME", new StandardSQLFunction("FN_GET_NAME", StandardBasicTypes.STRING));
	}

}
