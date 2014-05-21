import java.util.HashMap;

public class Environment {
	Environment prev;
	HashMap<String, SValue.Base> env;

	public Environment() {
		prev = null;
		env = new HashMap<String, SValue.Base>();
		// built-in functions
		this.env.put("+", new SValue.Function("+"));
		this.env.put("-", new SValue.Function("-"));
		this.env.put("*", new SValue.Function("*"));
		this.env.put("/", new SValue.Function("/"));
		this.env.put("<", new SValue.Function("<"));
		this.env.put(">", new SValue.Function(">"));
		this.env.put("<=", new SValue.Function("<="));
		this.env.put(">=", new SValue.Function(">="));
		this.env.put("=", new SValue.Function("="));
		this.env.put("not", new SValue.Function("not"));
		this.env.put("define", new SValue.Function("define"));
		this.env.put("equal?", new SValue.Function("equal?"));
		this.env.put("lambda", new SValue.Function("lambda"));
		this.env.put("if", new SValue.Function("if"));
		this.env.put("list", new SValue.Function("list"));
		this.env.put("quote", new SValue.Function("quote"));
		this.env.put("car", new SValue.Function("car"));
		this.env.put("cdr", new SValue.Function("cdr"));
		this.env.put("null?", new SValue.Function("null?"));
		this.env.put("display", new SValue.Function("display"));
		this.env.put("set!", new SValue.Function("set!"));
		this.env.put("list?", new SValue.Function("list?"));
		this.env.put("length", new SValue.Function("length"));
		this.env.put("begin", new SValue.Function("begin"));
	}
	
	public Environment(Environment prev) {
		this.prev = prev;
		env = new HashMap<String, SValue.Base>();
	}
	
    public void bind(String name, SValue.Base value) {
        env.put(name, value);
    }

	public SValue.Base lookup(String name) {
		SValue.Base value = this.env.get(name);
		if( (value == null) && (prev != null) ) {
			return prev.lookup(name);
		}

		return value;
	}
	
    public String toString() {
        return env.keySet() + " -> " + prev;
    }

}
