import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.*
import java.util.concurrent.LinkedBlockingQueue



object ShowOneDialogHandler{


    private val dialogMap= mutableMapOf<FragmentManager,LinkedList<DialogWithState>>()

    fun show(fragmentManager: FragmentManager,dialogFragment: DialogFragment){
        var dialogList= dialogMap[fragmentManager]
        if(dialogList==null){
            dialogList= LinkedList()
            dialogMap[fragmentManager]=dialogList
        }
        if(dialogList.isEmpty()){
            showAllowingStateLoss(fragmentManager,dialogFragment)
        }
        dialogList.addFirst(DialogWithState(dialogFragment,fragmentManager,dialogList))

    }


    private fun showAllowingStateLoss(fragmentManager: FragmentManager,dialogFragment: DialogFragment) {
        fragmentManager.beginTransaction().add(dialogFragment, javaClass.simpleName).commitAllowingStateLoss()
    }


    private class DialogWithState(private val dialog:DialogFragment,
                          private val fragmentManager: FragmentManager,
                          private val dialogList:LinkedList<DialogWithState>):LifecycleObserver{


        init {
            dialog.lifecycle.addObserver(this)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDialogDestroy(){
            dialog.lifecycle.removeObserver(this)
            dialogList.remove(this)
            if(dialogList.isNotEmpty()){
                val nextDialog=dialogList.last()
                showAllowingStateLoss(fragmentManager,nextDialog.dialog)
            }
        }
    }


}