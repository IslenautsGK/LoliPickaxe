package com.anotherstar.common.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigField {

	ConfigType[] type();

	String comment();

	ValurType valueType();

	int intDefaultValue() default 0;

	int intMinValue() default 0;

	int intMaxValue() default 0;

	String intMinValueField() default "";

	String intMaxValueField() default "";

	double doubleDefaultValue() default 0.0;

	double doubleMinValue() default 0;

	double doubleMaxValue() default 0;

	String doubleMinValueField() default "";

	String doubleMaxValueField() default "";

	boolean booleanDefaultValue() default false;

	String stringDefaultValue() default "";

	String[] listDefaultValue() default {};

	ValurType listType() default ValurType.STRING;

	String[] mapDefaultValue() default {};

	ValurType mapKeyType() default ValurType.STRING;

	ValurType mapValueType() default ValurType.INT;

	boolean warning() default false;

	String warningMethod() default "";

	public static enum ConfigType {
		NONE, CONFIG, COMMAND, GUI
	}

	public static enum ValurType {
		INT, DOUBLE, BOOLEAN, STRING, LIST, MAP
	}

}
