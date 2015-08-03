package org.jessica.core.orm.router.rules;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * horizontal partitioning oriented rule that matches against entity/table type
 * and attribute values.<br>
 * 
 * @author fujohnwang
 *
 * @param <F>
 * @param <T>
 */
public abstract class AbstractEntityAttributeRule<F, T> extends AbstractEntityTypeRule<F, T> {
	private String attributePattern;

	public AbstractEntityAttributeRule(String typePattern, String action) {
		super(typePattern, action);
	}

	public AbstractEntityAttributeRule(String typePattern, String action, String attributePattern) {
		super(typePattern, action);
		Validate.notEmpty(StringUtils.trimToEmpty(attributePattern));

		this.attributePattern = attributePattern;
	}

	public String getAttributePattern() {
		return attributePattern;
	}

	public void setAttributePattern(String attributePattern) {
		Validate.notEmpty(StringUtils.trimToEmpty(attributePattern));
		this.attributePattern = attributePattern;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attributePattern == null) ? 0 : attributePattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntityAttributeRule other = (AbstractEntityAttributeRule) obj;
		if (attributePattern == null) {
			if (other.attributePattern != null)
				return false;
		} else if (!attributePattern.equals(other.attributePattern))
			return false;
		return true;
	}

}
