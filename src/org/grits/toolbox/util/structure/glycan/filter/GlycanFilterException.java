package org.grits.toolbox.util.structure.glycan.filter;

public class GlycanFilterException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public GlycanFilterException(String a_string)
    {
        super(a_string);
    }

    public GlycanFilterException(String a_string, Exception a_exception)
    {
        super(a_string, a_exception);
    }

}
