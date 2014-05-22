import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Runs the REPL or a Scheme file.
 */
public class Main {
	
	/*
	 * Run the REPL if there are no command line arguments.
	 */
	public static void repl() {
		System.out.println("Scheme Interpreter v3 by Steely Morneau");
		int counter = 1;
		Scanner s = new Scanner(System.in);
		String input;
		System.out.print("\n" + counter + " ]=> ");
		Environment env = null;
		try {
			do {
				counter++;
				input = s.nextLine();
				env = compute(input, env, true);
				if (env == null) {
					counter = 1;
				}
				System.out.print("\n" + counter + " ]=> ");
			} while (s.hasNext());	
		} catch (NoSuchElementException e) {
			System.out.println();
		}
		
	}
	
	/*
	 * Run a .scm file.
	 */
	public static void scm(String filename) {
		if (!filename.endsWith(".scm")) {
			System.out.println("File must have extension '.scm'");
			return;
		}
		File file = new File(filename);
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    Environment env = null;

		    while ((text = reader.readLine()) != null) {
		    	env = compute(text, env, false);
		    }
		} catch (FileNotFoundException e) {
		    System.out.println(filename + " not found.");
		} catch (IOException e) {
		    System.out.println("IOException");
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    	System.out.println("IOException");
		    }
		}
	}
	
	public static Environment compute(String expr, Environment env, boolean repl) {
        TokenScanner scanner;
        LinkedList<Token> tokens;
        ParserTree parserTree;
        Tree.SexprList sexprs = null;
        try {
        	scanner = new TokenScanner(expr);
            tokens = scanner.scan();
        } catch(ScanError e) {
            System.out.println(e);
            return null;
        }

        parserTree = new ParserTree(tokens);

        try {
            sexprs = parserTree.parse();
        } catch(ParseError e) {
            System.out.println(e);
            return null;
        }

        EvalTree evalTree = new EvalTree(sexprs);
        try {
			return evalTree.eval(env, repl);
		} catch (EvalError e) {
			System.out.println(e);
			return null;
		}
	}

    public static void main(String[] args) {
    	if (args.length == 1) {
    		scm(args[0]);
    	} else {
    		repl();
    	}
    }
}
