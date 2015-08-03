package org.jessica.core.util.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ����ע��������Ե�Ԫ���ݣ���ЩԪ���ݿ����ڴ������ɻ�����ʱ��̬��������
 * 
 * @author lide
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PACKAGE })
public @interface MetaData {
	String defaultValue() default "";

	/**
	 * ��Ҫע��˵����һ���Ӧ����Label������ʾ
	 */
	String value();

	/**
	 * ����form��ʾ��Lable
	 * 
	 * @return
	 */
	String labelText() default "";

	/**
	 * ����Ĭ�ϵ�����
	 */
	int sortNumber() default 0;

	/**
	 * ���ڱ���ʾ�ķ����ǩ
	 * 
	 */
	String groupText() default "";

	/**
	 * ��ʾ��Ϣ��һ���Ӧ�������ʾ˵����֧����HTML��ʽ
	 */
	String tooltips() default "";

	/**
	 * ע��˵�����������������ڲ��÷�˵����һ�㲻����ǰ��UI��ʾ
	 */
	String comments() default "";

	/**
	 * ��ʶ�����Ƿ��ڴ���������ɱ༭
	 */
	boolean editable() default true;

}
