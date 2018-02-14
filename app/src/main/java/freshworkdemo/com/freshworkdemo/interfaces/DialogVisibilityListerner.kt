package freshworkdemo.com.freshworkdemo.interfaces

/**
 * @author Jass
 * @version 1.0
 */
interface DialogVisibilityListerner{
    fun showProgressBar( isShowing: Boolean)
    fun checkPermissionStatus(): Boolean
    fun setUpPermission()

}