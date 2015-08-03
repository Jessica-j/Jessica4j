package org.jessica.core.orm.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
public class SimpleSqlAuditor implements ISqlAuditor {

    private transient final Logger logger = LoggerFactory.getLogger(SimpleSqlAuditor.class);

    public void audit(String id, String sql, Object sqlContext) {
        logger.info("SQL id:{} SQL:{} - Parameter:{}", new Object[] { id, sql, sqlContext });
    }

}
