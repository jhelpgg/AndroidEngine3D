/*
 *  <h1>License :</h1> <br/>
 * The following code is deliver as is. <br/>
 *  You can use, modify, the code as your need for any usage.<br/>
 *  But you can't do any action that avoid me or other person use, modify this code.<br/>
 *  The code is free for usage and modification, you can't change that fact.
 */

package fr.jhelp.multitools.showroom

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

const val SHOW_ROOM_TITLE_TYPE = 0
const val SHOW_ROOM_PREVIEW_TYPE = 1

sealed class ShowRoomElement(val type: Int)

class ShowRoomTitle(@StringRes val title: Int) : ShowRoomElement(SHOW_ROOM_TITLE_TYPE)

class ShowRoomPreview(@DrawableRes val image: Int, @StringRes val description: Int,
                      val activityClass: Class<out Activity>) :
    ShowRoomElement(SHOW_ROOM_PREVIEW_TYPE)