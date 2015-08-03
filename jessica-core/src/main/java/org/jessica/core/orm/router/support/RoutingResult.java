package org.jessica.core.orm.router.support;

import java.util.List;

import org.jessica.core.orm.merger.IMerger;

public class RoutingResult {
	private List<String> resourceIdentities;
	private IMerger<?, ?> merger;

	public List<String> getResourceIdentities() {
		return resourceIdentities;
	}

	public void setResourceIdentities(List<String> resourceIdentities) {
		this.resourceIdentities = resourceIdentities;
	}

	public void setMerger(IMerger<?, ?> merger) {
		this.merger = merger;
	}

	public IMerger<?, ?> getMerger() {
		return merger;
	}

}
