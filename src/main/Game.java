package main;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;

import java.util.Map;

public class Game {
    private int aInt, bInt, cInt, dInt;

    public Game() {
        start();
        System.out.println(showSolution());
    }

    private void start() {
        Query q = new Query("consult", new Term[]{new Atom("src/data/prolog/engine.pl")});
        System.out.println("consult " + (q.hasSolution() ? "succeeded" : "failed"));
        q.close();
        q = new Query("create_secret(4, [A, B, C, D])");
        Map<String, Term> solution = q.nextSolution();
        aInt = (solution.get("A")).intValue();
        bInt = (solution.get("B")).intValue();
        cInt = (solution.get("C")).intValue();
        dInt = (solution.get("D")).intValue();
        q.close();
    }

    int[] getSolution(int i) {
        String qStr = String.format("evaluate([%s,%s,%s,%s], %s, Bulls, Cows)", aInt, bInt, cInt, dInt, i);
        Query q = new Query(qStr);
        if (!q.hasSolution())
            return null;
        Map<String, Term> res = q.nextSolution();
        int bulls = res.get("Bulls").intValue();
        int cows = res.get("Cows").intValue();
        return new int[]{bulls, cows};
    }

    public String showSolution() {
        return String.format("%s%s%s%s", aInt, bInt, cInt, dInt);
    }
}
