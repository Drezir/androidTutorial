package app.android.adam.androidapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Locale;

import app.android.adam.androidapp.interfaces.FragmentDataExtractor;

public class Popup extends DialogFragment {

    private FragmentDataExtractor<String> dataExtractor;

    public void setDataExtractor(FragmentDataExtractor<String> dataExtractor) {
        this.dataExtractor = dataExtractor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popup, null);
        final DatePicker datePicker = view.findViewById(R.id.popupDatePicker);
        Button closeButton = view.findViewById(R.id.popupCloseButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = String.format(Locale.ENGLISH, "%d. %d. %d",
                        datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
                if (dataExtractor != null) {
                    dataExtractor.provideData(date);
                }
                Popup.this.dismiss();
            }
        });

        return view;
    }
}
