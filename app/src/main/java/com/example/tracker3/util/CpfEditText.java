package com.example.tracker3.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;

public class CpfEditText extends androidx.appcompat.widget.AppCompatEditText {
    private boolean isUpdating;

    /*
     * Maps the cursor position from phone number to masked number... 12345678912
     * => xxx.xxx.xxx-xx
     */
    private final int[] positioning = { 0, 1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14 };

    public CpfEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();

    }

    public CpfEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();

    }

    public CpfEditText(Context context) {
        super(context);
        initialize();

    }

    private void initialize() {

        final int maxNumberLength = 11;
        this.setKeyListener(keylistenerNumber);

        this.setText("     -   ");
        this.setSelection(1);

        this.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String current = s.toString();

                /*
                 * Ok, here is the trick... calling setText below will recurse
                 * to this function, so we set a flag that we are actually
                 * updating the text, so we don't need to reprocess it...
                 */
                if (isUpdating) {
                    isUpdating = false;
                    return;

                }

                /* Strip any non numeric digit from the String... */
                String number = current.replaceAll("[^0-9]*", "");
                if (number.length() > 11)
                    number = number.substring(0, 11);
                int length = number.length();

                /* Pad the number to 10 characters... */
                String paddedNumber = padNumber(number, maxNumberLength);

                /* Split phone number into parts... */
                String part1 = paddedNumber.substring(0, 3);
                String part2 = paddedNumber.substring(3, 6);
                String part3 = paddedNumber.substring(6, 9);
                String part4 = paddedNumber.substring(9, 11);

                /* build the masked phone number... */
                String cpf = part1 + "." + part2 + "." + part3 + "-" + part4;

                /*
                 * Set the update flag, so the recurring call to
                 * afterTextChanged won't do nothing...
                 */
                isUpdating = true;
                CpfEditText.this.setText(cpf);

                CpfEditText.this.setSelection(positioning[length]);

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
    }

    protected String padNumber(String number, int maxLength) {
        String padded = new String(number);
        for (int i = 0; i < maxLength - number.length(); i++)
            padded += " ";
        return padded;

    }

    private final KeylistenerNumber keylistenerNumber = new KeylistenerNumber();

    private static class KeylistenerNumber extends NumberKeyListener {

        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;

        }

        @Override
        protected char[] getAcceptedChars() {
            return new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9' };

        }
    }
}