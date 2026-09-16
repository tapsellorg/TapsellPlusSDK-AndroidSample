package ir.tapsell.plussample.android.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ir.tapsell.plussample.android.DefaultConfigs;
import ir.tapsell.plussample.android.model.Zone;
import ir.tapsell.plussample.android.model.ZoneType;

/**
 * App + zone config source for the sample screens.
 * <p>
 * Reads {@code assets/tapsell.json} (same format as the MediationAndroidSample)
 * and exposes the app secret key and the zones matching a {@link ZoneType}.
 */
public final class ConfigProvider {

    private static final String TAG = "ConfigProvider";
    private static final String ZONES_FILE_NAME = "tapsell.json";

    private static List<Zone> cachedZones;
    private static String cachedSecretKey;

    /** null = not checked yet, TRUE = tapsell.json loaded, FALSE = tapsell.json absent. */
    private static Boolean jsonAvailable;

    private ConfigProvider() {
    }

    /** @return the secret key for {@code TapsellPlus.initialize(...)}. */
    public static String getSecretKey(Context context) {
        load(context);
        return Boolean.TRUE.equals(jsonAvailable) ? cachedSecretKey : DefaultConfigs.TAPSELL_KEY;
    }

    public static List<Zone> zonesFor(Context context, ZoneType type) {
        load(context);
        List<Zone> all = Boolean.TRUE.equals(jsonAvailable) ? cachedZones : fallbackZones();
        List<Zone> result = new ArrayList<>();
        for (Zone zone : all) {
            if (zone.type == type) {
                result.add(zone);
            }
        }
        return result;
    }

    private static List<Zone> fallbackZones() {
        List<Zone> zones = new ArrayList<>();
        zones.add(new Zone("Default Zone", ZoneType.REWARDED, DefaultConfigs.TAPSELL_REWARDED_VIDEO));
        zones.add(new Zone("Default Zone", ZoneType.INTERSTITIAL, DefaultConfigs.TAPSELL_INTERSTITIAL));
        zones.add(new Zone("Default Zone", ZoneType.NATIVE, DefaultConfigs.TAPSELL_NATIVE_BANNER));
        zones.add(new Zone("Default Zone", ZoneType.NATIVE_VIDEO, DefaultConfigs.TAPSELL_NATIVE_VIDEO));
        zones.add(new Zone("Default Zone", ZoneType.BANNER, DefaultConfigs.TAPSELL_STANDARD_BANNER));
        zones.add(new Zone("Default Zone", ZoneType.PRE_ROLL, DefaultConfigs.TAPSELL_VAST_PREROLL));
        return zones;
    }

    private static void load(Context context) {
        if (jsonAvailable != null) {
            return;
        }
        JSONObject root = readFromJson(context);
        if (root == null) {
            jsonAvailable = false;
            return;
        }
        jsonAvailable = true;
        cachedSecretKey = root.optString("secret-key").trim();
        cachedZones = parseZones(root);
    }

    private static List<Zone> parseZones(JSONObject root) {
        JSONArray arr = root.optJSONArray("zones");
        if (arr == null) {
            return new ArrayList<>();
        }
        List<Zone> zones = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.optJSONObject(i);
            if (obj == null) {
                continue;
            }
            String typeStr = obj.optString("type").trim().toUpperCase();
            ZoneType type;
            try {
                type = ZoneType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                continue; // unknown type -> skip
            }
            String name = obj.optString("name").trim();
            String id = obj.optString("id").trim();
            if (!name.isEmpty() && !id.isEmpty()) {
                zones.add(new Zone(name, type, id));
            }
        }
        return zones;
    }

    private static JSONObject readFromJson(Context context) {
        try (InputStream stream = context.getAssets().open(ZONES_FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            Log.i(TAG, "Cannot read " + ZONES_FILE_NAME, e);
            return null;
        }
    }
}
