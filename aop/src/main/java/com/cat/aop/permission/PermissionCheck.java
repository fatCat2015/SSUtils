package com.cat.aop.permission;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class PermissionCheck implements IPermissionCheck {

    @Override
    public void checkPermissions(FragmentActivity fragmentActivity, IPermissionCheckResult permissionCheckResult, String... permissions) {
        checkPermissions(new RxPermissions(fragmentActivity),permissionCheckResult,permissions);
    }

    @Override
    public void checkPermissions(Fragment fragment, IPermissionCheckResult permissionCheckResult, String... permissions) {
        checkPermissions(new RxPermissions(fragment),permissionCheckResult,permissions);
    }


    private void checkPermissions(RxPermissions rxPermissions,final IPermissionCheckResult permissionCheckResult,String...permissions){
        final List<String> deniedPermissions=new ArrayList<>();
        final List<String> deniedPermissionsWithNeverAskAgain=new ArrayList<>();
        rxPermissions.requestEach(permissions)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if(deniedPermissions.isEmpty()&&deniedPermissionsWithNeverAskAgain.isEmpty()){
                            permissionCheckResult.onGranted();
                        }else if(deniedPermissionsWithNeverAskAgain.isEmpty()){
                            permissionCheckResult.onDenied(deniedPermissions);
                        }else{
                            permissionCheckResult.onDeniedWithAskNeverAgain(deniedPermissions,deniedPermissionsWithNeverAskAgain);
                        }
                    }
                })
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (!permission.granted) {
                            if (permission.shouldShowRequestPermissionRationale) {
                                // Denied permission without ask never again
                                deniedPermissions.add(permission.name);
                            } else {
                                // Denied permission with never ask again
                                deniedPermissionsWithNeverAskAgain.add(permission.name);
                            }
                        }
                    }
                });
    }

}
