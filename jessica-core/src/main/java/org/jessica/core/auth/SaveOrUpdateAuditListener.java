package org.jessica.core.auth;
import java.util.Date;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

 /**
	 * ��Ƽ�¼��¼�������޸���Ϣ
	 * @see AuditingEntityListener
	 *
	 */
	@Component
	public class SaveOrUpdateAuditListener {

	    private Logger logger = LoggerFactory.getLogger(getClass());

	    private boolean dateTimeForNow = true;
	    private boolean modifyOnCreation = false;
	    //���ǵ�Ч��Ӱ���ʵ�����ò���Ĭ�Ϲر�update���¼�¼����
	    //�����Ҫ��¼������̣����Կ���ʹ��ר�ŵ�hibernate envers����
	    private boolean skipUpdateAudit = true;

	    public void setDateTimeForNow(boolean dateTimeForNow) {
	        this.dateTimeForNow = dateTimeForNow;
	    }

	    public void setModifyOnCreation(final boolean modifyOnCreation) {
	        this.modifyOnCreation = modifyOnCreation;
	    }

	    /**
	     * Sets modification and creation date and auditor on the target object in
	     * case it implements {@link DefaultAuditable} on persist events.
	     * 
	     * @param target
	     */
	    @PrePersist
	    public void touchForCreate(Object target) {
	        touch(target, true);
	    }

	    /**
	     * Sets modification and creation date and auditor on the target object in
	     * case it implements {@link DefaultAuditable} on update events.
	     * 
	     * @param target
	     */
	    @PreUpdate
	    public void touchForUpdate(Object target) {
	        if (skipUpdateAudit) {
	            return;
	        }
	        touch(target, false);
	    }

	    private void touch(Object target, boolean isNew) {

	        if (!(target instanceof DefaultAuditable)) {
	            return;
	        }

	        @SuppressWarnings("unchecked")
	        DefaultAuditable<String, ?> auditable = (DefaultAuditable<String, ?>) target;

	        String auditor = touchAuditor(auditable, isNew);
	        Date now = dateTimeForNow ? touchDate(auditable, isNew) : null;

	        Object defaultedNow = now == null ? "not set" : now;
	        Object defaultedAuditor = auditor == null ? "unknown" : auditor;

	        logger.trace("Touched {} - Last modification at {} by {}", new Object[] { auditable, defaultedNow, defaultedAuditor });
	    }

	    /**
	     * Sets modifying and creating auditioner. Creating auditioner is only set
	     * on new auditables.
	     * 
	     * @param auditable
	     * @return
	     */
	    private String touchAuditor(final DefaultAuditable<String, ?> auditable, boolean isNew) {

	        String auditor = AuthContextHolder.getAuthUserDisplay();

	        if (isNew) {

	            auditable.setCreatedBy(auditor);

	            if (!modifyOnCreation) {
	                return auditor;
	            }
	        }

	        auditable.setLastModifiedBy(auditor);

	        return auditor;
	    }

	    /**
	     * Touches the auditable regarding modification and creation date. Creation
	     * date is only set on new auditables.
	     * 
	     * @param auditable
	     * @return
	     */
	    private Date touchDate(final DefaultAuditable<String, ?> auditable, boolean isNew) {

	        Date now = new Date();

	        if (isNew) {
	            auditable.setCreatedDate(now);

	            if (!modifyOnCreation) {
	                return now;
	            }
	        }

	        auditable.setLastModifiedDate(now);

	        return now;
	    }
	}

