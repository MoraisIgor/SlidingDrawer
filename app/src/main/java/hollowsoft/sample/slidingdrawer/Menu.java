package hollowsoft.sample.slidingdrawer;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;

public enum Menu {

    VERTICAL_DRAWER     (1, R.string.vertical_drawer_activity, R.drawable.ic_launcher, VerticalDrawerActivity.class),
    HORIZONTAL_DRAWER   (2, R.string.horizontal_drawer_activity, R.drawable.ic_launcher, HorizontalDrawerActivity.class),
    ABOUT               (5, R.string.about_activity, R.drawable.ic_launcher, AboutActivity.class);

    private final int id;
    private final int name;
    private final int icon;
    private final Class<?> menuClass;

    Menu(final int id, final int name, final int icon, final Class<?> menuClass) {

        this.id = id;
        this.name = name;
        this.icon = icon;
        this.menuClass = menuClass;
    }

    public final int getId() {
        return id;
    }

    public final int getName() {
        return name;
    }

    public final int getIcon() {
        return icon;
    }

    public final Class<?> getMenuClass() {
        return menuClass;
    }

    public final String getTag() {
        return menuClass.getSimpleName();
    }

    public final boolean isDialog() {
        return DialogFragment.class.isAssignableFrom(menuClass);
    }

    public final boolean isFragment() {
        return Fragment.class.isAssignableFrom(menuClass);
    }

    public final boolean isActivity() {
        return Activity.class.isAssignableFrom(menuClass);
    }

    public static Menu getBy(final int id) {

        for (final Menu menu : Menu.values()) {

            if (menu.getId() == id) {
                return menu;
            }
        }

        throw new IllegalArgumentException("The Menu has not found.");
    }

    public static Menu getBy(final String name) {

        for (final Menu menu : Menu.values()) {

            if (menu.name().equals(name)) {
                return menu;
            }
        }

        throw new IllegalArgumentException("The Menu has not found.");
    }
}
