package com.dts.bookies.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public abstract class TextValidator implements TextWatcher {

    private EditText editText;

    public TextValidator(EditText editText) {
        this.editText = editText;
    }

    public abstract void validate(EditText editText, String text);

    @Override
    public void afterTextChanged(Editable editable) {
        String text = this.editText.getText().toString();
        validate(this.editText, text);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        do nothing.
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        do nothing.
    }

    public boolean isEmpty() {
        return this.editText.getText().toString().equals("");
    }

}
