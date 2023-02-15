package org.tquadrat.foundation.logging;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotBlankArgument;

import java.util.logging.Level;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary This class is a replacement for
 *  {@link Level}.} It provides alias names for the log levels</p>
 *  <ul>
 *      <li>{@link Level#FINE}</li>
 *      <li>{@link Level#FINEST}</li>
 *      <li>{@link Level#ERROR}</li>
 *  </ul>
 *  <p>so that the log levels for the JDK logging are better aligned 
 *  with those from Log4j.</p>
 *  <p>In addition it provides an additional log level
 *  {@link #INFO_FORCED}.</p>
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public class LogLevel extends Level
{
        /*-----------*\
    ====** Constants **==============================================
        \*-----------*/
    /**
     *  {@code ALL} indicates that all messages should be logged.
     * 
     *  @see Level#ALL
     */
    public static final LogLevel ALL = (LogLevel) Level.ALL;

    /**
     *  {@code CONFIG} is a message level for static configuration 
     *  messages.
     *
     *  @see Level#CONFIG
     */
    public static final LogLevel CONFIG = (LogLevel) Level.CONFIG;

    /**
     *  {@code FINE} is a message level providing tracing
     *  information.
     *  
     *  @see Level#FINE
     */
    public static final LogLevel FINE = (LogLevel) Level.FINE;

    /**
     *  {@code FINER} indicates a fairly detailed tracing message.
     *
     *  @see Level#FINER
     */
    public static final LogLevel FINER = (LogLevel) Level.FINER;

    /**
     *  {@code FINEST} indicates a highly detailed tracing message.
     *
     *  @see Level#FINEST
     */
    public static final LogLevel FINEST = (LogLevel) Level.FINEST;

    /**
     *  {@code INFO} is a message level for informational messages.
     *
     *  @see Level#INFO
     */
    public static final LogLevel INFO = (LogLevel) Level.INFO;

    /**
     *  {@code OFF} is a special level that can be used to turn off 
     *  logging.
     *
     * @see Level#OFF
     */
    public static final LogLevel OFF = (LogLevel) Level.OFF;

    /**
     *  {@code SEVERE} is a message level indicating a serious
     *  failure.
     *
     *  @see Level#SEVERE
     */
    public static final LogLevel SEVERE = (LogLevel) Level.SEVERE;

    /**
     *  {@code WARNING} is a message level indicating a potential
     *  problem.
     *  
     *  @see Level#WARNING
     */
    public static final LogLevel WARNING = (LogLevel) Level.WARNING;

        /*------------------------*\
    ====** Static Initialisations **=================================
        \*------------------------*/
    /**
     *  {@code DEBUG} is the alias for
     *  {@link #FINE}.
     */
    public static final LogLevel DEBUG = new LogLevel( "DEBUG", FINE );

    /**
     *  {@code ERROR} is the alias for
     *  {@link #SEVERE}.
     */
    public static final LogLevel ERROR = new LogLevel( "ERROR", SEVERE );

    /**
     *  <p>{@summary {@code INFO_FORCED} is the log level for 
     *  informational messages that have to be logged independently
     *  from any logger configuration, under all circumstances.} It
     *  will be initialized to
     *  {@link Integer#MAX_VALUE}
     *  {@code - 1}. This means that it can be still switched off
     *  by setting a logger's log level to
     *  {@link #OFF}.
     */
    public static final LogLevel INFO_FORCED = new LogLevel( "INFO_FORCED", Integer.MAX_VALUE - 1 );

    /**
     *  {@code TRACE} is the alias for
     *  {@link #FINEST}.
     */
    public static final LogLevel TRACE = new LogLevel( "TRACE", FINEST );

        /*--------------*\
    ====** Constructors **===========================================
        \*--------------*/
    /**
     *  Creates a named LogLevel with a given integer value.
     *
     *  @param  name    The name of the LogLevel.
     *  @param  value   An integer value for the LogLevel.
     */
    protected LogLevel( final String name, final int value )
    {
        super( requireNotBlankArgument( name, "name" ), value );
    }   //  LogLevel()

    /**
     *  Creates a named LogLevel as an alias of the given level.
     *
     *  @param  name    The name of the LogLevel.
     *  @param  level   The log level to rename.
     */
    protected LogLevel( final String name, final Level level )
    {
        super( requireNotBlankArgument( name, "name" ), requireNonNullArgument( level, "level" ).intValue() );
    }   //  LogLevel()
}
//  class LogLevel

/*
 *  End of File
 */
