package ir.tapsell.plussample.android.model;

import java.io.Serializable;

import ir.tapsell.plussample.android.enums.ListItemType;

public class ItemList implements Serializable {
    public ListItemType listItemType;
    public String id;
    public String title;
}
