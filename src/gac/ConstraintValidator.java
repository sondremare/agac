package gac;

import org.python.core.PyInteger;
import org.python.core.PyObject;

import java.util.ArrayList;

public class ConstraintValidator {
    private PyObject pyObject;

    public ConstraintValidator(PyObject pyObject) {
        this.pyObject = pyObject;
    }

    public boolean check(int[] args) {
        PyInteger[] pyArgs = new PyInteger[args.length];
        for (int i = 0; i < args.length; i++) {
            pyArgs[i] = new PyInteger(args[i]);
        };
        return (Boolean) this.pyObject.__call__(pyArgs).__tojava__(Boolean.class);
    }
}
