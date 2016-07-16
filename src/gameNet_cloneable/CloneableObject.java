package gameNet_cloneable;



public abstract class CloneableObject extends Object implements Cloneable
{
	 public abstract  void myCloneMethod( Object obj);
	 public Object clone( )
	    {
	       try
	       {
	          CloneableObject object =  (CloneableObject)super.clone( );//Invocation of clone 
	                               //in the base class Object
	          object.myCloneMethod(object);
	          return object;
	       }
	       catch(CloneNotSupportedException e)
	       {//This should not happen.
	    	   System.out.println("CloneableObject.clone: CloneNotSupported error: "+e);
	    	   e.printStackTrace(System.out);
	          return null; //To keep the compiler happy.
	       }
	    }
}
class MyCloneableObject extends CloneableObject
{
	public void myCloneMethod( Object obj)
    {
       // take care of any other issues.
		// If your class only has primitives and Immutable like
		// String and Wrapper classes(Integer, Double, etc.), then there is 
		// nothing to do here.
    }

}
