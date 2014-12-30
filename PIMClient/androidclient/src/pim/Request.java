package pim;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    String name ; //enum? // well, this should not be transient. Transient variables won't be seriazlied.
    //ArrayList<Parameter> parameterList = null;
    Object parameterList[] = null ;
    public Request (String name) {
        this.name = name;
    }
    public Request (String name, Object... params) {
        this.name = name;
        this.parameterList = params;
    }
    public String getName()
    {
    	return this.name ;
    }
    public Object[] getParameterList()
    {
    	return parameterList ;
    }
    /*
    public Object findValue(String inputname)
    {
        for(int i = 0; i < this.parameterList.size(); i ++)
        {
            if(parameterList.get(i).name.equals(inputname))
                return parameterList.get(i).value ;
        }
        return null ;
    }
    */
}

/*
class CreateRequest extends Request implements Serializable
{
    String reqname = "" ;
    transient PJ data = null ;
    public CreateRequest(String reqname, PJ data)
    {
        this.reqname = reqname ;
        this.data = data ;
    }
    String getreqname()
    {
        return this.reqname ;
    }
    PJ getData()
    {
        return this.data ;
    }
}
class UpdateRequest extends Request implements Serializable
{
    Object data ;
    int[] address = new int[8] ;
    public UpdateRequest(String reqname, int[] address, Object data)
    {
        this.reqname = reqname ;
        this.data = data ;
        this.address = address ;
    }
    String getreqname()
    {
        return this.reqname ;
    }
    Object getData()
    {
        return this.data ;
    }
    int[] getAddress()
    {
        return this.address ;
    }
}
class ReadRequest extends Request implements Serializable
{
    int[] address = new int[8];
    public ReadRequest(String reqname, int[] address)
    {
        this.reqname = reqname ;
        this.address = address ;
    }
    String getreqname()
    {
        return this.reqname ;
    }
    int[] getAddress()
    {
        return this.address ;
    }
}
*/

