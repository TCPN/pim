package pim;

import java.lang.reflect.Type;
import java.io.Serializable;

public class Parameter<T> implements Serializable {
    String name;
    T value;
    public Parameter(String name, T value) {
        this.name = name;
        this.value = value;
    }
    public Object findValue(String inputname)
    {
    	if(this.name.equals(inputname))
    		return this.value ;
    	else
    		return null ;
    	
    }
}