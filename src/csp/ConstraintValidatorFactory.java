package csp;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class ConstraintValidatorFactory {
    public static PythonInterpreter pythonInterpreter = new PythonInterpreter();

    public static ConstraintValidator createConstraint(String[] variableNames, String constraintExpression) {
        String args = String.join(", ", variableNames);
        String lambdaExpression = "(lambda " + args + ": " + constraintExpression + ")";
        PyObject pyObject = pythonInterpreter.eval(lambdaExpression);
        return new ConstraintValidator(pyObject);
    }



}
