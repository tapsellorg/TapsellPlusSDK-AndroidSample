package ir.tapsell.plussample.android.util;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import ir.tapsell.plussample.android.model.Zone;

/**
 * Fills a {@link Spinner} with zone names and resolves the currently
 * selected zone.
 */
public final class ZoneSpinner {

    private ZoneSpinner() {
    }

    public static void populate(Spinner spinner, List<Zone> zones) {
        String[] names = new String[zones.size()];
        for (int i = 0; i < zones.size(); i++) {
            names[i] = zones.get(i).name;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(), android.R.layout.simple_spinner_dropdown_item, names);
        spinner.setAdapter(adapter);
    }

    /** @return the zone currently selected, or null when the spinner is empty. */
    public static Zone selectedZone(Spinner spinner, List<Zone> zones) {
        int position = spinner.getSelectedItemPosition();
        if (position < 0 || position >= zones.size()) {
            return null;
        }
        return zones.get(position);
    }
}
