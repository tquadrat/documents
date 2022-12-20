package org.tquadrat.foundation.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *  The marker annotation for methods that are meant to be 
 *  overwritten in child classes.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@Documented
@Retention( SOURCE )
@Target( METHOD )
public @interface MountPoint
{ 
    /**
     *  Optionally provides a short description on how to use this 
     *  mount-point.
     *
     *  @return The description of the mount point.
     */
    String value() default "";
}
//  @interface MountPoint

