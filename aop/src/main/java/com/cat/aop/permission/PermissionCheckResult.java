package com.cat.aop.permission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.cat.aop.R;
import com.cat.aop.ReflectUtils;
import com.cat.aop.permission.annotation.OnPermissionDenied;
import com.cat.aop.permission.annotation.OnPermissionDeniedWithNeverAskAgain;
import com.cat.aop.permission.annotation.OnPermissionGranted;
import com.cat.aop.permission.annotation.OnPermissionSettings;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionCheckResult implements IPermissionCheckResult {


    private static Map<String,Integer> permissionDescriptions;

    private boolean dispatchCheckResult;

    static {
        permissionDescriptions=new HashMap<>();
        permissionDescriptions.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,R.string.permission_write_external_storage);
        permissionDescriptions.put(Manifest.permission.CAMERA,R.string.permission_camera);
        permissionDescriptions.put(Manifest.permission.VIBRATE,R.string.permission_vibrate);
        permissionDescriptions.put(Manifest.permission.RECORD_AUDIO,R.string.permission_record_audio);
    }

    private ProceedingJoinPoint joinPoint;

    public PermissionCheckResult(ProceedingJoinPoint joinPoint,boolean dispatchCheckResult){
        this.joinPoint=joinPoint;
        this.dispatchCheckResult=dispatchCheckResult;
    }

    @Override
    public void onGranted()  {
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        if(dispatchCheckResult){
            ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(), OnPermissionGranted.class);
        }
    }

    @Override
    public void onDenied(List<String> deniedPermissions) {
        if(dispatchCheckResult){
            ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(), OnPermissionDenied.class);
        }
    }

    @Override
    public void onDeniedWithAskNeverAgain(List<String> deniedPermissions, List<String> deniedPermissionsWithAskNeverAgain) {
        Context context=findContext();
        if(context==null){
            if(dispatchCheckResult){
                ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(), OnPermissionDeniedWithNeverAskAgain.class);
            }
        }else{
            showAlertDialog(context,deniedPermissionsWithAskNeverAgain);
        }
    }

    private void showAlertDialog(final Context context,List<String> deniedPermissionsWithAskNeverAgain){
        AlertDialog alertDialog=new AlertDialog.Builder(context)
                .setTitle(R.string.permission_alert_title)
                .setMessage(String.format(context.getString(R.string.permission_alert_description), getPermissionDescription(context,deniedPermissionsWithAskNeverAgain)))
                .setPositiveButton(R.string.permission_alert_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toPermissionsSetting(context);
                        if(dispatchCheckResult){
                            ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(), OnPermissionSettings.class);
                        }
                    }
                })
                .setNegativeButton(R.string.permission_alert_cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dispatchCheckResult){
                            ReflectUtils.executeMethodWithAnnotation(joinPoint.getTarget(), OnPermissionDeniedWithNeverAskAgain.class);
                        }
                    }
                } )
                .create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private String getPermissionDescription(Context context,List<String> alwaysDeniedPermissions){
        StringBuilder stringBuilder=new StringBuilder();
        for (String permission:alwaysDeniedPermissions) {
            String permissionDes=context.getString(permissionDescriptions.get(permission));
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
