package com.cat.aop.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.cat.aop.ReflectUtils;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionCheckResult implements IPermissionCheckResult {


    private static Map<String,String> permissionDescriptions;

    static {
        permissionDescriptions=new HashMap<>();
        permissionDescriptions.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储");
        permissionDescriptions.put(Manifest.permission.CAMERA,"相机");
        permissionDescriptions.put(Manifest.permission.VIBRATE,"震动");
        permissionDescriptions.put(Manifest.permission.WAKE_LOCK,"唤醒");
        permissionDescriptions.put(Manifest.permission.RECORD_AUDIO,"录音");
        //TODO 添加更多权限的描述
    }

    private ProceedingJoinPoint joinPoint;

    public PermissionCheckResult(ProceedingJoinPoint joinPoint){
        this.joinPoint=joinPoint;
    }

    @Override
    public void onAllGranted()  {
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(),OnPermissionGranted.class);
    }

    @Override
    public void onDenied(List<String> deniedPermissions) {
        ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(),OnPermissionDenied.class);
    }

    @Override
    public void onDeniedWithAskNeverAgain(List<String> deniedPermissions, List<String> deniedPermissionsWithAskNeverAgain) {
        Context context=findContext();
        if(context==null){
            ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(),OnPermissionDeniedWithAskNeverAgain.class);
        }else{
            showAlertDialog(context,deniedPermissionsWithAskNeverAgain);
        }
    }

    private void showAlertDialog(Context context,List<String> deniedPermissionsWithAskNeverAgain){
        AlertDialog alertDialog=new AlertDialog.Builder(context)
                .setTitle("权限拒绝")
                .setMessage(String.format("%s权限被永久拒绝,功能无法使用!", getDeniedPermissionsDescription(deniedPermissionsWithAskNeverAgain)))
                .setPositiveButton("设置权限", (dialog, which) -> {
                    toPermissionsSetting(context);
                    ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(), OnPermissionSettings.class);
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(),OnPermissionDeniedWithAskNeverAgain.class);
                })
                .create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private String getDeniedPermissionsDescription(List<String> alwaysDeniedPermissions){
        StringBuilder stringBuilder=new StringBuilder();
        for (String permission:alwaysDeniedPermissions) {
            String permissionDes=permissionDescriptions.get(permission);
            if(!TextUtils.isEmpty(permissionDes)){
                stringBuilder.append(permissionDes);
                stringBuilder.append(",");
            }
        }
        if(stringBuilder.length()>1){
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
        }
        return stringBuilder.toString();
    }



    private Context findContext(){
        Object target=joinPoint.getTarget();
        Context context=null;
        if(target instanceof FragmentActivity){
            context= (Context) target;
        }else if(target instanceof Fragment){
            context=((Fragment) target).getActivity();
        }else{
            Object[] args = joinPoint.getArgs();
            if(args!=null&&args.length>0){
                for (Object parameter:args) {
                    if(parameter instanceof FragmentActivity){
                        context= (Context) parameter;
                        break;
                    }else if(parameter instanceof Fragment){
                        context=((Fragment) parameter).getActivity();
                        break;
                    }
                }
            }
        }
        return context;
    }

    /**
     * TODO 适配
     */
    private void  toPermissionsSetting(Context context){
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
