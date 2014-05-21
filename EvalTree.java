import java.util.ArrayList;
import java.util.HashMap;

public class EvalTree {

    Tree.SexprList fullTree;

    public EvalTree(Tree.SexprList tree) {
        this.fullTree = tree;
    }
    
    public Environment eval(Environment env, boolean repl) throws EvalError {
    	if (env == null) {    		
    		env = new Environment();
    	}
    	for (Tree.Sexpr s: fullTree.sexprList) {
    		SValue.Base val = evalSexpr(s, env);
    		if (repl && val != null) {
    			System.out.println(";Value: " + val);   
    		}
    	}
    	return env;
    }
    
    public SValue.Base evalSexpr(Tree.Sexpr tree, Environment env) throws EvalError {
    	if (tree instanceof Tree.SexprList) {        		
    		return evalSexprList((Tree.SexprList)tree, env);
    	} else if (tree instanceof Tree.SexprNumber) {
    		return new SValue.Number(((Tree.SexprNumber) tree).value);
    	} else if (tree instanceof Tree.SexprBoolean) {
    		return new SValue.Boolean(((Tree.SexprBoolean) tree).value);
    	} else if (tree instanceof Tree.SexprIdent) {
    		SValue.Base val = env.lookup(((Tree.SexprIdent) tree).value);
    		if (val == null) {
    			System.out.println("ENV: " + env);
    			throw new EvalError(tree);
    		}
    		return val;
    	} else {
    		throw new EvalError(tree);
    	}
    }

    public SValue.Base evalSexprList(Tree.SexprList tree, Environment env) throws EvalError {
    	SValue.Base first = env.lookup(((Tree.SexprIdent)tree.sexprList.get(0)).value);
    	if (first instanceof SValue.Function) {
    		if (((SValue.Function) first).value == "+") {
        		return evalPlus(tree, env);
    		} else if (((SValue.Function) first).value == "-") { 
        		return evalMinus(tree, env);
    		} else if (((SValue.Function) first).value == "*") {
        		return evalMult(tree, env);
    		} else if (((SValue.Function) first).value == "/") {
        		return evalDiv(tree, env);
    		} else if (((SValue.Function) first).value == "<") {
        		return evalLT(tree, env);
    		} else if (((SValue.Function) first).value == ">") {
        		return evalGT(tree, env);
    		} else if (((SValue.Function) first).value == "<=") {
        		return evalLTE(tree, env);
    		} else if (((SValue.Function) first).value == ">=") {
        		return evalGTE(tree, env);
    		} else if (((SValue.Function) first).value == "not") {
        		return evalNot(tree, env);
    		} else if (((SValue.Function) first).value == "if") {
        		return evalIf(tree, env);
    		} else if (((SValue.Function) first).value == "define") {
        		return evalDefine(tree, env);
    		} else if (((SValue.Function) first).value == "equal?" || 
    				((SValue.Function) first).value == "=") {
        		return evalEqual(tree, env);
    		} else if (((SValue.Function) first).value == "lambda") {
        		return evalLambda(tree, env);
    		} else if (((SValue.Function) first).value == "list") {
        		return evalList(tree, env);
    		} else if (((SValue.Function) first).value == "quote") {
        		return evalQuote(tree);
    		} else if (((SValue.Function) first).value == "car") {
        		return evalCar(tree, env);
    		} else if (((SValue.Function) first).value == "cdr") {
        		return evalCdr(tree, env);
    		} else if (((SValue.Function) first).value == "null?") {
        		return evalNull(tree, env);
    		} else if (((SValue.Function) first).value == "display") {
        		evalDisplay(tree, env);
    		} else if (((SValue.Function) first).value == "set!") {
        		return evalSet(tree, env);
    		} else if (((SValue.Function) first).value == "list?") {
        		return evalIsList(tree, env);
    		} else if (((SValue.Function) first).value == "length") {
        		return evalLength(tree, env);
    		} else if (((SValue.Function) first).value == "begin") {
        		return evalBegin(tree, env);
    		} else {
    			System.out.println("Function not supported.");
    		}
    	} else if (first instanceof SValue.Closure) {
    		return evalClosure(tree, env);
    	} else {
    		System.out.println("?" + ((Tree.SexprIdent)tree.sexprList.get(0)).value);
    	}
    	return null;
    }
    
	public SValue.Number evalPlus(Tree.SexprList tree, Environment env) throws EvalError {
    	int sum = 0;
    	for (int i = 1; i < tree.sexprList.size(); i++) {
    		sum += ((SValue.Number) evalSexpr(tree.sexprList.get(i), env)).value;
    	}
    	return new SValue.Number(sum);
    }
    
    public SValue.Number evalMinus(Tree.SexprList tree, Environment env) throws EvalError {
    	int difference = ((SValue.Number) evalSexpr(tree.sexprList.get(1), env)).value;
    	for (int i = 2; i < tree.sexprList.size(); i++) {
			difference -= ((SValue.Number) evalSexpr(tree.sexprList.get(i), env)).value;;
    	}
    	return new SValue.Number(difference);
    }
    
    public SValue.Number evalMult(Tree.SexprList tree, Environment env) throws EvalError {
    	int product = 1;
    	for (int i = 1; i < tree.sexprList.size(); i++) {
    		product *= ((SValue.Number) evalSexpr(tree.sexprList.get(i), env)).value;
    	}
    	return new SValue.Number(product);
    }
  
    public SValue.Number evalDiv(Tree.SexprList tree, Environment env) throws EvalError {
    	int quotient = ((SValue.Number) evalSexpr(tree.sexprList.get(1), env)).value;
    	for (int i = 2; i < tree.sexprList.size(); i++) {
    		quotient /= ((SValue.Number) evalSexpr(tree.sexprList.get(i), env)).value;
    	}
    	return new SValue.Number(quotient);
    }
    
    public SValue.Boolean evalLT(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	
    	SValue.Base a = evalSexpr(tree.sexprList.get(1), env);
    	SValue.Base b = evalSexpr(tree.sexprList.get(2), env);
    	
    	if(!(a instanceof SValue.Number) || !(b instanceof SValue.Number)) {
    		throw new EvalError(tree);	
    	}
    	
    	return new SValue.Boolean(((SValue.Number)a).value < ((SValue.Number)b).value);
	}
    
    public SValue.Boolean evalGT(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	
    	SValue.Base a = evalSexpr(tree.sexprList.get(1), env);
    	SValue.Base b = evalSexpr(tree.sexprList.get(2), env);
    	
    	if(!(a instanceof SValue.Number) || !(b instanceof SValue.Number)) {
    		throw new EvalError(tree);	
    	}
    	
    	return new SValue.Boolean(((SValue.Number)a).value > ((SValue.Number)b).value);
	}
    
    public SValue.Boolean evalLTE(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	
    	SValue.Base a = evalSexpr(tree.sexprList.get(1), env);
    	SValue.Base b = evalSexpr(tree.sexprList.get(2), env);
    	
    	if(!(a instanceof SValue.Number) || !(b instanceof SValue.Number)) {
    		throw new EvalError(tree);	
    	}
    	
    	return new SValue.Boolean(((SValue.Number)a).value <= ((SValue.Number)b).value);
	}
    
    public SValue.Boolean evalGTE(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	
    	SValue.Base a = evalSexpr(tree.sexprList.get(1), env);
    	SValue.Base b = evalSexpr(tree.sexprList.get(2), env);
    	
    	if(!(a instanceof SValue.Number) || !(b instanceof SValue.Number)) {
    		throw new EvalError(tree);	
    	}
    	
    	return new SValue.Boolean(((SValue.Number)a).value >= ((SValue.Number)b).value);
	}
    
    public SValue.Boolean evalNot(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	
    	boolean b = true;
    	
    	SValue.Base test = evalSexpr(tree.sexprList.get(1), env);
    	if (test instanceof SValue.Boolean) {
        	b = ((SValue.Boolean) test).value;
    	} 
    	
    	return new SValue.Boolean(!b);
    }
    
    public SValue.Base evalIf(Tree.SexprList tree, Environment env) throws EvalError {
//    	(if test conseq alt)
    	
    	boolean b = true;
    	
    	if (tree.sexprList.size() != 4) {
    		throw new EvalError(tree);
    	}
    	
    	SValue.Base test = evalSexpr(tree.sexprList.get(1), env);
    	
    	if (test instanceof SValue.Boolean) {
        	b = ((SValue.Boolean) test).value;
    	} 
    	
    	if (b) {
    		return evalSexpr(tree.sexprList.get(2), env); 		
    	} else {
    		return evalSexpr(tree.sexprList.get(3), env);	
    	}
    }
    
    public SValue.Base evalClosure(Tree.SexprList tree, Environment oldEnv) throws EvalError {
    	// (proc exp...)
     	Environment env = new Environment(oldEnv);
    	
    	SValue.Base closure = evalSexpr(tree.sexprList.get(0), env);
    	
    	if (!(closure instanceof SValue.Closure)) {
    		throw new EvalError(tree.sexprList.get(0));
    	}
    	
    	env.env = (HashMap<String, SValue.Base>) ((SValue.Closure) closure).env.env.clone();
    	
    	if (tree.sexprList.size() - 1 != ((SValue.Closure) closure).params.size()) {
    		System.out.println("Incorrect number of parameters for " + ((Tree.SexprIdent)tree.sexprList.get(0)).value);
    		throw new EvalError(tree);
    	}
    	
    	for (int i = 0; i < ((SValue.Closure) closure).params.size(); i++) {
    		SValue.Base val = evalSexpr(tree.sexprList.get(i + 1), env);
    		String param = ((SValue.Closure) closure).params.get(i);
    		env.bind(param, val);
    	}
    	
    	return evalSexpr(((SValue.Closure)closure).body, env);
    }
    
    
    public SValue.Closure evalLambda(Tree.SexprList tree, Environment env) throws EvalError {
    	// (lambda (var*) exp)
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	Tree.Sexpr params = tree.sexprList.get(1);
    	if (!(params instanceof Tree.SexprList)) {
    		throw new EvalError(params);
    	}
    	ArrayList<String> paramsList = new ArrayList<String>();
    	for(Tree.Sexpr param: ((Tree.SexprList)params).sexprList) {
    		paramsList.add( ((Tree.SexprIdent)param).value );
    	}
    	Tree.Sexpr body = tree.sexprList.get(2);
    	
    	return new SValue.Closure(env, paramsList, body);
    }
    
    
    public SValue.Name evalDefine(Tree.SexprList tree, Environment env) throws EvalError {
    	// (define var exp)
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	Tree.Sexpr sexpr = tree.sexprList.get(1);
    	if (!(sexpr instanceof Tree.SexprIdent)) {
    		throw new EvalError(sexpr);
    	}
    	String variable = ((Tree.SexprIdent)sexpr).value;
    	env.bind(variable, evalSexpr(tree.sexprList.get(2), env));
    	return new SValue.Name(variable);
    }
    
    
    public SValue.Base evalEqual(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	SValue.Base a = evalSexpr(tree.sexprList.get(1), env);
    	SValue.Base b = evalSexpr(tree.sexprList.get(2), env);
    	
    	if (a instanceof SValue.Number) {
    		if (b instanceof SValue.Number) {
    			if (((SValue.Number)a).value == ((SValue.Number)b).value) {
    				return new SValue.Boolean(true);
    			} 
    		} 
    	} else if (a instanceof SValue.Boolean) {
    		if (b instanceof SValue.Boolean) {
    			if (((SValue.Boolean)a).value == ((SValue.Boolean)b).value) {
    				return new SValue.Boolean(true);
    			} 
    		} 
    	} else if (a instanceof SValue.Name) {
    		if (b instanceof SValue.Name) {
    			if (((SValue.Name)a).value.compareTo(((SValue.Name)b).value) == 0) {
    				return new SValue.Boolean(true);
    			} 
    		} 
    	} else if (a instanceof SValue.Function) {
    		if (b instanceof SValue.Function) {
    			if (((SValue.Function)a).value.compareTo(((SValue.Function)b).value) == 0) {
    				return new SValue.Boolean(true);
    			} 
    		} 
    	} else if (a instanceof SValue.Closure) {
    		if (b instanceof SValue.Closure) {
    			try {
    				String valA = ((Tree.SexprIdent) tree.sexprList.get(1)).value;
    				String valB = ((Tree.SexprIdent) tree.sexprList.get(2)).value;
    				if (valA.compareTo(valB) == 0) {
    					return new SValue.Boolean(true);
    				}
    			} catch (Exception e) {}
    		} 
    	}
    	return new SValue.Boolean(false);
    }
    

    private SValue.Base quote(Tree.Sexpr sexpr) {
    	SValue.Base val = null;
		if (sexpr instanceof Tree.SexprNumber) {
    		val = new SValue.Number(((Tree.SexprNumber) sexpr).value);
    	} else if (sexpr instanceof Tree.SexprBoolean) {
    		val = new SValue.Boolean(((Tree.SexprBoolean) sexpr).value);
    	} else if (sexpr instanceof Tree.SexprIdent) {
    		val = new SValue.Name(((Tree.SexprIdent) sexpr).value);
    	} 
		return val;
    }
    
    public SValue.Base evalQuote(Tree.SexprList tree) throws EvalError {
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	
    	Tree.Sexpr sexpr = tree.sexprList.get(1);
    	if (sexpr instanceof Tree.SexprList) {
    		ArrayList<SValue.Base> vals = new ArrayList<SValue.Base>();
    		for (Tree.Sexpr s: ((Tree.SexprList)sexpr).sexprList) {
    			if (s instanceof Tree.SexprList) {
    				vals.add(evalQuote((Tree.SexprList) s));
    			} else {
        			vals.add(quote(s));
    			}
    		}
    		return new SValue.List(vals);
    	} else {
    		return quote(sexpr);
    	}
    }
    
    public SValue.Base evalList(Tree.SexprList tree, Environment env) throws EvalError {
    	ArrayList<SValue.Base> vals = new ArrayList<SValue.Base>();
    	for (int i = 1; i < ((Tree.SexprList)tree).sexprList.size(); i++) {
			if (tree.sexprList.get(i) instanceof Tree.SexprList) {
				vals.add(evalList((Tree.SexprList) tree.sexprList.get(i), env));
			} else {
    			vals.add(evalSexpr(tree.sexprList.get(i), env));
			}
		}
    	return new SValue.List(vals);
    }
    
    public SValue.Base evalCar(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	SValue.Base val = evalSexpr(tree.sexprList.get(1), env);
    	if (!(val instanceof SValue.List)) {
    		throw new EvalError(tree.sexprList.get(1));
    	}
    	return ((SValue.List) val).list.get(0);
    }
    
    public SValue.Base evalCdr(Tree.SexprList tree, Environment env) throws EvalError {
    	ArrayList<SValue.Base> vals = new ArrayList<SValue.Base>();
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	SValue.Base val = evalSexpr(tree.sexprList.get(1), env);
    	if (!(val instanceof SValue.List)) {
    		throw new EvalError(tree.sexprList.get(1));
    	}
    	vals.addAll(((SValue.List) val).list);
    	vals.remove(0);
    	return new SValue.List(vals);
    }

    public SValue.Boolean evalNull(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	SValue.Base val = evalSexpr(tree.sexprList.get(1), env);
    	if (!(val instanceof SValue.List)) {
    		throw new EvalError(tree.sexprList.get(1));
    	}
    	return new SValue.Boolean(((SValue.List)val).list.isEmpty());
    }

    public SValue.Base evalDisplay(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	SValue.Base val = evalSexpr(tree.sexprList.get(1), env);
    	System.out.println(val);
    	return val;
    }
    
    public SValue.Name evalSet(Tree.SexprList tree, Environment env) throws EvalError {
    	// (set! var exp)
    	if (tree.sexprList.size() != 3) {
    		throw new EvalError(tree);
    	}
    	Tree.Sexpr sexpr = tree.sexprList.get(1);
    	if (!(sexpr instanceof Tree.SexprIdent)) {
    		throw new EvalError(sexpr);
    	}
    	SValue.Base variable = env.lookup(((Tree.SexprIdent)sexpr).value);
    	if (variable == null) {
    		System.out.println("Variable must have been previously defined.");
    		throw new EvalError(sexpr);
    	}
    	env.bind(((Tree.SexprIdent)sexpr).value, evalSexpr(tree.sexprList.get(2), env));
    	return new SValue.Name(((Tree.SexprIdent)sexpr).value);
    }
    
    public SValue.Boolean evalIsList(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	Tree.Sexpr sexpr = tree.sexprList.get(1);
    	SValue.Base val = null;
    	if (sexpr instanceof Tree.SexprIdent) {
    		val = env.lookup(((Tree.SexprIdent)sexpr).value);
    	} else if (sexpr instanceof Tree.SexprList) {
    		val = evalSexpr(sexpr, env);
    	}
    	if (val instanceof SValue.List) {
    		return new SValue.Boolean(true);
    	} else {    		
    		return new SValue.Boolean(false);
    	}
    }

    public SValue.Number evalLength(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() != 2) {
    		throw new EvalError(tree);
    	}
    	Tree.Sexpr sexpr = tree.sexprList.get(1);
    	SValue.Base val = null;
    	if (sexpr instanceof Tree.SexprIdent) {
    		val = env.lookup(((Tree.SexprIdent)sexpr).value);
    	} else if (sexpr instanceof Tree.SexprList) {
    		val = evalSexpr(sexpr, env);
    	}
    	if (val instanceof SValue.List) {
    		int length = ((SValue.List)val).list.size();
    		return new SValue.Number(length);
    	} else {    	
    		System.out.println("'length' function expects list type.");
    		throw new EvalError(sexpr);
    	}
    }
    
    public SValue.Base evalBegin(Tree.SexprList tree, Environment env) throws EvalError {
    	if (tree.sexprList.size() < 2) {
    		throw new EvalError(tree);
    	}
    	SValue.Base result = null;
    	for (int i = 1; i < ((Tree.SexprList)tree).sexprList.size(); i++) {
    		result = evalSexpr(tree.sexprList.get(i), env);
    	}
    	return result;
    }
    
}
