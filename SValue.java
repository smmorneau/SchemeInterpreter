import java.util.ArrayList;

public class SValue {

    abstract static class Base {}
    
    static class Boolean extends Base {
    	boolean value;
    	String sval;
    	
    	public Boolean(boolean value) {
    		this.value = value;
    		if (value) {
    			sval = "#t";
    		} else {
    			sval = "#f";
    		}
    	}
    	
        public String toString() {
            return this.sval;
        }
    }
    
    static class Closure extends Base {
    	Environment env;
        ArrayList<String> params;
        Tree.Sexpr body;
        
        public Closure(Environment env, ArrayList<String> params, Tree.Sexpr body) {
        	this.env = env;
        	this.params = params;
        	this.body = body;
        }
        
        public String toString() {
            return "Closure <" + params + " " + body + ">" ;
        }
        
    }
    
    static class Function extends Base {
    	String value;
    	
    	public Function(String value) {
    		this.value = value;
    	}
    }
    
    static class List extends Base {
    	ArrayList<SValue.Base> list;
    	
    	public List(ArrayList<SValue.Base> list) {
    		this.list = list;
    	}
    	
        public String toString() {
        	String l = "(";
        	for (SValue.Base val: list) {
        		l += val + " ";
        	}
        	if (l.length() > 1) {        		
        		l = l.substring(0, l.length() - 1);
        	}
        	l += ")";
            return l;
        }
    }
    
    static class Name extends Base {
    	String value;
    	
    	public Name(String value) {
    		this.value = value;
    	}
    	
        public String toString() {
            return this.value;
        }
    }
    
    static class Number extends Base {
    	int value;
    	
    	public Number(int value) {
    		this.value = value;
    	}
    	
        public String toString() {
            return "" + this.value;
        }
    }
    
    static class Quote extends Base {
    	Base value;
    	
    	public Quote(Base value) {
    		this.value = value;
    	}
    	
        public String toString() {
            return "" + this.value;
        }
    }

}
