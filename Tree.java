// Tree.java - a parse tree implementation for Scheme

import java.util.ArrayList;
import java.util.Collections;

public class Tree {
	
    abstract static class Node {
    }

    abstract static class Sexpr extends Node {
    }

    static class SexprList extends Sexpr {
        ArrayList<Sexpr> sexprList;

        public SexprList(ArrayList<Sexpr> sexprList) {
            this.sexprList = sexprList;
        }

        public SexprList(Sexpr... sexprs) {
            sexprList = new ArrayList<Sexpr>();
            Collections.addAll(sexprList, sexprs);
        }

        public String toString() {
            return "SexprList(" + sexprList.toString() + ")";
        }
    }

    static class SexprIdent extends Sexpr {
        String value;

        public SexprIdent(String value) {
            this.value = value;
        }

        public String toString() {
            return "SexprIdent(" + value + ")";
        }
    }

    static class SexprNumber extends Sexpr {
        int value;

        public SexprNumber(int value) {
            this.value = value;
        }

        public String toString() {
            return "SexprNumber(" + value + ")";
        }
    }

    static class SexprBoolean extends Sexpr {
        boolean value;

        public SexprBoolean(boolean value) {
            this.value = value;
        }

        public String toString() {
            return "SexprBoolean(" + Boolean.toString(value) + ")";
        }
    }
    
    public static void printTree(Node node) {
        System.out.println(node);
    }

    public static void main(String[] args) {
        Sexpr t1 = new SexprNumber(1);
        System.out.println(t1);

        Sexpr t2 = new SexprList(new SexprIdent("+"));
        System.out.println(t2);
        
        Sexpr t3 = new SexprList(t1, new SexprIdent("*"));
        System.out.println(t3);

    }

}
