package com.cat.aop.permission;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;


public class PermissionCheck implements IPermissionCheck {

    @Override
    public void checkPermissions(FragmentActivity fragmentActivity, IPermissionCheckResult permissionCheckResult, String... permissions) {
        checkPermissions(new RxPermissions(fragmentActivity),permissionCheckResult,permissions);
    }

    @Override
    public void checkPermissions(Fragment fragment, IPermissionCheckResult permissionCheckResult, String... permissions) {
        checkPermissions(new RxPermissions(fragment),permissionCheckResult,permissions);
    }


    private void checkPermissions(RxPermissions rxPermissions,IPermissionCheckResult permissionCheckResult,String...permissions){

        List<String> deniedPermissions=new ArrayList<>();
        List<String> deniedPermissionsWithNeverAskAgain=new ArrayList<>();

        rxPermissions.requestEach(permissions)
                .doOnComplete(() -> {
                    if(deniedPermissions.isEmpty()&&deniedPermissionsWithNeverAskAgain.isEmpty()){
                        permissionCheckResult.onGranted();
                    }else if(deniedPermissionsWithNeverAskAgain.isEmpty()){
                        permissionCheckResult.onDenied(deniedPermissions);
                    }else{
                        permissionCheckResult.onDeniedWithAskNeverAgain(deniedPermissions,deniedPermissionsWithNeverAskAgain);
                    }
                })
                .subscribe(permission -> {
                    if (!permission.granted) {
                        if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            deniedPermissions.add(permission.name);
                        } else {
                            // Denied permission with never ask again
                            deniedPermissionsWithNeverAskAgain.add(permission.name);
                        }
                    }
                });
    }

}
