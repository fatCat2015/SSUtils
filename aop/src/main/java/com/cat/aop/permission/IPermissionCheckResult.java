package com.cat.aop.permission;

import java.util.List;

public interface IPermissionCheckResult {
    /**
     * 所有的权限通过
     */
    void onGranted() ;

    /**
     * 部分权限被拒绝
     * @param deniedPermissions
     */
    void onDenied(List<String> deniedPermissions);

    /**
     * 部分权限被拒绝 并且被拒绝时 有权限勾选了不再提示
     * @param deniedPermissions  普通被拒绝权限
     * @param deniedPermissionsWithAskNeverAgain 勾选不再提示的拒绝权限
     */
    void onDeniedWithAskNeverAgain(List<String> deniedPermissions,List<String> deniedPermissionsWithAskNeverAgain);
}
