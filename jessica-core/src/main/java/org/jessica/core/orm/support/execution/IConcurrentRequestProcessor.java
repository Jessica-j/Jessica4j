  package org.jessica.core.orm.support.execution;

import java.util.List;

public interface IConcurrentRequestProcessor {
    List<Object> process(List<ConcurrentRequest> requests);
}
