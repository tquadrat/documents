package org.tquadrat.foundation.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *  This annotation allows to add information about applied fixes to 
 *  a program element.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@Documented
@Retention( RUNTIME )
@Target( {ANNOTATION_TYPE, CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, MODULE, PACKAGE, PARAMETER, RECORD_COMPONENT, TYPE, TYPE_PARAMETER, TYPE_USE} )
@Repeatable( FixList.class )
public @interface BUG
{
        /*------------*\
    ====** Attributes **=============================================
        \*------------*/
    /**
     *  An optional comment regarding the bug fix.
     *
     *  @return The comment.
     */
    String comment() default "";

    /**
     *  The BUG id as provided by the bug tracking system.
     *
     *  @return The BUG id.
     */
    String id();
}
//  @interface BUG

