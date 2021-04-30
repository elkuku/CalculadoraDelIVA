package org.elkuku.calculadoradeliva;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import java.text.DecimalFormat;

public class FirstFragment extends Fragment {

    private EditText edtTotal;
    private TextView txtSinIVA;
    private TextView txtIVA;
    private SharedPreferences sharedPref;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPref =
                PreferenceManager.getDefaultSharedPreferences(super.requireContext());

        Button btnClear = (Button) view.findViewById(R.id.btn_clear);

        edtTotal = (EditText) view.findViewById(R.id.edtTotal);
        txtSinIVA = (TextView) view.findViewById(R.id.txtSinIva);
        txtIVA = (TextView) view.findViewById(R.id.txtIva);
        TextView textIVA = (TextView) view.findViewById(R.id.textView2);

        btnClear.setOnClickListener(v -> {
            edtTotal.setText("");
            txtSinIVA.setText("0.00");
            txtIVA.setText("0.00");
        });

        TextWatcher textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateResult(view);
            }
        };

        edtTotal.addTextChangedListener(textWatcher);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        String ivaPref = sharedPref.getString(SettingsActivity.KEY_PREF_IVA_VALUE, "6");

        textIVA.setText("IVA " + ivaPref + " %");
    }

    private void calculateResult(View view) throws NumberFormatException {
        double sinIVA, IVA, total;

        txtSinIVA = (TextView) view.findViewById(R.id.txtSinIva);
        txtIVA = (TextView) view.findViewById(R.id.txtIva);

        Editable edit = edtTotal.getText();

        if (edit.toString().equals(""))
            return;

        try {
            total = Double.parseDouble(edit.toString());
        } catch (NumberFormatException e) {
            Toast.makeText(super.requireContext(), "Numero invalido",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String ivaPref = sharedPref.getString
                (SettingsActivity.KEY_PREF_IVA_VALUE, "6");

        float ivaValue = 1 + Float.parseFloat(ivaPref) / 100;

        sinIVA = total / ivaValue;
        IVA = total - sinIVA;

        txtSinIVA.setText(new DecimalFormat("#,###,##0.00").format(sinIVA));
        txtIVA.setText(new DecimalFormat("#,###,##0.00").format(IVA));
    }
}