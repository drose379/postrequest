package dylanrose60.postrequest;


import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class EmptyFieldException extends Exception {

    public ArrayList errorArray = null;

    public EmptyFieldException(ArrayList<View> array) {
        errorArray = array;
    }

    public ArrayList getErrors() {
        return errorArray;
    }

}
