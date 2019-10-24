package com.cat.aop.permission;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface IPermissionCheck {

    void checkPermissions(FragmentActivity fragmentActivity, IPermissionCheckResult permissionCheckResult, String...permissions);
    void checkPermissions(Fragment fragment, IPermissionCheckResult permissionCheckResult, String...permissions);
}
