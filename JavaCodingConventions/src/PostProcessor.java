package util;

import static java.util.Objects.requireNonNull;

/**
 *  Use this class to implement an uncoditional post-processing
 *  feature utilising try-with-resources.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
public class PostProcessor implements AutoCloseable
{
        /*------------*\
    ====** Attributes **=============================================
        \*------------*/
    /**
     *  The action.
     */
    private final Runnable m_Action;
    
        /*--------------*\
    ====** Constructors **===========================================
        \*--------------*/
    /**
     *  Creates a new {@code PostProcessor} object.
     *
     *  @param  action  The action that has to executed.
     */
    public PostProcessor( final Runnable action )
    {
        m_Action = requireNonNull( action );
    }   //  PostProcessor()
    
        /*---------*\
    ====** Methods **================================================
        \*---------*/
    /**
     *  Calls the
     *  {@link Runnable#run() run()}
     *  method of the
     *  {@linkplain #m_Action action}.
     *
     *  @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception { m_Action.run(); }
}
//  class PostProcessor

