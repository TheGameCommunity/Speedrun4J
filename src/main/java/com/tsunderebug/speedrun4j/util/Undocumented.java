package com.tsunderebug.speedrun4j.util;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Represents that this uses an undocumented endpoint which may break
 * or be unavailable in the future
 */
@Retention(CLASS)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR, PACKAGE, MODULE })
public @interface Undocumented {

}
