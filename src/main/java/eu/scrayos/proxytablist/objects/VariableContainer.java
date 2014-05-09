package eu.scrayos.proxytablist.objects;

import eu.scrayos.proxytablist.api.Variable;

import java.util.ArrayList;
import java.util.regex.MatchResult;

public class VariableContainer {
    private final ArrayList<Variable> variable = new ArrayList<>();
    private final ArrayList<MatchResult> foundStr = new ArrayList<>();

    public ArrayList<Variable> getVariable() {
        return variable;
    }

    public ArrayList<MatchResult> getFoundStr() {
        return foundStr;
    }

    public void addVariableMatch(Variable variable1, MatchResult found) {
        variable.add(variable1);
        foundStr.add(found);
    }
}
