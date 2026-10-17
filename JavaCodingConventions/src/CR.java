/*
 * ============================================================================
 *  Copyright © 2002-2026 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

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
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;

/**
 *  <p>{@summary This annotation allows to add information about applied fixes
 *  (aka. &quot;Change Requests&quot;) to a program element.}</p>
 *  <p>This annotation does not appear in the Javadoc documentation of the
 *  annotated program elements.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CR.java 1294 2026-10-17 15:40:42Z tquadrat $
 *  @since 0.25.14
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "NewClassNamingConvention" )
@ClassVersion( sourceVersion = "$Id: CR.java 1294 2026-10-17 15:40:42Z tquadrat $" )
@API( status = STABLE, since = "0.25.14" )
@Retention( RUNTIME )
@Target( {ANNOTATION_TYPE, CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, MODULE, PACKAGE, PARAMETER, RECORD_COMPONENT, TYPE, TYPE_PARAMETER, TYPE_USE} )
@Repeatable( ChangeRequestList.class )
public @interface CR
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary Types of change requests.}</p>
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: CR.java 1294 2026-10-17 15:40:42Z tquadrat $
     *  @since 0.25.14
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: CR.java 1294 2026-10-17 15:40:42Z tquadrat $" )
    @API( status = STABLE, since = "0.25.14" )
    public enum CRType
    {
        /**
         *  <p>{@summary The implementation of a generic change request.}</p>
         */
        @SuppressWarnings( "FieldNamingConvention" )
        CR,

        /**
         *  <p>{@summary A bug fix.}</p>
         */
        @SuppressWarnings( "FieldNamingConvention" )
        BUG,

        /**
         *  <p>{@summary A patch for debugging purposes; perhaps only temporary.}</p>
         */
        DEBUG,

        /**
         *  <p>{@summary The implementation of an enhancement request.}</p>
         */
        @SuppressWarnings( "FieldNamingConvention" )
        ENH,

        /**
         *  <p>{@summary A field service patch.}</p>
         */
        @SuppressWarnings( "FieldNamingConvention" )
        FSP,

        /**
         *  <p>{@summary A maintenance patch.}</p>
         */
        PATCH,

        /**
         *  <p>{@summary A change that was required by a new version of the
         *  product.} Use this type if there is no change request id for this
         *  particular change. Use the new version as the
         *  {@linkplain #id() id}.</p>
         */
    }
    //  enum CRType

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary An optional comment regarding the change.}</p>
     *
     *  @return The comment.
     */
    String comment() default "";

    /**
     *  <p>{@summary The date when the change was applied, in the format
     *  &quot;YYYY-mm-dd&quot;.} Will be ignored if empty.</p>
     *
     *  @return The change date.
     *
     *  @see java.util.Formatter
     */
    String date() default "";

    /**
     *  <p>{@summary The id for the change request as provided by the
     *  respective tracking system.}</p>
     *
     *  @return The change request id.
     */
    @SuppressWarnings( "NewMethodNamingConvention" )
    String id();

    /**
     *  <p>{@summary The type for the change request.}</p>
     *
     *  @return The type of the change request.
     */
    CRType type() default CRType.CR;
}
//  @interface BUG

/*
 *  End of File
 */