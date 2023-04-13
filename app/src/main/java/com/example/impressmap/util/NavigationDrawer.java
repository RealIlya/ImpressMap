package com.example.impressmap.util;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.impressmap.R;
import com.example.impressmap.model.MenuItemMeta;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class NavigationDrawer implements NavigationView.OnNavigationItemSelectedListener
{
    private final int PROFILE_GROUP = 0;
    private final int SETTINGS_GROUP = 1;
    private final Context context;
    private final NavigationView navigationView;
    private final DrawerLayout drawerLayout;
    private final FragmentManager fragmentManager;
    private final Map<Integer, MenuItemMeta> menuItemMetas = new HashMap<>();

    public NavigationDrawer(Context context,
                            NavigationView navigationView,
                            DrawerLayout drawerLayout,
                            FragmentManager fragmentManager)
    {
        this.context = context;
        this.navigationView = navigationView;
        this.drawerLayout = drawerLayout;
        this.fragmentManager = fragmentManager;

        initNavigationMenu();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        menuItemMetas.get(item.getItemId()).getClickListener().onClick();

        close();

        return true;
    }

    private void initNavigationMenu()
    {
        Menu menu = navigationView.getMenu();
        menu.clear();

        int order = 0;

        addNavigationMenuItem(menu, PROFILE_GROUP, order++, R.string.menu_profile, new MenuItemMeta(
                () -> fragmentManager.beginTransaction()
                                     .replace(R.id.container, new Fragment())
                                     .addToBackStack("")
                                     .commit()), R.drawable.ic_menu_profile);

        // the next submenu
        Menu submenuSettings = menu.addSubMenu(SETTINGS_GROUP, Menu.NONE, order,
                R.string.nav_header_item_settings);

        addNavigationMenuItem(submenuSettings, SETTINGS_GROUP, order, R.string.menu_language,
                new MenuItemMeta(() -> fragmentManager.beginTransaction()
                                                      .replace(R.id.container, new Fragment())
                                                      .addToBackStack("")
                                                      .commit()), R.drawable.ic_menu_international);
    }

    private void addNavigationMenuItem(Menu menu,
                                       int groupId,
                                       int order,
                                       @StringRes int title,
                                       MenuItemMeta menuItemMeta,
                                       @DrawableRes int icon)
    {
        addNavigationMenuItem(menu, groupId, order, context.getString(title), menuItemMeta, icon);
    }

    private void addNavigationMenuItem(Menu menu,
                                       int groupId,
                                       int order,
                                       CharSequence title,
                                       MenuItemMeta menuItemMeta,
                                       @DrawableRes int icon)
    {
        MenuItem menuItem = menu.add(groupId, order, order, title).setIcon(icon);

        menuItemMetas.put(menuItem.getItemId(), menuItemMeta);
    }

    public boolean isOpen()
    {
        return drawerLayout.isDrawerOpen(navigationView);
    }

    public void open()
    {
        drawerLayout.openDrawer(navigationView);
    }

    public boolean isClosed()
    {
        return !drawerLayout.isDrawerOpen(navigationView);
    }

    public void close()
    {
        drawerLayout.closeDrawer(navigationView);
    }
}
