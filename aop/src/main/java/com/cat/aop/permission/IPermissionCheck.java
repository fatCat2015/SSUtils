package com.cat.aop.permission;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public interface IPermissionCheck {

    void checkPermissions(FragmentActivity fragmentActivity,IPermissionCheckResult permissionCheckResult,String...permissions);
    void checkPermissions(Fragment fragment, IPermissionCheckResult permissionCheckResult, String...permissions);
}
